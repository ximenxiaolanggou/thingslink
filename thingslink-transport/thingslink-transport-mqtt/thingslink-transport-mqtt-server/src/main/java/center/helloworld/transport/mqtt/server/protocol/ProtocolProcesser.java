package center.helloworld.transport.mqtt.server.protocol;

import center.helloworld.transport.mqtt.server.protocol.connect.Connect;
import center.helloworld.transport.mqtt.server.protocol.pingreq.Pingreq;
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
    private Pingreq ping;

    /**
     * 连接处理
     * @return
     */
    public Connect connectProcess() {
        return connect;
    }

    /**
     * ping处理
     * @return
     */
    public Pingreq pingProcess() {
        return ping;
    }

}
