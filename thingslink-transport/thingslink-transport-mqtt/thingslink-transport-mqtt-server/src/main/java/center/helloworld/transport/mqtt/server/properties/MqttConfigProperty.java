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
     * 存储策略 # memory(默认) | redis | mysql
     */
    private String storageMode = "redis";

    /**
     * broker启动端口
     */
    private Integer brokerPort = 1883;

}
