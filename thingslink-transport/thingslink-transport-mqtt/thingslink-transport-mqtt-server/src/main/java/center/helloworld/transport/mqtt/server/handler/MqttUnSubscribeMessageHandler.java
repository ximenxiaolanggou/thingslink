package center.helloworld.transport.mqtt.server.handler;

import center.helloworld.transport.mqtt.server.protocol.unsubscribe.UnSubscribe;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttUnsubscribeMessage;
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
public class MqttUnSubscribeMessageHandler extends SimpleChannelInboundHandler<MqttUnsubscribeMessage> {

  public static final String HANDLER_NAME = "mqttUnSubscribeMessageHandler";

  @Resource
  private UnSubscribe unSubscribe;

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttUnsubscribeMessage mqttUnsubscribeMessage) throws Exception {
    Channel channel = channelHandlerContext.channel();
    log.info("unSubscribe event, channel id: {}", channel.id());
    unSubscribe.process(channel, mqttUnsubscribeMessage);
  }
}
