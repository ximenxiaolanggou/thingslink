package center.helloworld.transport.mqtt.server.protocol;

import center.helloworld.transport.mqtt.server.protocol.connect.Connect;
import center.helloworld.transport.mqtt.server.protocol.disconnect.DisConnect;
import center.helloworld.transport.mqtt.server.protocol.pingreq.Pingreq;
import center.helloworld.transport.mqtt.server.protocol.publish.Publish;
import center.helloworld.transport.mqtt.server.protocol.publish.factory.QosPublishFactory;
import center.helloworld.transport.mqtt.server.protocol.pubrel.PubRel;
import center.helloworld.transport.mqtt.server.protocol.subscribe.Subscribe;
import center.helloworld.transport.mqtt.server.protocol.unsubscribe.UnSubscribe;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @create 2024/3/18
 * @note 协议处理
 */
@Component
public class ProtocolProcesser {

    @Autowired
    private Connect connect;

    @Autowired
    private DisConnect disConnect;

    @Autowired
    private Pingreq ping;

    @Autowired
    private PubRel pubRel;

    @Autowired
    private Subscribe subscribe;

    @Autowired
    private UnSubscribe unSubscribe;

    @Autowired
    private QosPublishFactory qosPublishFactory;

    /**
     * 连接处理
     * @return
     */
    public Connect connectProcess() {
        return connect;
    }

    /**
     * 关闭连接
     * @return
     */
    public DisConnect disConnectProcess() {
        return disConnect;
    }

    /**
     * ping处理
     * @return
     */
    public Pingreq pingProcess() {
        return ping;
    }

    /**
     * pubRel处理
     * @return
     */
    public PubRel pubRelProcess() {
        return pubRel;
    }

    /**
     * publish
     * @return
     */
    public Publish publishProcess(MqttPublishMessage message) {
        return qosPublishFactory.getQosLevelPublishHandler(message.fixedHeader().qosLevel());
    }

    /**
     * subscribe
     * @return
     */
    public Subscribe subscribeProcess() {
        return subscribe;
    }

    /**
     * subscribe
     * @return
     */
    public UnSubscribe unsubscribeProcess() {
        return unSubscribe;
    }


}
