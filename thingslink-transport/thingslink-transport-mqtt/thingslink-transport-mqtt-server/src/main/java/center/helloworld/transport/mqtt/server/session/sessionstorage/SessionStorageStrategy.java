package center.helloworld.transport.mqtt.server.session.sessionstorage;

import center.helloworld.transport.mqtt.server.session.entity.Session;

/**
 * @author zhishun.cai
 * @date 2024/3/19
 * @note 会话存储策略接口
 */
public interface SessionStorageStrategy {

    /**
     * 存储会话
     * @param uSessionId
     * @return
     */
    void storeSession(String uSessionId, Session session);
}
