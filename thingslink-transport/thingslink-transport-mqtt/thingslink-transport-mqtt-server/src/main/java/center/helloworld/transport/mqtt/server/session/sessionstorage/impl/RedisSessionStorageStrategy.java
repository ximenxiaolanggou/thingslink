package center.helloworld.transport.mqtt.server.session.sessionstorage.impl;

import center.helloworld.transport.mqtt.server.session.entity.Session;
import center.helloworld.transport.mqtt.server.session.sessionstorage.SessionStorageStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @date 2024/3/19
 * @note Redis 会话存储策略
 */
@Component
@ConditionalOnProperty(value = "thingslink.transport.mqtt.sessionStorageMode", havingValue = "redis", matchIfMissing = false)
public class RedisSessionStorageStrategy /*implements SessionStorageStrategy*/ {


}
