package center.helloworld.transport.mqtt.server.session.dao.impl;

import center.helloworld.transport.mqtt.server.session.entity.Session;
import center.helloworld.transport.mqtt.server.session.dao.ISessionDao;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhishun.cai
 * @date 2024/3/19
 * @note 内存 会话存储策略
 */
@Component
@ConditionalOnProperty(value = "thingslink.transport.mqtt.storageMode", havingValue = "memory", matchIfMissing = true)
public class MemorySessionDao /*implements ISessionDao*/ {

    /**
     * 会话存储容器
     */
    private Map<String, Session> sessionMap = new ConcurrentHashMap();

    /**
     * 存储会话
     * @param uSessionId
     * @return
     */
    public void storeSession(String uSessionId, Session session) {
        session.setSessionId(uSessionId);
        sessionMap.put(uSessionId, session);
    }

    /**
     * 根据客户端ID获取会话
     * @param clientId
     * @return
     */
    public Session sessionByClientId(String clientId) {
        for (String sessionId : sessionMap.keySet()) {
            Session session = sessionMap.get(sessionId);
            if(clientId.equals(session.getClientId())) {
                return session;
            }
        }
        return null;
    }
}
