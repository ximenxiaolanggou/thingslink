package center.helloworld.transport.mqtt.server.protocol.subscribe;

import cn.hutool.core.util.HexUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note
 */
public interface Subscribe {

    String topicReg = "^(\\/[a-zA-Z0-9_]+)(((\\/[a-zA-Z0-9_]+)*(\\/\\#)?)|((\\/([a-zA-Z0-9_]+|\\+))*))";

    /**
     * 发生错误处理
     * @param channel
     * @param message
     */
    default void onErr(Channel channel, MqttSubscribeMessage message, int reasonCode) {
        MqttSubAckMessage subAckMessage = (MqttSubAckMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(message.variableHeader().messageId()),
                new MqttSubAckPayload(reasonCode));
        channel.writeAndFlush(subAckMessage);
    }

    /**
     * 处理事件
     * @param channel
     * @param message
     */
    default void process(Channel channel, MqttSubscribeMessage message) {
        if(!validTopic(channel, message) || !aclTopic(channel, message)) {
            onErr(channel, message, 0xA1);
            return;
        }

        if(!aclTopic(channel, message)) {
            onErr(channel, message, 0x87);
            return;
        }
        subAck(channel, message);
        sendRetainMessage(channel, message);
    }


    /**
     * 校验码tpic名称合法性
     * @param channel
     * @return
     */
    boolean validTopic(Channel channel, MqttSubscribeMessage message);

    /**
     * ACL权限校验
     * @param channel
     * @param message
     * @return
     */
    boolean aclTopic(Channel channel, MqttSubscribeMessage message);

    /**
     * 订阅 ack
     * @param channel
     * @param message
     */
    void subAck(Channel channel, MqttSubscribeMessage message);

    /**
     * 发送保留消息
     * @param channel
     * @param message
     */
    void sendRetainMessage(Channel channel, MqttSubscribeMessage message);

}
