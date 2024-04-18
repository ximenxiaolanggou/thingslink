package center.helloworld.transport.mqtt.server.message;

import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhishun.cai
 * @create 2024/4/16
 * @note MQTT 连接属性
 */

@Data
public class MqttConnectProperties implements Serializable {

    public static final Map<Integer, MqttConnectPropertyType> mqttConnectPropertiesTypeMap = new HashMap();

    static {
        mqttConnectPropertiesTypeMap.put(17, MqttConnectPropertyType.SESSION_EXPIRY_INTERVAL);
        mqttConnectPropertiesTypeMap.put(33, MqttConnectPropertyType.RECEIVE_MAXIMUM);
        mqttConnectPropertiesTypeMap.put(39, MqttConnectPropertyType.MAXIMUM_PACKET_SIZE);
        mqttConnectPropertiesTypeMap.put(34, MqttConnectPropertyType.TOPIC_ALIAS_MAXIMUM);
        mqttConnectPropertiesTypeMap.put(25, MqttConnectPropertyType.REQUEST_RESPONSE_INFORMATION);
        mqttConnectPropertiesTypeMap.put(23, MqttConnectPropertyType.REQUEST_PROBLEM_INFORMATION);
        mqttConnectPropertiesTypeMap.put(38, MqttConnectPropertyType.USER_PROPERTY);
        mqttConnectPropertiesTypeMap.put(21, MqttConnectPropertyType.AUTHENTICATION_METHOD);
        mqttConnectPropertiesTypeMap.put(22, MqttConnectPropertyType.AUTHENTICATION_DATA);
    }


    /**
     * 连接属性类型
     */
    public enum MqttConnectPropertyType {
        // 会话过期间隔 17
        SESSION_EXPIRY_INTERVAL(0x11),
        // 接收最大值 33
        RECEIVE_MAXIMUM(0x21),
        // 最大报文长度 39
        MAXIMUM_PACKET_SIZE(0x27),
        // 主题别名最大值 34
        TOPIC_ALIAS_MAXIMUM(0x22),
        // 请求响应信息 25
        REQUEST_RESPONSE_INFORMATION(0x19),
        // 请求问题信息 23
        REQUEST_PROBLEM_INFORMATION(0x17),
        // 用户属性 38
        USER_PROPERTY(0x26),
        // 认证方法 21
        AUTHENTICATION_METHOD(0x15),
        // 认证数据 22
        AUTHENTICATION_DATA(0x16)
        ;

        private final int value;

        MqttConnectPropertyType(int value) {
            this.value = value;
        }
    }

    /**
     * 连接属性长度
     */
    private int connectPropertiesLen;

    /**
     * 会话过期间隔标识符
     */
    private boolean sessionExpiryIntervalFlag = false;

    /**
     * 会话过期间隔
     */
    private int sessionExpiryInterval;

    /**
     * 接收最大值标识符
     */
    private boolean receiveMaximumFlag = false;

    /**
     * 接收最大值
     */
    private int receiveMaximum;

    /**
     * 最大报文长度标识符
     */
    private boolean maximumPacketSizeFlag = false;

    /**
     * 最大报文长度
     */
    private int maximumPacketSize;

    /**
     * 主题别名最大值标识符
     */
    private boolean topicAliasMaximumFlag = false;

    /**
     * 主题别名最大值
     */
    private int topicAliasMaximum;

    /**
     * 请求响应信息标识符
     */
    private boolean requestResponseInformationFlag = false;

    /**
     * 请求响应信息
     */
    private int requestResponseInformation;

    /**
     * 请求问题信息标识符
     */
    private boolean requestProblemInformationFlag = false;

    /**
     * 请求问题信息
     */
    private int requestProblemInformation;


    /**
     * 用户自定义属性信息标识符
     */
    private boolean customerPropertiesFlag = false;

    /**
     * 用户自定义属性信息
     */
    private String customerProperties;

    public static MqttConnectProperties mqttConnectPropertiesresolver(ByteBuf connectPropertiesData) {
        MqttConnectProperties mqttConnectProperties = new MqttConnectProperties();
        mqttConnectProperties.setConnectPropertiesLen(connectPropertiesData.capacity());

        while (connectPropertiesData.readableBytes() > 0) {
            // 属性标识符
            byte propertyIdentifier = connectPropertiesData.readByte();
            MqttConnectPropertyType mqttConnectPropertyType = mqttConnectPropertiesTypeMap.get(Integer.valueOf(propertyIdentifier));
            switch (mqttConnectPropertyType) {
                case SESSION_EXPIRY_INTERVAL -> {
                    mqttConnectProperties.setSessionExpiryIntervalFlag(true);
                    mqttConnectProperties.setSessionExpiryInterval(connectPropertiesData.readInt());
                }
                case RECEIVE_MAXIMUM -> {
                    mqttConnectProperties.setReceiveMaximumFlag(true);
                    mqttConnectProperties.setReceiveMaximum(connectPropertiesData.readChar());
                }

                case MAXIMUM_PACKET_SIZE -> {
                    mqttConnectProperties.setMaximumPacketSizeFlag(true);
                    mqttConnectProperties.setMaximumPacketSize(connectPropertiesData.readInt());
                }

                case TOPIC_ALIAS_MAXIMUM -> {
                    mqttConnectProperties.setTopicAliasMaximumFlag(true);
                    mqttConnectProperties.setTopicAliasMaximum(connectPropertiesData.readChar());
                }

                case REQUEST_RESPONSE_INFORMATION -> {
                    mqttConnectProperties.setRequestResponseInformationFlag(true);
                    mqttConnectProperties.setRequestResponseInformation(connectPropertiesData.readByte());
                }

                case REQUEST_PROBLEM_INFORMATION -> {
                    mqttConnectProperties.setRequestProblemInformationFlag(true);
                    mqttConnectProperties.setRequestProblemInformation(connectPropertiesData.readByte());
                }

                case USER_PROPERTY -> {
                    mqttConnectProperties.setCustomerPropertiesFlag(true);
                    mqttConnectProperties.setCustomerProperties(connectPropertiesData.readBytes(connectPropertiesData.readableBytes()).toString(StandardCharsets.UTF_8));
                }
            }
        }
        return mqttConnectProperties;
    }

}
