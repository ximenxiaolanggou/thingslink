package center.helloworld.transport.mqtt.server.protocol.puback;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note PubAck 消息
 */

public interface PubAck {

    /**
     * pub ack 消息处理
     * @param channel
     * @param mqttMessageIdVariableHeader
     */
    default void process(Channel channel, MqttMessageIdVariableHeader mqttMessageIdVariableHeader) {

    }
}
