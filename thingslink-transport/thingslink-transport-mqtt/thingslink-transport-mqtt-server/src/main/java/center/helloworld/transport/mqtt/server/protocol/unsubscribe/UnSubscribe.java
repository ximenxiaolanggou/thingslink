package center.helloworld.transport.mqtt.server.protocol.unsubscribe;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttUnsubscribeMessage;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note
 */

public interface UnSubscribe {

    default void process(Channel channel, MqttUnsubscribeMessage message) {
    }
}
