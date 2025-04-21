package center.helloworld.transport.mqtt.server.protocol.pubrel.impl;

import center.helloworld.transport.mqtt.server.mqttextendmsg.MqttPubRelMessage;
import center.helloworld.transport.mqtt.server.protocol.pubrel.PubRel;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note
 */

@Component
public class DefaultPubrel implements PubRel {

    @Override
    public void process(Channel channel, MqttPubRelMessage mqttPubRelMessage) {

        int messageId = mqttPubRelMessage.variableHeader().messageId();

        // 组装并发送pubCom消息
        MqttMessage pubCompMessage = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBCOMP, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(messageId), null);
        channel.writeAndFlush(pubCompMessage);
    }
}
