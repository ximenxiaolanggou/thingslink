package center.helloworld.transport.mqtt.server.protocol.puback.impl;

import center.helloworld.transport.mqtt.server.protocol.puback.PubAck;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note
 */

@Component
public class DefaultPubAck implements PubAck {

    /**
     * pub ack 消息处理
     * @param channel
     * @param mqttMessageIdVariableHeader
     */
    public void process(Channel channel, MqttMessageIdVariableHeader mqttMessageIdVariableHeader) {

        int messageId = mqttMessageIdVariableHeader.messageId();
        // TODO 删除消息
    }
}
