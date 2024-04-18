package center.helloworld.transport.mqtt.server.codec.fixedheader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhishun.cai
 * @create 2024/4/15
 * @note Mqtt 固定头
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttFixedHeader implements Serializable {

    /**
     * 控制报文的类型
     */
    private MqttControlPacketType controlPacketType;

    /**
     * 控制报文类型的标志位
     */
    private MqttControlPacketTypeFlag controlPacketTypeFlag;

    /**
     * 剩余长度（可变头和载体）
     */
    private int remainingLength;
}
