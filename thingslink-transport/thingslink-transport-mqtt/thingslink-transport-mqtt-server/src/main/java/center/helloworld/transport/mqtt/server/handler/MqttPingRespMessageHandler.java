package center.helloworld.transport.mqtt.server.handler;

import center.helloworld.transport.mqtt.server.mqttextendmsg.MqttPingRespMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
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
public class MqttPingRespMessageHandler extends SimpleChannelInboundHandler<MqttPingRespMessage> {

  public static final String HANDLER_NAME = "mqttPingRespMessageHandler";


  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttPingRespMessage mqttPingRespMessage) throws Exception {
    Channel channel = channelHandlerContext.channel();
    log.info("pingResp event, channel id: {}", channel.id());
  }
}
