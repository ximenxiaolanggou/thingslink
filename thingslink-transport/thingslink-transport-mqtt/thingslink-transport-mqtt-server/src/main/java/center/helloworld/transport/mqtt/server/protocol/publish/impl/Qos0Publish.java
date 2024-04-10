package center.helloworld.transport.mqtt.server.protocol.publish.impl;

import center.helloworld.transport.mqtt.server.protocol.publish.Publish;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note Qos0 消息
 */
@Component("qos0Publish")
public class Qos0Publish implements Publish {

    @Override
    public void process(Channel channel, MqttPublishMessage msg) {
        int packetId = msg.variableHeader().packetId();
        System.out.println("Qos0Publish -> " + packetId);
        System.out.println(msg);
    }
}
