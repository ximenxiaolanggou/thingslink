package center.helloworld.transport.mqtt.server.protocol.subscribe.impl;

import center.helloworld.transport.mqtt.server.protocol.subscribe.Subscribe;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note Subscribe
 */

@Component
public class DefaultSubscribe implements Subscribe {

    public void process(Channel channel, MqttSubscribeMessage message) {

        List<MqttTopicSubscription> topicSubscriptions = message.payload().topicSubscriptions();
        List<Integer> topics = topicSubscriptions.stream().map(topic -> topic.qualityOfService().value()).collect(Collectors.toList());
        MqttSubAckMessage subAckMessage = (MqttSubAckMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(message.variableHeader().messageId()),
                new MqttSubAckPayload(topics));
        channel.writeAndFlush(subAckMessage);
    }
}
