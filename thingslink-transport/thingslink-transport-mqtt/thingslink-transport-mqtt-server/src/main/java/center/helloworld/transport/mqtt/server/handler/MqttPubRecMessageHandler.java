package center.helloworld.transport.mqtt.server.handler;

import center.helloworld.transport.mqtt.server.mqttextendmsg.MqttPubRecMessage;
import center.helloworld.transport.mqtt.server.protocol.pubrec.PubRec;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 * PUBREC
 *
 * @author zhishun.cai
 * @date 2024/11/19
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MqttPubRecMessageHandler extends SimpleChannelInboundHandler<MqttPubRecMessage> {

  public static final String HANDLER_NAME = "mqttPubRecMessageHandler";

  @Resource
  private PubRec pubRec;

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttPubRecMessage mqttPubRecMessage) throws Exception {
    Channel channel = channelHandlerContext.channel();
    log.info("pubRec event, channel id: {}", channel.id());
    pubRec.processPubRec(channel, mqttPubRecMessage);
  }
}
