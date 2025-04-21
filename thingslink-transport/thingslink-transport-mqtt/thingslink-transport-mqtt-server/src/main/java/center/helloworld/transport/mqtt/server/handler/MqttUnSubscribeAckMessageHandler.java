package center.helloworld.transport.mqtt.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttUnsubAckMessage;
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
public class MqttUnSubscribeAckMessageHandler extends SimpleChannelInboundHandler<MqttUnsubAckMessage> {

  public static final String HANDLER_NAME = "mqttUnSubscribeAckMessageHandler";


  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttUnsubAckMessage mqttUnsubAckMessage) throws Exception {
    Channel channel = channelHandlerContext.channel();
    log.info("unSubscribe ack event, channel id: {}", channel.id());
  }
}
