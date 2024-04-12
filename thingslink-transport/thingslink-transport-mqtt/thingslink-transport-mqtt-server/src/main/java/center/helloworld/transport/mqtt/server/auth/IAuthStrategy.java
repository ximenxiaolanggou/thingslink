package center.helloworld.transport.mqtt.server.auth;

import io.netty.handler.codec.mqtt.MqttConnectReturnCode;

/**
 * @author zhishun.cai
 * @create 2024/4/12
 * @note 会话认证接口
 */
public interface IAuthStrategy {

    /**
     * 认证
     * @param credentials
     * @return
     */
    MqttConnectReturnCode auth(Object credentials);
}
