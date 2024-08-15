package center.helloworld.transport.mqtt.server.session;

import center.helloworld.transport.mqtt.server.session.entity.Session;
import center.helloworld.transport.mqtt.server.storage.IStorageMode;
import cn.hutool.core.lang.UUID;

import java.util.List;

/**
 * @author zhishun.cai
 * @date 2024/3/19
 * @note 会话存储策略接口
 */
public abstract class SessionStore {

    public IStorageMode<Session> storageMode;

    public SessionStore(IStorageMode<Session> storageMode) {
        this.storageMode = storageMode;
    }

    /**
     * 存储会话
     * @param uSessionId
     * @return
     */
    public abstract void storeSession(String uSessionId, Session session, long expire);


    /**
     * 存储会话
     * @param uSessionId
     * @return
     */
    public abstract void storeSession(String uSessionId, Session session);


    /**
     * 根据会话ID查询
     * @param sessionId
     * @return
     */
    public abstract Session sessionBySessionId(String sessionId);

    /**
     * 根据客户端ID查询会话
     * @param clientId
     * @return
     */
    public abstract Session sessionByClientId(String clientId);

    /**
     * 生成会话ID
     * @return TODO 可以将该方法抽取成会话ID生成策略，便于后续分布式部署
     */
    public String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 删除
     * @param sessionId
     */
    public abstract void removeBySessionId(String sessionId);

    /**
     * 获取所有clean session为true的会话
     * @return
     */
    public abstract List<Session> findAllCleanSession();
}
