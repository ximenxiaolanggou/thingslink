package center.helloworld.transport.mqtt.server.protocol.pubrel.impl;

import center.helloworld.transport.mqtt.server.dup.dao.DupPubRecMessageDao;
import center.helloworld.transport.mqtt.server.protocol.publish.dao.PublishMessageDao;
import center.helloworld.transport.mqtt.server.protocol.pubrel.PubRel;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note
 */

@Slf4j
@Component
public class DefaultPubrel implements PubRel {

    @Autowired
    private DupPubRecMessageDao dupPubRecMessageStore;

    @Autowired
    private PublishMessageDao publishMessageStore;

    /**
     * PubRel 处理方法
     * @param channel
     * @param variableHeader
     */
    @Override
    public void process(Channel channel, MqttMessageIdVariableHeader variableHeader) {
        int messageId = variableHeader.messageId();
        // 删除dup消息
        dupPubRecMessageStore.remove(String.valueOf(messageId));

        // 删除 接收到的publish消息
        publishMessageStore.remove(String.valueOf(messageId));

        // 组装并发送pubCom消息
        MqttMessage pubCompMessage = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBCOMP, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(messageId), null);
        log.debug("PUBREL - clientId: {}, messageId: {}", (String) channel.attr(AttributeKey.valueOf("clientId")).get(), messageId);
        channel.writeAndFlush(pubCompMessage);
    }
}
