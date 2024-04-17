package center.helloworld.transport.mqtt.server.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zhishun.cai
 * @create 2024/4/15
 * @note mqtt 控制报文类型标志位置
 */

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MqttControlPacketTypeFlag {

    /**
     * 是否为dup消息
     */
    private int dup = 0;

    /**
     * QOS级别
     */
    private int qos = 0 ;

    /**
     * 是否为保留消息
     */
    private int retain = 0;
}
