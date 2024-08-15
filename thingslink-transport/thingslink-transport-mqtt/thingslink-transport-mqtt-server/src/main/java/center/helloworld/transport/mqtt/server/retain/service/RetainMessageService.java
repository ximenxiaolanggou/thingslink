package center.helloworld.transport.mqtt.server.retain.service;


import center.helloworld.transport.mqtt.server.message.pojo.Message;

import java.util.List;

/**
 * @author zhishun.cai
 * @create 2023/8/4
 * @note Retain 消息处理服务层
 */
public interface RetainMessageService {

    /**
     * 存储retain标志消息
     */
    void put(String topic, Message retainMessageStore);

    /**
     * 获取retain消息
     */
    Message get(String topic);

    /**
     * 删除retain标志消息
     */
    void remove(String topic);

    /**
     * 判断指定topic的retain消息是否存在
     */
    boolean containsKey(String topic);

    /**
     * 获取retain消息集合
     */
    List<Message> search(String topicFilter);
}
