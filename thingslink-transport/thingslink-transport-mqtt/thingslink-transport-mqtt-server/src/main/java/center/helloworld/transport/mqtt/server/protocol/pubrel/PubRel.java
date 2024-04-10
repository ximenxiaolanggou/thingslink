package center.helloworld.transport.mqtt.server.protocol.pubrel;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note PubRel 消息处理
 */
public interface PubRel {

    default void process(Channel channel, MqttMessageIdVariableHeader message) {

    }
}
