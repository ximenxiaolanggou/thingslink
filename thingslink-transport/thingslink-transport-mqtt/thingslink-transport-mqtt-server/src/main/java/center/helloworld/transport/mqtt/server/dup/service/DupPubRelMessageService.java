package center.helloworld.transport.mqtt.server.dup.service;


import center.helloworld.transport.mqtt.server.message.pojo.Message;

import java.util.List;

/**
 * @author zhishun.cai
 * @create 2023/7/27
 * @note
 */
public interface DupPubRelMessageService {

    /**
     * 存储消息
     */
    void put(String clientId, Message dupPubRelMessageStore);

    /**
     * 获取消息集合
     */
    List<Message> get(String clientId);

    /**
     * 删除消息
     */
    void remove(String clientId, int messageId);

    /**
     * 删除消息
     */
    void removeByClient(String clientId);
}
