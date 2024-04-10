package center.helloworld.transport.mqtt.server.protocol.pingreq.impl;

import center.helloworld.transport.mqtt.server.protocol.pingreq.Pingreq;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note
 */

@Component
public class DefaultPingreq implements Pingreq {

    @Override
    public void process(Channel channel, MqttMessage msg) {
        System.out.println("ping - > " + channel);
        System.out.println("ping - > " + msg);

        MqttMessage pingRespMessage = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0), null, null);
        channel.writeAndFlush(pingRespMessage);
    }
}
