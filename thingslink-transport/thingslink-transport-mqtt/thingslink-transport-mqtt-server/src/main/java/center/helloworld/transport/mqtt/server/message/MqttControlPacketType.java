package center.helloworld.transport.mqtt.server.message;

/**
 * @author zhishun.cai
 * @create 2024/4/15
 * @note Mqtt 控制包类型 第1个字节，二进制位7-4。
 */
public enum MqttControlPacketType {

    CONNECT(1),        //	客户端到服务端	客户端请求连接服务端
    CONNACK(2),        //	服务端到客户端	连接报文确认
    PUBLISH(3),        //	两个方向都允许	发布消息
    PUBACK(4),         //	两个方向都允许	QoS 1消息发布收到确认
    PUBREC(5),         //	两个方向都允许	发布收到（保证交付第一步）
    PUBREL(6),         //	两个方向都允许	发布释放（保证交付第二步）
    PUBCOMP(7),        //	两个方向都允许	QoS 2消息发布完成（保证交互第三步）
    SUBSCRIBE(8),      //	客户端到服务端	客户端订阅请求
    SUBACK(9),         //	服务端到客户端	订阅请求报文确认
    UNSUBSCRIBE(10),   // 客户端到服务端	客户端取消订阅请求
    UNSUBACK(11),      //	服务端到客户端	取消订阅报文确认
    PINGREQ(12),       //	客户端到服务端	心跳请求
    PINGRESP(13),      //	服务端到客户端	心跳响应
    DISCONNECT(14),    //	两个方向都允许	断开连接通知
    AUTH(15);          //	两个方向都允许	认证信息交换

    private int value;

    MqttControlPacketType(int value) {
        this.value = value;
    }
}
