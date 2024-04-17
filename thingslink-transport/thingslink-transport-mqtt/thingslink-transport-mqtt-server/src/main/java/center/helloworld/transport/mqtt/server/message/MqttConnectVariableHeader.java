package center.helloworld.transport.mqtt.server.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author zhishun.cai
 * @create 2024/4/15
 * @note Mqtt 连接可变头
 */
@Data
@ToString
public class MqttConnectVariableHeader implements MqttVariableHeader {
    /**
     * 协议名称长度
     */
    private int protocolNameLen;

    /**
     * 协议名称
     */
    private String protocolName;

    /**
     * 版本
     */
    private int version;

    /**
     * 用户名标志位
     */
    private boolean usernameFlag;

    /**
     * 密码标志位
     */
    private boolean passwordFlag;

    /**
     * 遗言保留
     */
    private boolean willRetain;

    /**
     * 遗言消息 Qos
     */
    private int willQos;

    /**
     * 遗言标志
     */
    private boolean willFlag;

    /**
     * 清除会话
     */
    private boolean cleanStart;

    /**
     * 保持会话时长
     */
    private Integer keepAliveTimeSeconds;

    /**
     * MQTT 连接属性
     */
    private MqttConnectProperties mqttConnectProperties;
}
