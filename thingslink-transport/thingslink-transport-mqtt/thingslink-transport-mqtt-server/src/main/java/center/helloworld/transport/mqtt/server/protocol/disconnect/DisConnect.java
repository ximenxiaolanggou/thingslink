package center.helloworld.transport.mqtt.server.protocol.disconnect;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note 关闭连接处理
 */
public interface DisConnect {

    default void process(Channel channel, MqttMessage msg) {
    }
}
