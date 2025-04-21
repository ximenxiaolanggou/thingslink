package center.helloworld.transport.mqtt.server.handler;

import center.helloworld.transport.mqtt.server.protocol.puback.PubAck;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * MQTT连接ACK事件
 *
 * @author zhishun.cai
 * @date 2024/11/19
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MqttPubAckMessageHandler extends SimpleChannelInboundHandler<MqttPubAckMessage> {

  public static final String HANDLER_NAME = "mqttPubAckMessageHandler";

  @Autowired
  private PubAck pubAck;

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttPubAckMessage mqttPubAckMessage) throws Exception {
    Channel channel = channelHandlerContext.channel();
    log.info("pubAck event, channel id: {}", channel.id());
    pubAck.process(channel, mqttPubAckMessage);
  }
}
