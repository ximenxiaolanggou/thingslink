package center.helloworld.transport.mqtt.server.mqttextendmsg;

import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;

/**
 * PINGREQ请求消息
 * @author zhishun.cai
 * @date 2024/11/20
 */
public class MqttPingReqMessage extends MqttMessage {
    public MqttPingReqMessage(MqttFixedHeader mqttFixedHeader) {
        super(mqttFixedHeader);
    }

    public MqttPingReqMessage(MqttFixedHeader mqttFixedHeader, Object variableHeader) {
        super(mqttFixedHeader, variableHeader);
    }

}
