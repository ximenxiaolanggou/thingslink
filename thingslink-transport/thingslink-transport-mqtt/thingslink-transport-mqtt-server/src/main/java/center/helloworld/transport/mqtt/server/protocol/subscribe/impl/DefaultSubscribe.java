package center.helloworld.transport.mqtt.server.protocol.subscribe.impl;

import center.helloworld.transport.mqtt.server.protocol.subscribe.Subscribe;
import center.helloworld.transport.mqtt.server.utils.TopicUtil;
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

    @Override
    public void process(Channel channel, MqttSubscribeMessage message) {

        List<MqttTopicSubscription> topicSubscriptions = message.payload().topicSubscriptions();
        List<Integer> topics = topicSubscriptions.stream().map(topic -> topic.qualityOfService().value()).collect(Collectors.toList());
        MqttSubAckMessage subAckMessage = (MqttSubAckMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(message.variableHeader().messageId()),
                new MqttSubAckPayload(topics));
        channel.writeAndFlush(subAckMessage);
    }

    /**
     * 校验Topic
     * @param channel
     * @param message
     * @return
     */
    @Override
    public boolean validTopic(Channel channel, MqttSubscribeMessage message) {
        // 获取订阅的主题集合
        List<MqttTopicSubscription> topicSubscriptions = message.payload().topicSubscriptions();
        List<String> topicFilters = topicSubscriptions.stream().map(item -> item.topicFilter()).collect(Collectors.toList());
        return TopicUtil.validTopics(topicFilters);
    }

    @Override
    public boolean aclTopic(Channel channel, MqttSubscribeMessage message) {
        return true;
    }

    @Override
    public void subAck(Channel channel, MqttSubscribeMessage message) {

    }

    @Override
    public void sendRetainMessage(Channel channel, MqttSubscribeMessage message) {

    }
}
