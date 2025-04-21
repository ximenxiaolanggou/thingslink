package center.helloworld.transport.mqtt.server.mqttextendmsg;

import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;

/**
 * 关闭连接消息
 * @author zhishun.cai
 * @date 2024/11/20
 */
public class MqttDisconnectMessage extends MqttMessage {

    public MqttDisconnectMessage(MqttFixedHeader mqttFixedHeader) {
        super(mqttFixedHeader);
    }


    public MqttDisconnectMessage(MqttFixedHeader mqttFixedHeader, Object variableHeader) {
        super(mqttFixedHeader, variableHeader);
    }
}
