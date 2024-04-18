package center.helloworld.transport.mqtt.server.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhishun.cai
 * @create 2024/4/15
 * @note mqtt 消息
 */

@Data
@AllArgsConstructor
public class MqttMessage implements Serializable {

    /**
     * 固定头
     */
    private final MqttFixedHeader mqttFixedHeader;

    /**
     * 可变头
     */
    private final Object variableHeader;

    /**
     * 载体
     */
    private final Object payload;

    /**
     * 编码结果
     */
    private final DecoderResult decoderResult;
}
