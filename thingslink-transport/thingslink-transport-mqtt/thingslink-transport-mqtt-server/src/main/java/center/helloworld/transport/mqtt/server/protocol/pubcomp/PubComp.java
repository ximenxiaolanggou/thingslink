package center.helloworld.transport.mqtt.server.protocol.pubcomp;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note PubComp 消息
 */
public interface PubComp {

    /**
     * pubcomp处理事件
     * @param channel
     * @param variableHeader
     */
    void processPubComp(Channel channel, MqttMessageIdVariableHeader variableHeader);
}
