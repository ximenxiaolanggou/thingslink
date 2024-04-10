package center.helloworld.transport.mqtt.server.protocol.unsubscribe.impl;

import center.helloworld.transport.mqtt.server.protocol.unsubscribe.UnSubscribe;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note UnSubscribe 处理
 */

@Component
public class DefaultUnSubscribe implements UnSubscribe {

    @Override
    public void process(Channel channel, MqttUnsubscribeMessage msg) {
        List<String> topicFilters = msg.payload().topics();

        MqttUnsubAckMessage unsubAckMessage = (MqttUnsubAckMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(msg.variableHeader().messageId()), null);
        channel.writeAndFlush(unsubAckMessage);
    }
}
