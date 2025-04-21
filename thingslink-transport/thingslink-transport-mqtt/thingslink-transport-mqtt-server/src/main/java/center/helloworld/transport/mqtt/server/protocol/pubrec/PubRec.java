package center.helloworld.transport.mqtt.server.protocol.pubrec;

import center.helloworld.transport.mqtt.server.mqttextendmsg.MqttPubRecMessage;
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
     * @param mqttPubRecMessage
     */
    public void processPubRec(Channel channel, MqttPubRecMessage mqttPubRecMessage);
}
