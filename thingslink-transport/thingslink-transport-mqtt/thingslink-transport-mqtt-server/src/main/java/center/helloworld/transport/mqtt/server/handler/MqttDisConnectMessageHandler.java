package center.helloworld.transport.mqtt.server.handler;

import center.helloworld.transport.mqtt.server.mqttextendmsg.MqttDisconnectMessage;
import center.helloworld.transport.mqtt.server.protocol.disconnect.DisConnect;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * MQTT关闭连接事件
 *
 * @author zhishun.cai
 * @date 2024/11/19
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MqttDisConnectMessageHandler extends SimpleChannelInboundHandler<MqttDisconnectMessage> {

  public static final String HANDLER_NAME = "mqttDisConnectMessageHandler";

  @Autowired
  private DisConnect disConnect;

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttDisconnectMessage mqttDisconnectMessage) throws Exception {
    Channel channel = channelHandlerContext.channel();
    log.info("disConnect event, channel id: {}", channel.id());
    disConnect.process(channel, mqttDisconnectMessage);
  }
}
