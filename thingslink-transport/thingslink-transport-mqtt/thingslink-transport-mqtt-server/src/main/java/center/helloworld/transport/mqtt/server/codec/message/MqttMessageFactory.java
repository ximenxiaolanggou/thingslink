package center.helloworld.transport.mqtt.server.codec.message;

import center.helloworld.transport.mqtt.server.codec.DecoderResult;

/**
 * @author zhishun.cai
 * @create 2024/4/18
 * @note MQTT 消息创建工厂
 */
public class MqttMessageFactory {

    /**
     * 创建无效消息
     * @param cause
     * @return
     */
    public static MqttMessage newInvalidMessage(Throwable cause) {
        return new MqttMessage(null, null, null, DecoderResult.FAILURE(cause));
    }
}
