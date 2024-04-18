package center.helloworld.transport.mqtt.server.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhishun.cai
 * @create 2024/4/17
 * @note MQTT will 消息属性
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttWillProperties {

    /**
     * 属性长度
     */
    private Integer propertyLength;

    /**
     * 遗嘱延时间隔
     */
    private Integer willDelayInterval;

    /**
     * 载荷格式指示
     */
    private Integer payloadFormatIndicator;

    /**
     * 消息过期间隔
     */
    private Integer messageExpireInterval;

    /**
     * 内容类型
     */
    private Integer contentType;

    /**
     * 响应主题
     */
    private String responesType;


    /**
     * 对比数据
     */
    private String correlationData;

    /**
     * 用户自定义属性信息
     */
    private String customerProperties;

}
