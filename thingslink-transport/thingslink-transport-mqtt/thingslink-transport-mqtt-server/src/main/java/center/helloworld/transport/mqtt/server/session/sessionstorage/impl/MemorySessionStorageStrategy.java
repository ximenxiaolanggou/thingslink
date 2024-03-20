package center.helloworld.transport.mqtt.server.session.sessionstorage.impl;

import center.helloworld.transport.mqtt.server.session.sessionstorage.SessionStorageStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @date 2024/3/19
 * @note 内存 会话存储策略
 */
@Component
@ConditionalOnProperty(value = "thingslink.transport.mqtt.sessionStorageMode", havingValue = "memory", matchIfMissing = true)
public class MemorySessionStorageStrategy implements SessionStorageStrategy {
}
