package center.helloworld.transport.mqtt.server.protocol.connect.impl;

import center.helloworld.transport.mqtt.server.auth.IAuthStrategy;
import center.helloworld.transport.mqtt.server.channel.ChannelRegister;
import center.helloworld.transport.mqtt.server.handler.*;
import center.helloworld.transport.mqtt.server.message.WillMessage;
import center.helloworld.transport.mqtt.server.protocol.connect.Connect;
import center.helloworld.transport.mqtt.server.session.entity.Session;
import center.helloworld.transport.mqtt.server.session.dao.ISessionDao;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private MqttConnackMessageHandler mqttConnackMessageHandler;

    @Autowired
    private MqttDisConnectMessageHandler mqttDisConnectMessageHandler;

    @Autowired
    private MqttPingReqMessageHandler mqttPingReqMessageHandler;

    @Autowired
    private MqttPingRespMessageHandler mqttPingRespMessageHandler;

    @Autowired
    private MqttPubAckMessageHandler mqttPubAckMessageHandler;

    @Autowired
    private MqttPubCompMessageHandler mqttPubCompMessageHandler;

    @Autowired
    private MqttPublishMessageHandler mqttPublishMessageHandler;

    @Autowired
    private MqttPubRecMessageHandler mqttPubRecMessageHandler;

    @Autowired
    private MqttPubRelMessageHandler mqttPubRelMessageHandler;

    @Autowired
    private MqttSubscribeAckMessageHandler mqttSubscribeAckMessageHandler;

    @Autowired
    private MqttSubscribeMessageHandler mqttSubscribeMessageHandler;

    @Autowired
    private MqttUnSubscribeAckMessageHandler mqttUnSubscribeAckMessageHandler;

    @Autowired
    private MqttUnSubscribeMessageHandler mqttUnSubscribeMessageHandler;

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
            // 删除会话
            Channel toRemoveChannel = channelRegister.getChannel(clientId);
            if(toRemoveChannel != null) {
                MqttReasonCodes.Disconnect disconnectCode = MqttReasonCodes.Disconnect.SESSION_TAKEN_OVER;
                toRemoveChannel.writeAndFlush(disconnectCode);
                toRemoveChannel.close();
            }

            // 删除会话
            channelRegister.removeChannel(clientId);
            return true;
        }

        return false;
    }

    /**
     * 心跳处理
     * @param channel
     * @param msg
     * @return
     */
    @Override
    public int heartHandle(Channel channel, MqttConnectMessage msg) {
        ChannelPipeline pipeline = channel.pipeline();
        // 心跳检查handler
        int expire = 0;
        if (msg.variableHeader().keepAliveTimeSeconds() > 0) {
            // 去除默认、添加新的
            if (pipeline.names().contains("idle")) {
                pipeline.remove("idle");
            }
            expire = Math.round(msg.variableHeader().keepAliveTimeSeconds() * 1.5f);
            channel.pipeline().addFirst("idle", new IdleStateHandler(0, 0, expire));
        }
        return expire;
    }

    /**
     * 添加其余流水线handler
     * @param channel
     * @param msg
     * @return expire * 1.5
     */
    @Override
    public void pipelineAddHadnler(Channel channel, MqttConnectMessage msg) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.remove(MqttConnectMessageHandler.HANDLER_NAME);
        pipeline.addLast(MqttPublishMessageHandler.HANDLER_NAME, mqttPublishMessageHandler);
        pipeline.addLast(MqttPingReqMessageHandler.HANDLER_NAME, mqttPingReqMessageHandler);
        pipeline.addLast(MqttPingRespMessageHandler.HANDLER_NAME,mqttPingRespMessageHandler);
        pipeline.addLast(MqttPubAckMessageHandler.HANDLER_NAME,mqttPubAckMessageHandler);
        pipeline.addLast(MqttPubCompMessageHandler.HANDLER_NAME,mqttPubCompMessageHandler);
        pipeline.addLast(MqttPubRecMessageHandler.HANDLER_NAME,mqttPubRecMessageHandler);
        pipeline.addLast(MqttPubRelMessageHandler.HANDLER_NAME,mqttPubRelMessageHandler);
        pipeline.addLast(MqttSubscribeAckMessageHandler.HANDLER_NAME,mqttSubscribeAckMessageHandler);
        pipeline.addLast(MqttSubscribeMessageHandler.HANDLER_NAME,mqttSubscribeMessageHandler);
        pipeline.addLast(MqttUnSubscribeAckMessageHandler.HANDLER_NAME,mqttUnSubscribeAckMessageHandler);
        pipeline.addLast(MqttUnSubscribeMessageHandler.HANDLER_NAME,mqttUnSubscribeMessageHandler);
        pipeline.addLast(MqttConnackMessageHandler.HANDLER_NAME,mqttConnackMessageHandler);
        pipeline.addLast(MqttDisConnectMessageHandler.HANDLER_NAME,mqttDisConnectMessageHandler);
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
        // 是否清除会话
        boolean isCleanSession = msg.variableHeader().isCleanSession();
        // 属性信息
        MqttProperties properties = msg.variableHeader().properties();
        MqttProperties.MqttProperty expiryProp = properties.getProperty(MqttProperties.MqttPropertyType.SESSION_EXPIRY_INTERVAL.value());

        // 会话过期时间
        int sessionExpiryInterval = (expiryProp != null) ? (Integer) expiryProp.value() : 0;

        // 根据设备客户端ID获取会话信息
        Session oldSession = sessionStorage.sessionByClientId(clientId);
        // 新会话
        Session newSession = oldSession;

        if(newSession == null || isCleanSession) {
            // 会话不存在 或者 需要清除会话
            // 创建新会话直接覆盖旧会话
            newSession = new Session( clientId,  channelId, isCleanSession, null, sessionExpiryInterval);
            if(newSession != null) {
                // TODO 清除其余信息
            }
        }else {
            // 会话存在 并且 不需要cleanSession，则更新会话
            newSession.setCleanSession(isCleanSession);
            newSession.setChannelId(channelId);
            newSession.setExpire(sessionExpiryInterval);
        }

        // 遗言
       if( msg.variableHeader().isWillFlag()) {
           // 存在遗言消息
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

       }

        // 保存会话
        sessionStorage.storeSession(clientId, newSession);
        channelRegister.registerChannel(clientId, channel);
        return newSession;
    }


    @Override
    public void responseAckMessageHandle(Channel channel, MqttConnectMessage msg) {
        // 返回应答
        boolean sessionPresent = false /*sessionStoreService.containsKey(msg.payload().clientIdentifier()) && !msg.variableHeader().isCleanSession()*/;
        MqttConnAckMessage okResp = (MqttConnAckMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, sessionPresent), null);
        channel.writeAndFlush(okResp);
        log.info("CONNECT - clientId: {}, cleanSession: {}", msg.payload().clientIdentifier(), msg.variableHeader().isCleanSession());
    }
}
