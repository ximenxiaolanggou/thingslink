package center.helloworld.transport.mqtt.server.protocol.publish.impl;

import center.helloworld.transport.mqtt.server.protocol.publish.Publish;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.springframework.stereotype.Component;

import static io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader.from;
import static io.netty.handler.codec.mqtt.MqttQoS.AT_MOST_ONCE;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note Qos2 消息
 */
@Component("qos2Publish")
public class Qos2Publish implements Publish {

    @Override
    public void process(Channel channel, MqttPublishMessage msg) {
        int packetId = msg.variableHeader().packetId();
        System.out.println("Qos2Publish -> " + packetId);
        System.out.println(msg);


        // 创建pubRec报文
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBREC, false, AT_MOST_ONCE,
                false, 0);
        MqttPubAckMessage pubRecMessage = new MqttPubAckMessage(fixedHeader, from(packetId));
        channel.writeAndFlush(pubRecMessage);
    }
}
