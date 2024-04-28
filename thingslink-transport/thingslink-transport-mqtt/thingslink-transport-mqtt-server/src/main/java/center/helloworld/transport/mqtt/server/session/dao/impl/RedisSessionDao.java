package center.helloworld.transport.mqtt.server.session.dao.impl;

import center.helloworld.starter.redis.service.RedisService;
import center.helloworld.transport.mqtt.server.session.dao.ISessionDao;
import center.helloworld.transport.mqtt.server.session.entity.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Set;

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
    String SESSION_STORE_PREFIX = "transport_mqtt_session_";


    @Autowired
    private RedisService redisService;


    /**
     * 存储会话 会话ID为客户端ID
     * @param uSessionId | clientId
     * @param session
     */
    @Override
    public void storeSession(String uSessionId, Session session) {
        redisService.set(SESSION_STORE_PREFIX + uSessionId, session);
    }

    /**
     * 存储会话 会话ID为客户端ID
     * @param uSessionId | clientId
     * @param session
     */
    @Override
    public void storeSession(String uSessionId, Session session, long expire) {
        if(expire < 0) {
            throw new RuntimeException("expire value must > 0");
        }
        redisService.set(SESSION_STORE_PREFIX + uSessionId, session, expire);
    }

    /**
     * 根据
     * @param sessionId
     * @return
     */
    @Override
    public Session sessionBySessionId(String sessionId) {
        return (Session) redisService.get(SESSION_STORE_PREFIX + sessionId);
    }

    /**
     * 根据客户端ID查询会话
     * @param clientId
     * @return
     */
    @Override
    public Session sessionByClientId(String clientId) {
        Set<String> keys = redisService.allKeys(SESSION_STORE_PREFIX + "*");
        for (String key : keys) {
            Session session =  (Session) redisService.get(key);
            if(session.getClientId().equals(clientId)) {
                return session;
            }
        }
        return null;
    }

    /**
     * 删除
     * @param sessionId
     */
    @Override
    public void removeBySessionId(String sessionId) {
        redisService.del(SESSION_STORE_PREFIX + sessionId);
    }
}
