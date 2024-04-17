package center.helloworld.transport.mqtt.server.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zhishun.cai
 * @create 2024/4/15
 * @note mqtt 消息
 */

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MqttMessage implements Serializable {

    /**
     * 固定头
     */
    private MqttFixedHeader mqttFixedHeader;

    /**
     * 可变头
     */
    private MqttVariableHeader variableHeader;

    /**
     * 载体
     */
    private MqttPayload payload;

    /**
     * 编码结果
     */
    private DecoderResult decoderResult;
}
