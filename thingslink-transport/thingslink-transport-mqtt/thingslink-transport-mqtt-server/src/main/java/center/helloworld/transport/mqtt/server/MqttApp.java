package center.helloworld.transport.mqtt.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zhishun.cai
 * @date 2024/3/19
 * @note
 */

@SpringBootApplication
public class MqttApp {

    public static void main(String[] args) {
        SpringApplication.run(MqttApp.class, args);
    }
}
