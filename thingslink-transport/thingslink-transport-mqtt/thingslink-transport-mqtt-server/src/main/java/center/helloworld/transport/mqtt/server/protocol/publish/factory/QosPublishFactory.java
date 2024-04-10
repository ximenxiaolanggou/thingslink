package center.helloworld.transport.mqtt.server.protocol.publish.factory;

import center.helloworld.transport.mqtt.server.protocol.publish.Publish;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.mqtt.MqttQoS.*;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note Qos 发布工厂
 */

@Component
public class QosPublishFactory {

    @Autowired
    private Map<String, Publish> qosPublishMap;

    public static Map<MqttQoS, String> strategyMap = new HashMap(3);

    static  {
        strategyMap.put(AT_MOST_ONCE, "qos0Publish");
        strategyMap.put(AT_LEAST_ONCE, "qos1Publish");
        strategyMap.put(EXACTLY_ONCE, "qos2Publish");
    }

    /**
     * 获取各个Qos级别publish处理器
     * @param qosLevel
     * @return
     */
    public Publish getQosLevelPublishHandler(MqttQoS qosLevel) {
        return qosPublishMap.get(strategyMap.get(qosLevel));
    }
}
