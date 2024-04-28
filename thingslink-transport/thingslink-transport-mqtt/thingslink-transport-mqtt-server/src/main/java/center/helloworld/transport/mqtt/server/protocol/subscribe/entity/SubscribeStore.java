package center.helloworld.transport.mqtt.server.protocol.subscribe.entity;

import java.io.Serializable;

/**
 * @author zhishun.cai
 * @create 2024/4/28
 * @note
 */
public class SubscribeStore implements Serializable {

    private static final long serialVersionUID = 1276156087085594264L;

    private String clientId;

    private String topicFilter;

    private int mqttQoS;

    public SubscribeStore() {

    }

    public SubscribeStore(String clientId, String topicFilter, int mqttQoS) {
        this.clientId = clientId;
        this.topicFilter = topicFilter;
        this.mqttQoS = mqttQoS;
    }

    public String getClientId() {
        return clientId;
    }

    public SubscribeStore setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getTopicFilter() {
        return topicFilter;
    }

    public SubscribeStore setTopicFilter(String topicFilter) {
        this.topicFilter = topicFilter;
        return this;
    }

    public int getMqttQoS() {
        return mqttQoS;
    }

    public SubscribeStore setMqttQoS(int mqttQoS) {
        this.mqttQoS = mqttQoS;
        return this;
    }
}

