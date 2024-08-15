package center.helloworld.transport.mqtt.server.message.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhishun.cai
 * @create 2023/8/2
 * @note MQTT 消息
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    private static final long serialVersionUID = -8112511377194421600L;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 主题
     */
    private String topic;

    /**
     * Qos级别
     */
    private int mqttQoS;

    /**
     * 消息ID
     */
    private int messageId;

    /**
     * 消息体
     */
    private byte[] messageBytes;

    /**
     * 是否为dup消息
     */
    private boolean isDup;

    /**
     * 是否为保留消息
     */
    private boolean isRetain;

    /**
     * 是否有will消息
     */
    private boolean hasWillMessage = false;

    /**
     * 保留消息长度
     */
    private int remainingLength;

    public Message(String clientId, String topic, int mqttQoS, int messageId, byte[] messageBytes) {
        this.clientId = clientId;
        this.topic = topic;
        this.mqttQoS = mqttQoS;
        this.messageId = messageId;
        this.messageBytes = messageBytes;
    }
}
