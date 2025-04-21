package center.helloworld.transport.mqtt.server.mqttextendmsg;

import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;

/**
 * PUBREL消息
 * @author zhishun.cai
 * @date 2024/11/20
 */
public class MqttPubRelMessage extends MqttMessage {
    public MqttPubRelMessage(MqttFixedHeader mqttFixedHeader) {
        super(mqttFixedHeader);
    }

    @Override
    public MqttMessageIdVariableHeader variableHeader() {
        return (MqttMessageIdVariableHeader)super.variableHeader();
    }

    public MqttPubRelMessage(MqttFixedHeader mqttFixedHeader, Object variableHeader) {
        super(mqttFixedHeader, variableHeader);
    }
}
