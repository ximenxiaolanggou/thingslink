package center.helloworld.transport.mqtt.server.protocol.subscribe;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note
 */
public interface Subscribe {

    /**
     * 处理事件
     * @param channel
     * @param message
     */
    default void process(Channel channel, MqttSubscribeMessage message) {
    }
}
