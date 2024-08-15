package center.helloworld.transport.mqtt.server.protocol.subscribe.impl;

import center.helloworld.transport.mqtt.server.protocol.subscribe.Subscribe;
import center.helloworld.transport.mqtt.server.protocol.subscribe.entity.SubscribeStore;
import center.helloworld.transport.mqtt.server.protocol.subscribe.service.SubscribeService;
import center.helloworld.transport.mqtt.server.utils.TopicUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note Subscribe
 */

@Slf4j
@Component
public class DefaultSubscribe implements Subscribe {

    @Autowired
    private SubscribeService subscribeStoreService;


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
        List<String> topicNames = topicSubscriptions.stream().map(item -> item.topicName()).collect(Collectors.toList());
        return subscribeStoreService.validTopicFilter(Subscribe.topicReg, topicNames);
    }

    @Override
    public boolean aclTopic(Channel channel, MqttSubscribeMessage message) {
        return true;
    }

    @Override
    public void subAck(Channel channel, MqttSubscribeMessage message) {
        // 获取订阅的主题集合
        List<MqttTopicSubscription> topicSubscriptions = message.payload().topicSubscriptions();
        String clientId = (String) channel.attr(AttributeKey.valueOf("clientIdentifier")).get();


        List<Integer> mqttQoSList = new ArrayList<Integer>();
        topicSubscriptions.forEach(topicSubscription -> {
            String topicFilter = topicSubscription.topicName();
            MqttQoS mqttQoS = topicSubscription.qualityOfService();
            // 判断是否重复订阅
            SubscribeStore subscribeStore = subscribeStoreService.subscribed(clientId, topicFilter);
            if(subscribeStore == null) {
                // 封装成SubscribeStore对象
                subscribeStore = new SubscribeStore(clientId, topicFilter, mqttQoS.value());
            } else {
                subscribeStore.setMqttQoS(mqttQoS.value());
            }
            subscribeStoreService.put(topicFilter, subscribeStore);
            mqttQoSList.add(mqttQoS.value());
            log.debug("SUBSCRIBE - clientId: {}, topFilter: {}, QoS: {}", clientId, topicFilter, mqttQoS.value());
        });
        MqttSubAckMessage subAckMessage = (MqttSubAckMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(message.variableHeader().messageId()),
                new MqttSubAckPayload(mqttQoSList));
        channel.writeAndFlush(subAckMessage);
    }

    @Override
    public void sendRetainMessage(Channel channel, MqttSubscribeMessage message) {

    }
}
