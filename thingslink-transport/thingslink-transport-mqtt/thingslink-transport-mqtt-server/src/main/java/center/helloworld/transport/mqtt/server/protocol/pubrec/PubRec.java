package center.helloworld.transport.mqtt.server.protocol.pubrec;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note PubRec 消息
 */
public interface PubRec {

    /**
     * pubrec事件处理方法
     * @param channel
     * @param variableHeader
     */
    public void processPubRec(Channel channel, MqttMessageIdVariableHeader variableHeader);
}
