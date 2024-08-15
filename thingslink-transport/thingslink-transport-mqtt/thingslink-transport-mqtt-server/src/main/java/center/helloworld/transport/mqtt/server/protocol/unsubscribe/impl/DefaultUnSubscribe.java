package center.helloworld.transport.mqtt.server.protocol.unsubscribe.impl;

import center.helloworld.transport.mqtt.server.protocol.subscribe.service.SubscribeService;
import center.helloworld.transport.mqtt.server.protocol.unsubscribe.UnSubscribe;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note UnSubscribe 处理
 */

@Slf4j
@Component
public class DefaultUnSubscribe implements UnSubscribe {

    @Autowired
    private SubscribeService subscribeStoreService;

    @Override
    public void unSubscribe(Channel channel, MqttUnsubscribeMessage msg) {
        List<String> topicFilters = msg.payload().topics();
        String clinetId = (String) channel.attr(AttributeKey.valueOf("clientIdentifier")).get();
        topicFilters.forEach(topicFilter -> {
            subscribeStoreService.remove(topicFilter, clinetId);
            log.debug("UNSUBSCRIBE - clientId: {}, topicFilter: {}", clinetId, topicFilter);
        });
        MqttUnsubAckMessage unsubAckMessage = (MqttUnsubAckMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(msg.variableHeader().messageId()), null);
        channel.writeAndFlush(unsubAckMessage);
    }
}
