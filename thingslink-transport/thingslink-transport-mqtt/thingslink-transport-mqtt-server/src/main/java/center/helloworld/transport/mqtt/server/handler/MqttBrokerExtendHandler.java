package center.helloworld.transport.mqtt.server.handler;

import center.helloworld.transport.mqtt.server.channel.ChannelRegister;
import center.helloworld.transport.mqtt.server.mqttextendmsg.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @date 2025/4/21
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class MqttBrokerExtendHandler extends SimpleChannelInboundHandler<MqttMessage> {

    public static final String HANDLER_NAME = "extendHandler";

    @Autowired
    private ChannelRegister channelRegister;

    /**
     * 连接成功
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive~~~~~");
//        channelRegister.registerChannel(ctx.channel().id().asLongText(), ctx.channel());
//        channelRegister.print();
        super.channelActive(ctx);
    }

    /**
     * 断开连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("channelInactive~~~~~");
        // 去除 channel
        Object clientId = ctx.channel().attr(AttributeKey.valueOf("clientId")).get();
        if(clientId != null) {
            channelRegister.removeChannel((String)clientId);
            channelRegister.print();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        log.info("channelRead~~~~~");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelRegistered~~~~~");
        super.channelRegistered(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        log.info("handlerRemoved~~~~~");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        System.out.println("出现异常喽~~~");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage mqttMessage) throws Exception {
        if(mqttMessage != null) {
            /**
             * 对MQTT未封装的消息类型进行扩展
             */
            MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
            Object variableHeader = mqttMessage.variableHeader();
            MqttMessageType mqttMessageType = mqttFixedHeader.messageType();
            switch (mqttMessageType) {
                // DISCONNECT消息
                case DISCONNECT:
                    mqttMessage = new MqttDisconnectMessage(mqttFixedHeader,variableHeader);
                    break;
                // PUBREC消息
                case PUBREC:
                    mqttMessage = new MqttPubRecMessage(mqttFixedHeader, variableHeader);
                    break;
                // PUBREL消息
                case PUBREL:
                    mqttMessage = new MqttPubRelMessage(mqttFixedHeader, variableHeader);
                    break;
                case PUBLISH:
                    ((MqttPublishMessage)mqttMessage).retain();
                    break;
                // PING REQ 消息
                case PUBCOMP:
                    mqttMessage = new MqttPubCompMessage(mqttFixedHeader, variableHeader);
                    break;
                // PING REQ 消息
                case PINGREQ:
                    mqttMessage = new MqttPingReqMessage(mqttFixedHeader, variableHeader);
                    break;
            }
        }
        ctx.fireChannelRead(mqttMessage);
    }
}
