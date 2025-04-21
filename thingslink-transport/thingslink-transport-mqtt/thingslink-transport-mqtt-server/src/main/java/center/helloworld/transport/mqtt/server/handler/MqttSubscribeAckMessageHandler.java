package center.helloworld.transport.mqtt.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttSubAckMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * PUBREL
 *
 * @author zhishun.cai
 * @date 2024/11/19
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MqttSubscribeAckMessageHandler extends SimpleChannelInboundHandler<MqttSubAckMessage> {

  public static final String HANDLER_NAME = "mqttSubscribeAckMessageHandler";


  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttSubAckMessage mqttSubAckMessage) throws Exception {
    Channel channel = channelHandlerContext.channel();
    log.info("Subscribe ack event, channel id: {}", channel.id());
  }
}
