package center.helloworld.transport.mqtt.server.auth.impl;

import center.helloworld.transport.mqtt.server.auth.IAuthStrategy;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @create 2024/4/12
 * @note http 认证方式
 */
@Component
@ConditionalOnProperty(value = "thingslink.transport.mqtt.clientAuthMode", havingValue = "http", matchIfMissing = false)
public class HttpAuth implements IAuthStrategy {


    /**
     * 认证
     * @param credentials
     * @return
     */
    @Override
    public MqttConnectReturnCode auth(Object credentials) {
        return null;
    }
}
