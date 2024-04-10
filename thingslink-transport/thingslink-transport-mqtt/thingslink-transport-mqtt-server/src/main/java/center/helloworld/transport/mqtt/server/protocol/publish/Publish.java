package center.helloworld.transport.mqtt.server.protocol.publish;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note
 */
public interface Publish {

    default void process(Channel channel, MqttPublishMessage msg) {

    }
}
