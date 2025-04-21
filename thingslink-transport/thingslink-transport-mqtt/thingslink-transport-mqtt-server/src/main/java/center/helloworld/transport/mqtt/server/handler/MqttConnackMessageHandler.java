package center.helloworld.transport.mqtt.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
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
public class MqttConnackMessageHandler extends SimpleChannelInboundHandler<MqttConnAckMessage> {

  public static final String HANDLER_NAME = "mqttConnackMessageHandler";


  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttConnAckMessage mqttConnAckMessage) throws Exception {
    Channel channel = channelHandlerContext.channel();
    log.info("connAck event, channel id: {}", channel.id());
  }
}
