package center.helloworld.transport.mqtt.server.auth.impl;

import center.helloworld.server.device.api.model.device.Device;
import center.helloworld.server.device.api.service.device.DeviceService;
import center.helloworld.transport.mqtt.server.auth.IAuthStrategy;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

/**
 * @author zhishun.cai
 * @create 2024/4/12
 * @note device 设备认证方式
 */
@Component
@ConditionalOnProperty(value = "thingslink.transport.mqtt.clientAuthMode", havingValue = "device", matchIfMissing = true)
public class DeviceAuth implements IAuthStrategy {

    @Autowired
    private DeviceService deviceService;

    /**
     * 认证
     * @param msg
     * @return
     */
    @Override
    public MqttConnectReturnCode auth(MqttConnectMessage msg) {
        Device device = null;
        String clientId = msg.payload().clientIdentifier();
        String username = msg.payload().userName();
        String pwd = msg.payload().passwordInBytes() == null ? null : new String(msg.payload().passwordInBytes());

        try {
            device = deviceService.deviceByClientId(clientId);
        }catch (Exception e) {
            // 服务不可达
            return MqttConnectReturnCode.CONNECTION_REFUSED_SERVER_UNAVAILABLE_5;
        }

        if(device == null) {
            // 无效客户端
            return MqttConnectReturnCode.CONNECTION_REFUSED_CLIENT_IDENTIFIER_NOT_VALID;
        }

        // 用户名为空 || 密码为空 || 用户名不匹配 || 密码不匹配
        if(username == null
                || pwd == null
                || !device.getDeviceKey().equals(username)
                || !device.getDeviceSecret().equals(pwd)) {
            return MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USERNAME_OR_PASSWORD;
        }

        return MqttConnectReturnCode.CONNECTION_ACCEPTED;
    }
}
