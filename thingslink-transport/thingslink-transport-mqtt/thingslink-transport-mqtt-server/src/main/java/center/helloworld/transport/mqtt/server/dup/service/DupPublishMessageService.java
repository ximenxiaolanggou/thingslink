package center.helloworld.transport.mqtt.server.dup.service;


import center.helloworld.transport.mqtt.server.message.pojo.Message;

import java.util.List;

/**
 * @author zhishun.cai
 * @create 2023/7/27
 * @note  PUBLISH重发消息存储服务接口, 当QoS=1和QoS=2时存在该重发机制
 */
public interface DupPublishMessageService {

    /**
     * 存储消息
     */
    void put(String clientId, Message dupPublishMessageStore);

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
