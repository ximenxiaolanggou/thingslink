package center.helloworld.transport.mqtt.server.message;

import io.netty.handler.codec.mqtt.MqttProperties;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhishun.cai
 * @create 2024/4/19
 * @note 遗言消息
 */
@Data
@NoArgsConstructor
public class MqttWillMessage implements Serializable {

    /**
     * 客户端标识符
     */
    private String clientIdentifier;

    /**
     * 遗言 Topic
     */
    private String willTopic;

    /**
     * Qos 级别
     */
    private MqttQoS willQos;

    /**
     * 遗言消息
     */
    private byte[] willMessage;

    /**
     *  遗言属性 , TODO 该属性没有提供无参构造导致后续后续反序列化成对象不成功，因为自定义will属性使用的不多所以这里暂时先不提供该功能
     */
//    private MqttProperties willProperties;

    public MqttWillMessage(String clientIdentifier, String willTopic, MqttQoS willQos, byte[] willMessage /*, MqttProperties willProperties*/) {
        this.clientIdentifier = clientIdentifier;
        this.willTopic = willTopic;
        this.willQos = willQos;
        this.willMessage = willMessage;
//        this.willProperties = willProperties;
    }
}
