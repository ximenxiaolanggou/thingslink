package center.helloworld.transport.mqtt.server.handler;

import center.helloworld.transport.mqtt.server.mqttextendmsg.MqttPingReqMessage;
import center.helloworld.transport.mqtt.server.protocol.pingreq.Pingreq;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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
public class MqttPingReqMessageHandler extends SimpleChannelInboundHandler<MqttPingReqMessage> {
  public static final String HANDLER_NAME = "mqttPingReqMessageHandler";

  @Autowired
  private Pingreq pingReq;

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttPingReqMessage pingReqMessage) throws Exception {
    Channel channel = channelHandlerContext.channel();
    log.info("pingReq event, channel id: {}", channel.id());
    pingReq.process(channel, pingReqMessage);
  }
}
