package center.helloworld.transport.mqtt.server.mqttextendmsg;

import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;

/**
 * PINGRESP消息
 * @author zhishun.cai
 * @date 2024/11/20
 */
public class MqttPingRespMessage extends MqttMessage {
    public MqttPingRespMessage(MqttFixedHeader mqttFixedHeader) {
        super(mqttFixedHeader);
    }

    public MqttPingRespMessage(MqttFixedHeader mqttFixedHeader, Object variableHeader) {
        super(mqttFixedHeader, variableHeader);
    }

}
