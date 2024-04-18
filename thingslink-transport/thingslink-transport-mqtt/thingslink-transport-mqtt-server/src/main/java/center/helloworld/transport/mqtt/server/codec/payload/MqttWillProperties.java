package center.helloworld.transport.mqtt.server.codec.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhishun.cai
 * @create 2024/4/17
 * @note MQTT will 消息属性
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttWillProperties {

    public static Map<Integer, MqttWillPropertyType> willPropertyTypeMap = new HashMap();

    static {
        willPropertyTypeMap.put(0x18, MqttWillPropertyType.WILL_DELAY_INTERVAL);
        willPropertyTypeMap.put(0x01, MqttWillPropertyType.PAYLOAD_FORMAT_INDICATOR);
        willPropertyTypeMap.put(0x02, MqttWillPropertyType.MESSAGE_EXPIRY_INTERVAL);
        willPropertyTypeMap.put(0x03, MqttWillPropertyType.CONTENT_TYPE);
        willPropertyTypeMap.put(0x08, MqttWillPropertyType.RESPONSE_TOPIC);
        willPropertyTypeMap.put(0x09, MqttWillPropertyType.CORRELATION_DATA);
        willPropertyTypeMap.put(0x26, MqttWillPropertyType.USER_PROPERTY);
    }

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
    private String contentType;

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
    private String userProperty;

    /**
     * mqtt will 属性类型
     */
    public enum MqttWillPropertyType {
        // 24 遗嘱延时间隔（Will Delay Interval）标识符
        WILL_DELAY_INTERVAL(0x18),
        // 1 载荷格式指示（Payload Format Indicator）标识符
        PAYLOAD_FORMAT_INDICATOR(0x01),
        // 2 消息过期间隔（Message Expiry Interval）标识符
        MESSAGE_EXPIRY_INTERVAL(0x02),
        // 3 (0x03)，内容类型（Content Type）标识符
        CONTENT_TYPE(0x03),
        // 8 (0x08)，响应主题（Response Topic）标识符
        RESPONSE_TOPIC(0x08),
        // 9 (0x09)，对比数据（Correlation Data）标识符
        CORRELATION_DATA(0x09),
        // 38 (0x26)，用户属性（User Property）标识符
        USER_PROPERTY(0x26),
        ;

        private int value;

        MqttWillPropertyType(int value) {
            this.value = value;
        }
    }

}
