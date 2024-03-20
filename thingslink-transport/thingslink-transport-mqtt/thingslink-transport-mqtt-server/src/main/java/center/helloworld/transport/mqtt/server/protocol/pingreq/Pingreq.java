package center.helloworld.transport.mqtt.server.protocol.pingreq;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note Ping处理
 */
public interface Pingreq {

    default void pingProcess(Channel channel, MqttMessage msg) {

    }
}
