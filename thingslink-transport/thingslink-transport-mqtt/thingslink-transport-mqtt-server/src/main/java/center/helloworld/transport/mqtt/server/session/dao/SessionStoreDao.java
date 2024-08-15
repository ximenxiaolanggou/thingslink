package center.helloworld.transport.mqtt.server.session.dao;

import center.helloworld.transport.mqtt.server.session.SessionStore;
import center.helloworld.transport.mqtt.server.session.entity.Session;
import center.helloworld.transport.mqtt.server.storage.IStorageMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author zhishun.cai
 * @date 2024/3/19
 * @note Redis 会话存储策略
 */
@Component
@ConditionalOnProperty(value = "thingslink.transport.mqtt.storageMode", havingValue = "redis", matchIfMissing = false)
public class SessionStoreDao extends SessionStore {

    /**
     * redis 存储key前缀
     */
    String SESSION_STORE_PREFIX = "transport_mqtt_session_";

    @Autowired
    public SessionStoreDao(IStorageMode<Session> storageMode) {
        super(storageMode);
    }

    /**
     * 存储会话 会话ID为客户端ID
     * @param uSessionId | clientId
     * @param session
     */
    @Override
    public void storeSession(String uSessionId, Session session) {
        storageMode.put(SESSION_STORE_PREFIX + uSessionId, session, 0L);
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
        storageMode.put(SESSION_STORE_PREFIX + uSessionId, session, expire);
    }

    /**
     * 根据
     * @param sessionId
     * @return
     */
    @Override
    public Session sessionBySessionId(String sessionId) {
        return (Session) storageMode.get(SESSION_STORE_PREFIX + sessionId);
    }

    /**
     * 根据客户端ID查询会话
     * @param clientId
     * @return
     */
    @Override
    public Session sessionByClientId(String clientId) {
        Set<String> keys = storageMode.allKeys(SESSION_STORE_PREFIX + "*");
        for (String key : keys) {
            Session session =  (Session) storageMode.get(key);
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
        storageMode.remove(SESSION_STORE_PREFIX + sessionId);
    }

    /**
     * 获取所有clean session为true的会话
     * @return
     */
    @Override
    public List<Session> findAllCleanSession() {
        List<Session> cleanSessions = new ArrayList();
        Set<String> keys = storageMode.allKeys(SESSION_STORE_PREFIX + "*");
        for (String key : keys) {
            Session session =  (Session) storageMode.get(key);
           if(session.isCleanSession()) {
               cleanSessions.add(session);
           }
        }
        return cleanSessions;
    }
}
