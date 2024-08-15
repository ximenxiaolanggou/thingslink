package center.helloworld.transport.mqtt.server.handler;

import center.helloworld.transport.mqtt.server.channel.ChannelRegister;
import center.helloworld.transport.mqtt.server.protocol.ProtocolProcesser;
import center.helloworld.transport.mqtt.server.protocol.publish.observer.PublishMessageSubject;
import center.helloworld.transport.mqtt.server.session.SessionStore;
import center.helloworld.transport.mqtt.server.session.entity.Session;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @create 2024/3/18
 * @note
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MqttServerMessageHandler extends SimpleChannelInboundHandler<MqttMessage> {

    @Autowired
    private ProtocolProcesser protocolProcesser;

    @Autowired
    private SessionStore sessionDao;

    @Autowired
    private ChannelRegister channelRegister;

    @Autowired
    private PublishMessageSubject publishMessageSubject;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage) throws Exception {
        Channel channel = channelHandlerContext.channel();
        MqttMessageType msgType = mqttMessage.fixedHeader().messageType();
        switch (msgType) {
            case CONNECT -> protocolProcesser.connectProcess().process(channel, (MqttConnectMessage) mqttMessage);
            case DISCONNECT -> protocolProcesser.disConnectProcess().disConnectProcess(channel, mqttMessage);
            case PINGREQ -> protocolProcesser.pingProcess().process(channel, mqttMessage);
            case PUBLISH -> protocolProcesser.publishProcess((MqttPublishMessage) mqttMessage).process(publishMessageSubject, channel, (MqttPublishMessage) mqttMessage);
            case PUBREL -> protocolProcesser.pubRelProcess().process(channel, (MqttMessageIdVariableHeader) mqttMessage.variableHeader());
            case SUBSCRIBE -> protocolProcesser.subscribeProcess().process(channel, (MqttSubscribeMessage) mqttMessage);
            case UNSUBSCRIBE -> protocolProcesser.unsubscribeProcess().process(channel, (MqttUnsubscribeMessage) mqttMessage);
            default -> throw new RuntimeException("not should here");
        }
        log.info("message type is {}", msgType);
        log.info("channelRead0 message -> {}", mqttMessage.toString());
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        log.info("channelRegistered ~~");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        log.info("channelUnregistered ~~");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("channelActive ~~");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        String sessionId = null;
        try {
            Object cleanCacheObj = ctx.channel().attr(AttributeKey.valueOf("cleanCache")).get();
            // 当cleanCache为空时代表客户端突然下线，没有走DISCONNECT报文
            Channel channel = ctx.channel();
            Object sessionIdObj = (String)channel.attr(AttributeKey.valueOf("sessionId")).get();
            if(sessionIdObj != null) {
                sessionId = (String) sessionIdObj;
                // TODO 会话处理
                Session session = sessionDao.sessionBySessionId(sessionId);
                if(session != null && cleanCacheObj == null && session.getExpire() == 0) {
                    sessionDao.removeBySessionId(sessionId);
                    // TODO 删除其余数据
                }
                // TODO 遗言处理
                log.debug("DISCONNECT - clientId: {}", sessionId);
                System.out.println("disConnectProcess");
            }
            channel.close();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(StringUtils.isNotBlank(sessionId)) {
                channelRegister.removeChannel(sessionId);
            }
        }
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        log.info("channelReadComplete ~~");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        log.info("userEventTriggered ~~");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.info("exceptionCaught ~~");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

}
