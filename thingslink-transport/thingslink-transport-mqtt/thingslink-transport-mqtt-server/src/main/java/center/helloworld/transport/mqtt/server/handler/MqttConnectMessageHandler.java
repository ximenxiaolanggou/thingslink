package center.helloworld.transport.mqtt.server.handler;

import center.helloworld.transport.mqtt.server.protocol.connect.Connect;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * MQTT连接事件
 *
 * @author zhishun.cai
 * @date 2024/11/19
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MqttConnectMessageHandler extends SimpleChannelInboundHandler<MqttConnectMessage> {

  public static final String HANDLER_NAME = "mqttConnectMessageHandler";

  @Autowired
  private Connect connect;

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttConnectMessage mqttConnectMessage) throws Exception {
    Channel channel = channelHandlerContext.channel();
    log.info("connect event, channel id: {}", channel.id());
    connect.process(channel, mqttConnectMessage);
  }
}
