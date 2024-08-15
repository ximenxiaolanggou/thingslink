package center.helloworld.transport.mqtt.server.protocol.connect.impl;

import center.helloworld.transport.mqtt.server.auth.IAuthStrategy;
import center.helloworld.transport.mqtt.server.channel.ChannelRegister;
import center.helloworld.transport.mqtt.server.message.MqttWillMessage;
import center.helloworld.transport.mqtt.server.protocol.connect.Connect;
import center.helloworld.transport.mqtt.server.session.entity.Session;
import center.helloworld.transport.mqtt.server.session.SessionStore;
import center.helloworld.transport.mqtt.server.utils.TopicUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhishun.cai
 * @date 2024/3/18
 * @note
 */

@Slf4j
@Service
public class DefaultConnect implements Connect {

    @Autowired
    private IAuthStrategy auth;

    @Autowired
    private SessionStore sessionStorage;

    @Autowired
    private ChannelRegister channelRegister;

    /**
     * 解析结果校验
     * @param channel
     * @param msg
     */
    @Override
    public boolean validateDecoderResult(Channel channel, MqttConnectMessage msg) {
        if(msg.decoderResult().isFailure()) {
            Throwable cause = msg.decoderResult().cause();
            if (cause instanceof MqttUnacceptableProtocolVersionException) {
                log.error("不支持协议版本，{}", msg.variableHeader().version());
                // 不支持的协议版本
                MqttConnAckMessage connAckMessage = (MqttConnAckMessage) MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                        new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_UNSUPPORTED_PROTOCOL_VERSION, false), null);
                channel.writeAndFlush(connAckMessage);
                channel.close();
                return false;
            } else if (cause instanceof MqttIdentifierRejectedException) {
                // 不合格的clientId
                log.error("不合格的clientId：{}", msg.payload().clientIdentifier());
                MqttConnAckMessage connAckMessage = (MqttConnAckMessage) MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                        new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_CLIENT_IDENTIFIER_NOT_VALID, false), null);
                channel.writeAndFlush(connAckMessage);
                channel.close();
                return false;
            }
            channel.close();
            return false;
        }
        return true;
    }

    /**
     * 设备认证
     * @param channel
     * @param message
     */
    @Override
    public boolean deviceAuth(Channel channel, MqttConnectMessage message) {
        MqttConnectReturnCode checkCode = this.auth.auth(message);
        // 拒绝连接
        if(checkCode != MqttConnectReturnCode.CONNECTION_ACCEPTED) {
            MqttConnAckMessage ackMessage = new MqttConnAckMessage(new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                    new MqttConnAckVariableHeader(checkCode, false));
            channel.writeAndFlush(ackMessage);
            channel.close();
            return false;
        }
        return true;
    }

    /**
     *
     * 清除会话处理
     *
     * 详细说明：https://www.emqx.com/zh/blog/mqtt5-new-feature-clean-start-and-session-expiry-interval
     * @param channel
     * @param message
     */
    @Override
    public boolean cleanSessionHandle(Channel channel, MqttConnectMessage message) {
        // 先获取会话
        String clientId = message.payload().clientIdentifier();
        boolean cleanSession = message.variableHeader().isCleanSession();
        Session session = sessionStorage.sessionByClientId(clientId);

        if(session != null && session.isActive()) {
            // 会话处于连接状态，关闭之前连接（新连接顶掉旧连接）|| 或者服务端突然down掉
            Channel toRemoveChannel = channelRegister.getChannel(session.getSessionId());
            if(toRemoveChannel != null) {
                // 正常情况
                MqttReasonCodes.Disconnect disconnectCode = MqttReasonCodes.Disconnect.CONNECTION_RATE_EXCEEDED;
                toRemoveChannel.writeAndFlush(disconnectCode);
                toRemoveChannel.close();
                channelRegister.removeChannel(session.getSessionId());
            }
        }

        if(cleanSession) {
            // 清除会话和相关脏数据
            if(session != null) {
                sessionStorage.removeBySessionId(session.getSessionId());
                // TODO 删除脏数据
            }
        }
        return session != null;
    }

    /**
     * 设置客户端心跳频率
     * @param channel
     * @param msg
     * @return
     */
    @Override
    public int heartHandle(Channel channel, MqttConnectMessage msg) {
        int expire = 0;
        if (msg.variableHeader().keepAliveTimeSeconds() > 0) {
            if (channel.pipeline().names().contains("idle")) {
                channel.pipeline().remove("idle");
            }
            expire = Math.round(msg.variableHeader().keepAliveTimeSeconds() * 1.5f);
            channel.pipeline().addFirst("idle", new IdleStateHandler(0, 0, expire));
        }
        return expire;
    }

    /**
     * 遗言消息处理
     * @param channel
     * @param msg
     * @return
     */
    @Override
    public Session willMessageHandle(Channel channel, MqttConnectMessage msg) {
        String clientId = msg.payload().clientIdentifier();
        boolean isCleanSession = msg.variableHeader().isCleanSession();
        Session session = new Session(clientId, isCleanSession, null, (int)msg.variableHeader().properties().getProperty(17).value());

        if(!isCleanSession) {
            // 不清除会话,则使用相同session id（新连接相关配置信息可能修改，这里不使用之前的配置信息）
            Session oSession = sessionStorage.sessionByClientId(clientId);
            if(oSession != null) {
                session.setSessionId(oSession.getSessionId());
            }
        }

        if(msg.variableHeader().isWillFlag()) {
            // 存在遗言，校验topic
            String willTopic = msg.payload().willTopic();
            if(willTopic == null || willTopic.trim() == "" || TopicUtil.validTopic(willTopic)) {
                // will topic 不合规
                MqttConnAckMessage connAckMessage = (MqttConnAckMessage) MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                        new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_TOPIC_NAME_INVALID, false), null);
                channel.writeAndFlush(connAckMessage);
                channel.close();
                return null;
            }
            // TODO topic ACL控制

            // 存储一样消息
            // willMessage序列化存储到redis中会报循环错误，并且错误原因为buffer，不好解决这里就自定义结构
            MqttWillMessage willMessage = new MqttWillMessage(
                    clientId,
                    msg.payload().willTopic(),
                    MqttQoS.valueOf(msg.variableHeader().willQos()),
                    msg.payload().willMessageInBytes());
            session.setWillMessage(willMessage);
        }
        // 至此存储会话信息及返回接受客户端连接
        session.setChannelId(channel.id());
        sessionStorage.storeSession(session.getSessionId(), session);
        // 存储 会话和channel关系
        channelRegister.registerChannel(session.getSessionId(), channel);
        // 将clientId存储到channel的map中
        channel.attr(AttributeKey.valueOf("clientIdentifier")).set(clientId);
        channel.attr(AttributeKey.valueOf("sessionId")).set(session.getSessionId());
        return session;
    }


    @Override
    public void connAckMessageHandle(Channel channel, MqttConnectMessage msg, boolean sessionPresent) {
        // 返回应答
        sessionPresent = sessionPresent && !msg.variableHeader().isCleanSession();
        MqttConnAckMessage okResp = (MqttConnAckMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, sessionPresent), null);
        channel.writeAndFlush(okResp);
        log.info("CONNECT - clientId: {}, cleanSession: {}", msg.payload().clientIdentifier(), msg.variableHeader().isCleanSession());
    }
}
