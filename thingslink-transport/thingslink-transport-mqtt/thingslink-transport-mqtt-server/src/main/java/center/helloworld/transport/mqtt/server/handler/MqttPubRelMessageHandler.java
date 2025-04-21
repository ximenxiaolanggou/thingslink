package center.helloworld.transport.mqtt.server.handler;

import center.helloworld.transport.mqtt.server.mqttextendmsg.MqttPubRelMessage;
import center.helloworld.transport.mqtt.server.protocol.pubrel.PubRel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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
public class MqttPubRelMessageHandler extends SimpleChannelInboundHandler<MqttPubRelMessage> {

  public static final String HANDLER_NAME = "mqttPubRelMessageHandler";

  @Resource
  private PubRel pubRel;

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttPubRelMessage mqttPubRelMessage) throws Exception {
    Channel channel = channelHandlerContext.channel();
    log.info("pubRel event, channel id: {}", channel.id());
    pubRel.process(channel, mqttPubRelMessage);
  }
}
