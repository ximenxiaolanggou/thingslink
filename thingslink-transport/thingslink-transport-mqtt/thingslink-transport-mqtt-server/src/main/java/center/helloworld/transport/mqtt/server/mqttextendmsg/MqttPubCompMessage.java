package center.helloworld.transport.mqtt.server.mqttextendmsg;

import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;

/**
 * PUBCOMP消息
 * @author zhishun.cai
 * @date 2024/11/20
 */
public class MqttPubCompMessage extends MqttMessage {
    public MqttPubCompMessage(MqttFixedHeader mqttFixedHeader) {
        super(mqttFixedHeader);
    }

    @Override
    public MqttMessageIdVariableHeader variableHeader() {
        return (MqttMessageIdVariableHeader)super.variableHeader();
    }

    public MqttPubCompMessage(MqttFixedHeader mqttFixedHeader, Object variableHeader) {
        super(mqttFixedHeader, variableHeader);
    }
}
