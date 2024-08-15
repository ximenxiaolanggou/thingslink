package center.helloworld.transport.mqtt.server.protocol.pubcomp.impl;

import center.helloworld.transport.mqtt.server.dup.dao.DupPubRelMessageDao;
import center.helloworld.transport.mqtt.server.dup.dao.DupPublishMessageDao;
import center.helloworld.transport.mqtt.server.protocol.pubcomp.PubComp;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note PubComp
 */

@Slf4j
@Component
public class DefaultPubComp implements PubComp {
    @Resource
    private DupPubRelMessageDao dupPubRelMessageStore;

    @Resource
    private DupPublishMessageDao dupPublishMessageStore;

    /**
     * pubcomp处理事件
     * @param channel
     * @param variableHeader
     */
    @Override
    public void processPubComp(Channel channel, MqttMessageIdVariableHeader variableHeader) {
        int messageId = variableHeader.messageId();
        // 删除pubrec dup消息
        dupPubRelMessageStore.remove(String.valueOf(messageId));

        // 删除publish message dup 消息
        dupPublishMessageStore.remove(String.valueOf(messageId));
        log.debug("PUBCOMP - clientId: {}, messageId: {}", (String) channel.attr(AttributeKey.valueOf("clientId")).get(), messageId);
    }
}
