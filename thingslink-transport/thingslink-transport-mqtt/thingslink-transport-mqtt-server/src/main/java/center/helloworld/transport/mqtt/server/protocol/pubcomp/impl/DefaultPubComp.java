package center.helloworld.transport.mqtt.server.protocol.pubcomp.impl;

import center.helloworld.transport.mqtt.server.protocol.pubcomp.PubComp;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note PubComp
 */

@Component
public class DefaultPubComp implements PubComp {
    @Override
    public void processPubComp(Channel channel, MqttMessageIdVariableHeader variableHeader) {
        int messageId = variableHeader.messageId();
        // TODO 删除消息
    }
}
