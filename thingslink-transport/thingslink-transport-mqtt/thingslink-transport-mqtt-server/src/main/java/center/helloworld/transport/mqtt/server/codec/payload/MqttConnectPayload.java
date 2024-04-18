package center.helloworld.transport.mqtt.server.codec.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zhishun.cai
 * @create 2024/4/17
 * @note MQTT 连接信息载体
 */

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MqttConnectPayload implements MqttPayload {

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 遗嘱主题
     */
    private String willTopic;

    /**
     * will 数据
     */
    private String willPayloadData;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * will消息属性
     */
    private MqttWillProperties willProperties;
}
