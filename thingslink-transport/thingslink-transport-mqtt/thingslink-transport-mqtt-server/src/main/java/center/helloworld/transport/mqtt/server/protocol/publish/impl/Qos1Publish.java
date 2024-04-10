package center.helloworld.transport.mqtt.server.protocol.publish.impl;

import center.helloworld.transport.mqtt.server.protocol.publish.Publish;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note Qos1 消息
 */
@Component("qos1Publish")
public class Qos1Publish implements Publish {

    @Override
    public void process(Channel channel, MqttPublishMessage msg) {
        int packetId = msg.variableHeader().packetId();
        System.out.println("Qos1Publish -> " + packetId);
        System.out.println(msg);

        MqttFixedHeader pubAckFixedHeader = new MqttFixedHeader(MqttMessageType.PUBACK, false,
                MqttQoS.AT_LEAST_ONCE, false, 0);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(msg.variableHeader().packetId()) ;//MqttMessageIdVariableHeader.from(mqttMessage.variableHeader().packetId());
        MqttPubAckMessage pubAck = new MqttPubAckMessage(pubAckFixedHeader, variableHeader);
        channel.writeAndFlush(pubAck);
    }
}
