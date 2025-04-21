package center.helloworld.transport.mqtt.server.handler;

import center.helloworld.transport.mqtt.server.mqttextendmsg.MqttPubCompMessage;
import center.helloworld.transport.mqtt.server.protocol.pubcomp.PubComp;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 * PUBCOMP
 *
 * @author zhishun.cai
 * @date 2024/11/19
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MqttPubCompMessageHandler extends SimpleChannelInboundHandler<MqttPubCompMessage> {

  public static final String HANDLER_NAME = "mqttPubCompMessageHandler";

  @Resource
  private PubComp pubComp;

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttPubCompMessage mqttPubCompMessage) throws Exception {
    Channel channel = channelHandlerContext.channel();
    log.info("pubComp event, channel id: {}", channel.id());
    pubComp.processPubComp(channel, mqttPubCompMessage);
  }
}
