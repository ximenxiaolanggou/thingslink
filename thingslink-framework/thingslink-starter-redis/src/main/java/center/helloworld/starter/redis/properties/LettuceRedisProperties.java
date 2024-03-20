package center.helloworld.starter.redis.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhishun.cai
 * @date 2024/3/19
 * @note redis配置类
 */

@ConfigurationProperties(prefix = "thingslink.starter.redis")
public class LettuceRedisProperties {
    /**
     * 是否开启Lettuce Redis
     */
    private Boolean enable = true;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
