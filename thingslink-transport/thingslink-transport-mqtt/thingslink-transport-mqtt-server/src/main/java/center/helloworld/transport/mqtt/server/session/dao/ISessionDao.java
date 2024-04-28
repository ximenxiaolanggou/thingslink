package center.helloworld.transport.mqtt.server.session.dao;

import center.helloworld.transport.mqtt.server.session.entity.Session;
import cn.hutool.core.lang.UUID;

import java.util.Set;

/**
 * @author zhishun.cai
 * @date 2024/3/19
 * @note 会话存储策略接口
 */
public interface ISessionDao {

    /**
     * 存储会话
     * @param uSessionId
     * @return
     */
    void storeSession(String uSessionId, Session session, long expire);


    /**
     * 存储会话
     * @param uSessionId
     * @return
     */
    void storeSession(String uSessionId, Session session);


    /**
     * 根据会话ID查询
     * @param sessionId
     * @return
     */
    Session sessionBySessionId(String sessionId);

    /**
     * 根据客户端ID查询会话
     * @param clientId
     * @return
     */
    Session sessionByClientId(String clientId);

    /**
     * 生成会话ID
     * @return TODO 可以将该方法抽取成会话ID生成策略，便于后续分布式部署
     */
    default String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 删除
     * @param sessionId
     */
    void removeBySessionId(String sessionId);

}
