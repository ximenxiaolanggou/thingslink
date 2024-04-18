package center.helloworld.transport.mqtt.server.message;

import cn.hutool.core.math.BitStatusUtil;
import cn.hutool.core.util.ByteUtil;

/**
 * @author zhishun.cai
 * @create 2024/4/15
 * @note
 */
public class MqttControlPacketTypeFlagFactory {

    public static MqttControlPacketTypeFlag getControlPacketTypeFlag(MqttControlPacketType controlPacketType, byte controlPackage) {
        MqttControlPacketTypeFlag cptf = new MqttControlPacketTypeFlag();
        switch (controlPacketType) {
            case PUBLISH -> {
                cptf.setDup(controlPackage >> 3 & 0x01);
                cptf.setQos(controlPackage >> 1 & 0x02);
                cptf.setRetain(controlPackage & 0x01);
            }
        };
        return cptf;
    }
}
