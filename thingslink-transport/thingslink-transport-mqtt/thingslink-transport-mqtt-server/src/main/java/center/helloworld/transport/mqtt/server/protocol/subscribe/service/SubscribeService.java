package center.helloworld.transport.mqtt.server.protocol.subscribe.service;

import center.helloworld.transport.mqtt.server.protocol.subscribe.entity.SubscribeStore;

import java.util.List;
import java.util.Map;

/**
 * @author zhishun.cai
 * @create 2024/4/28
 * @note
 */
public interface SubscribeService {


    /**
     * 校验topic合法性
     * @param topicNames
     * @return
     */
    boolean validTopicFilter(String topicReg, List<String> topicNames);

    /**
     * acl权限控制
     * @param clientId 设备客户端Id
     * @param topicNames
     * @return
     */
    boolean validateAcl(String clientId, List<String> topicNames);

    /**
     * 存储订阅
     */
    void put(String topicFilter, SubscribeStore subscribeStore);

    /**
     * 删除订阅
     */
    void remove(String topicFilter, String clientId);

    /**
     * 删除clientId的订阅
     */
    void removeForClient(String clientId);

    /**
     * 获取订阅存储集
     */
    List<SubscribeStore> search(String topic);

    /**
     * 判断客户端是否已经订阅该主题
     * @param clientId
     * @param topic
     * @return
     */
    SubscribeStore subscribed(String clientId, String topic);

    /**
     * 从非通配符缓存中获取
     * @param topic
     * @return
     */
    Map<String, SubscribeStore> getFromNotWildcardCache(String topic);

    /**
     * 清空
     */
    public void clear();
}
