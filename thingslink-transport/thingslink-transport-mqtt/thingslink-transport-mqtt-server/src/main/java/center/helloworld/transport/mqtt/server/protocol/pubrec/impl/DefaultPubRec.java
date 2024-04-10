package center.helloworld.transport.mqtt.server.protocol.pubrec.impl;

import center.helloworld.transport.mqtt.server.protocol.pubrec.PubRec;
import center.helloworld.transport.mqtt.server.protocol.pubrel.PubRel;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import org.springframework.stereotype.Component;

import static io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader.from;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note PubRec消息
 */

@Component
public class DefaultPubRec implements PubRec {


    @Override
    public void processPubRec(Channel channel, MqttMessageIdVariableHeader variableHeader) {
        int messageId = variableHeader.messageId();

        // 发布pubRel释放报文
        MqttFixedHeader recFixedHeader = new MqttFixedHeader(MqttMessageType.PUBREL, false,
                MqttQoS.EXACTLY_ONCE, false, 2);
        MqttPubAckMessage pubRelMessage = new MqttPubAckMessage(recFixedHeader, from(messageId));
        channel.writeAndFlush(pubRelMessage);
    }
}
