package center.helloworld.transport.mqtt.server.protocol.pubrec.impl;

import center.helloworld.transport.mqtt.server.dup.dao.DupPubRelMessageDao;
import center.helloworld.transport.mqtt.server.message.pojo.Message;
import center.helloworld.transport.mqtt.server.protocol.pubrec.PubRec;
import center.helloworld.transport.mqtt.server.protocol.pubrel.PubRel;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader.from;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note PubRec消息
 */

@Slf4j
@Component
public class DefaultPubRec implements PubRec {

    @Resource
    private DupPubRelMessageDao dupPubRelMessageStore;

    /**
     * pubrec事件处理方法
     * @param channel
     * @param variableHeader
     */
    @Override
    public void processPubRec(Channel channel, MqttMessageIdVariableHeader variableHeader) {
        // 1、存储dup消息
        int messageId = variableHeader.messageId();
        Message message = new Message();
        message.setMessageId(messageId);
        dupPubRelMessageStore.put(String.valueOf(messageId), message);
        // 2、发布pubRel释放报文
        MqttFixedHeader recFixedHeader = new MqttFixedHeader(MqttMessageType.PUBREL, false,
                MqttQoS.EXACTLY_ONCE, false, 2);
        MqttPubAckMessage pubRelMessage = new MqttPubAckMessage(recFixedHeader, from(messageId));
        channel.writeAndFlush(pubRelMessage);
    }
}
