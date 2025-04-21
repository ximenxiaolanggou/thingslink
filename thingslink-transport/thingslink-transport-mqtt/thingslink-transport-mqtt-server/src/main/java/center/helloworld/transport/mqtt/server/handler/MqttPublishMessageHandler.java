package center.helloworld.transport.mqtt.server.handler;

import center.helloworld.transport.mqtt.server.protocol.publish.Publish;
import center.helloworld.transport.mqtt.server.protocol.publish.factory.QosPublishFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * 发布消息事件
 *
 * @author zhishun.cai
 * @date 2024/11/19
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MqttPublishMessageHandler extends SimpleChannelInboundHandler<MqttPublishMessage> {

  public static final String HANDLER_NAME = "mqttPublishMessageHandler";

  @Autowired
  private QosPublishFactory qosPublishFactory;

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttPublishMessage mqttPublishMessage) throws Exception {
    Channel channel = channelHandlerContext.channel();
    log.info("publish event, channel id: {}", channel.id());
    MqttQoS mqttQoS = mqttPublishMessage.fixedHeader().qosLevel();
    Publish publish = qosPublishFactory.getQosLevelPublishHandler(mqttQoS);
    publish.process(channel, mqttPublishMessage);
  }
}
