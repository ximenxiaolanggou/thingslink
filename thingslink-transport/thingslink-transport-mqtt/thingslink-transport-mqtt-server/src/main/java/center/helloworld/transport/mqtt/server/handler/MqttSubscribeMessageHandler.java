package center.helloworld.transport.mqtt.server.handler;

import center.helloworld.transport.mqtt.server.protocol.subscribe.Subscribe;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
public class MqttSubscribeMessageHandler extends SimpleChannelInboundHandler<MqttSubscribeMessage> {

  public static final String HANDLER_NAME = "mqttSubscribeMessageHandler";

  @Resource
  private Subscribe subscribe;

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttSubscribeMessage mqttSubscribeMessage) throws Exception {
    Channel channel = channelHandlerContext.channel();
    log.info("Subscribe event, channel id: {}", channel.id());
    subscribe.process(channel, mqttSubscribeMessage);
  }
}
