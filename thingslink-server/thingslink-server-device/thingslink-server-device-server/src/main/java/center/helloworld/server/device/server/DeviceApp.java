package center.helloworld.server.device.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * @author zhishun.cai
 * @create 2024/4/12
 * @note
 */

@EnableDiscoveryClient
@SpringBootApplication
public class DeviceApp {

    @Value("${server.port}")
    private String port;

    public static void main(String[] args) {
        SpringApplication.run(DeviceApp.class, args);
    }

}
