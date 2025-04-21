package center.helloworld.transport.mqtt.server.session.dao.impl;

import center.helloworld.starter.redis.service.RedisService;
import center.helloworld.transport.mqtt.server.session.dao.ISessionDao;
import center.helloworld.transport.mqtt.server.session.entity.Session;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author zhishun.cai
 * @date 2024/3/19
 * @note Redis 会话存储策略
 */
@Component
@ConditionalOnProperty(value = "thingslink.transport.mqtt.storageMode", havingValue = "redis", matchIfMissing = false)
public class RedisSessionDao implements ISessionDao {

    /**
     * redis 存储key前缀
     */
    String STORE_PREFIX = "TL_SESSION_";

    @Autowired
    private RedisService redisService;


    /**
     * 存储会话 会话ID为客户端ID
     * @param uSessionId | clientId
     * @param session
     */
    @Override
    public void storeSession(String uSessionId, Session session) {
        redisService.set(STORE_PREFIX + uSessionId, session);
    }

    @Override
    public Session sessionByClientId(String clientId) {
        return (Session) redisService.get(STORE_PREFIX + clientId);
    }

    /**
     * 根据会话ID删除会话
     * @param sessionId
     */
    @Override
    public void removeSession(String sessionId) {
        redisService.del(STORE_PREFIX + sessionId);
    }
}
