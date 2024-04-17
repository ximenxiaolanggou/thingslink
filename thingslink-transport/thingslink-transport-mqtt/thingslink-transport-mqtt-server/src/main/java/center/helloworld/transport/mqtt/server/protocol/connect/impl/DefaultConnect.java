package center.helloworld.transport.mqtt.server.protocol.connect.impl;

import center.helloworld.transport.mqtt.server.auth.IAuthStrategy;
import center.helloworld.transport.mqtt.server.channel.ChannelRegister;
import center.helloworld.transport.mqtt.server.message.WillMessage;
import center.helloworld.transport.mqtt.server.protocol.connect.Connect;
import center.helloworld.transport.mqtt.server.session.entity.Session;
import center.helloworld.transport.mqtt.server.session.dao.ISessionDao;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleStateHandler;
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
    private ISessionDao sessionStorage;

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
        MqttConnAckMessage ackMessage = new MqttConnAckMessage(new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                new MqttConnAckVariableHeader(checkCode, false));
        channel.writeAndFlush(ackMessage);
        // 拒绝连接
        if(checkCode != MqttConnectReturnCode.CONNECTION_ACCEPTED) {
            channel.close();
            return false;
        }
        return true;
    }

    /**
     * 多连接处理
     * @param channel
     * @param message
     */
    @Override
    public boolean multiConnectHandle(Channel channel, MqttConnectMessage message) {
        String clientId = message.payload().clientIdentifier();
        Session session = sessionStorage.sessionByClientId(clientId);
        if(session != null) {// 会话已存在 - 新会话顶掉旧会话
            String sessionId = session.getSessionId();
            // 删除会话
            Channel toRemoveChannel = channelRegister.getChannel(sessionId);
            MqttReasonCodes.Disconnect disconnectCode = MqttReasonCodes.Disconnect.CONNECTION_RATE_EXCEEDED;
            toRemoveChannel.writeAndFlush(disconnectCode);
            toRemoveChannel.close();
            // 删除会话
            channelRegister.removeChannel(sessionId);
            // 删除会话存储数据 TODO 可以保留并让新会话发送
            return true;
        }

        return false;
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
        ChannelId channelId = channel.id();
        boolean isCleanSession = msg.variableHeader().isCleanSession();
        // 存在遗言， 校验topic是否合法，TODO 校验ACL
        Session session = new Session(clientId,  channelId, isCleanSession, null, 0);

        // TODO 校验ACL

        // willMessage序列化存储到redis中会报循环错误，并且错误原因为buffer，不好解决这里就自定义结构
        WillMessage message = new WillMessage(
                clientId,
                msg.payload().willTopic(),
                MqttQoS.valueOf(msg.variableHeader().willQos()),
               0,
                msg.payload().willMessageInBytes(),
                false,
                msg.variableHeader().isWillRetain(),
                true,
                0
        );

        return null;
    }


    @Override
    public void connAckMessageHandle(Channel channel, MqttConnectMessage msg) {
        // 返回应答
        boolean sessionPresent = false /*sessionStoreService.containsKey(msg.payload().clientIdentifier()) && !msg.variableHeader().isCleanSession()*/;
        MqttConnAckMessage okResp = (MqttConnAckMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, sessionPresent), null);
        channel.writeAndFlush(okResp);
        log.info("CONNECT - clientId: {}, cleanSession: {}", msg.payload().clientIdentifier(), msg.variableHeader().isCleanSession());
    }
}
