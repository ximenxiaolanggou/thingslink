package center.helloworld.starter.openfeign.config;

import center.helloworld.starter.openfeign.interceptors.RequestHeaderForwordInterceptor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhishun.cai
 * @create 2024/4/12
 * @note
 */

@Configuration
@EnableFeignClients(value = "center.helloworld.**.api")
public class OpenFeignAutoConfiguration {

    @Bean
    public RequestHeaderForwordInterceptor requestHeaderForwordInterceptor() {
        return new RequestHeaderForwordInterceptor();
    }
}
