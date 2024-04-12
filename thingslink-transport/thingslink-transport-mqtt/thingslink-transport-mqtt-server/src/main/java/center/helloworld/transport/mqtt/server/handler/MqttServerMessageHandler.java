package center.helloworld.transport.mqtt.server.handler;

import center.helloworld.transport.mqtt.server.protocol.ProtocolProcesser;
import center.helloworld.transport.mqtt.server.session.entity.Session;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author zhishun.cai
 * @create 2024/3/18
 * @note
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class MqttServerMessageHandler extends SimpleChannelInboundHandler<MqttMessage> {

    @Autowired
    private ProtocolProcesser protocolProcesser;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage) throws Exception {
        Channel channel = channelHandlerContext.channel();
        MqttMessageType msgType = mqttMessage.fixedHeader().messageType();
        switch (msgType) {
            case CONNECT -> protocolProcesser.connectProcess().process(channel, (MqttConnectMessage) mqttMessage);
            case PINGREQ -> protocolProcesser.pingProcess().process(channel, mqttMessage);
            case PUBLISH -> protocolProcesser.publishProcess((MqttPublishMessage) mqttMessage).process(channel, (MqttPublishMessage) mqttMessage);
            case PUBREL -> protocolProcesser.pubRelProcess().process(channel, (MqttMessageIdVariableHeader) mqttMessage.variableHeader());
            case SUBSCRIBE -> protocolProcesser.subscribeProcess().process(channel, (MqttSubscribeMessage) mqttMessage);
            case UNSUBSCRIBE -> protocolProcesser.unsubscribeProcess().process(channel, (MqttUnsubscribeMessage) mqttMessage);
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
        log.info("channelInactive ~~");
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
