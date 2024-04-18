package center.helloworld.transport.mqtt.server.message;

/**
 * @author zhishun.cai
 * @create 2024/4/15
 * @note MqttControlPacketType 工厂
 */
public class MqttControlPacketTypeFactory {


    public static MqttControlPacketType getPackageType(int value) {
        return switch (value) {
            case 1 -> MqttControlPacketType.CONNECT;
            case 2 -> MqttControlPacketType.CONNACK;
            case 3 -> MqttControlPacketType.PUBLISH;
            case 4 -> MqttControlPacketType.PUBACK;
            case 5 -> MqttControlPacketType.PUBREC;
            case 6 -> MqttControlPacketType.PUBREL;
            case 7 -> MqttControlPacketType.PUBCOMP;
            case 8 -> MqttControlPacketType.SUBSCRIBE;
            case 9 -> MqttControlPacketType.SUBACK;
            case 10 -> MqttControlPacketType.UNSUBSCRIBE;
            case 11 -> MqttControlPacketType.UNSUBACK;
            case 12 -> MqttControlPacketType.PINGREQ;
            case 13 -> MqttControlPacketType.PINGRESP;
            case 14 -> MqttControlPacketType.DISCONNECT;
            case 15 -> MqttControlPacketType.AUTH;
            default -> throw new RuntimeException("Code 错误"); // TODO 自定义异常
        };
    }
}
