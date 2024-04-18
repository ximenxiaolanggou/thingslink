package center.helloworld.transport.mqtt.server.handler;

import center.helloworld.transport.mqtt.server.codec.fixedheader.MqttControlPacketType;
import center.helloworld.transport.mqtt.server.codec.message.MqttMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @create 2024/4/15
 * @note
 */

@Slf4j
@ChannelHandler.Sharable
@Component
public class MqttMessageHandler extends SimpleChannelInboundHandler<MqttMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {
        System.out.println("handling ~~ res：" + msg);
    }

    public static void main(String[] args) {
        MqttControlPacketType pubrel = MqttControlPacketType.PUBREL;
    }
}
