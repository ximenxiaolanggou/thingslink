package center.helloworld.transport.mqtt.server.properties;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @date 2024/3/19
 * @note
 */

@Data
@Component
@ConfigurationProperties(prefix = "thingslink.transport.mqtt")
public class MqttConfigProperty {

    /**
     * 会话存储策略 # memory(默认) | redis | mysql
     */
    private String sessionStorageMode = "memory";

}
