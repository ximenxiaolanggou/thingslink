package center.helloworld.transport.mqtt.server.dup.dao;

import center.helloworld.transport.mqtt.server.message.MessageStore;
import center.helloworld.transport.mqtt.server.message.pojo.Message;
import center.helloworld.transport.mqtt.server.storage.IStorageMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @create 2023/8/2
 * @note Rec 消息存储实现类
 */

@Component
public class DupPubRecMessageDao extends MessageStore {

    private String CACH_PREFIX = "thingslink_dup_rec_msg_";

    @Autowired
    public DupPubRecMessageDao(IStorageMode messageStorageMode) {
        super(messageStorageMode);
    }


    @Override
    public void put(String id, Message message) {
        messageStorageMode.put(CACH_PREFIX + id, message, 0L);
    }

    @Override
    public Message get(String key) {
        return messageStorageMode.get(CACH_PREFIX + key);
    }

    @Override
    public void remove(String key) {
        messageStorageMode.remove(CACH_PREFIX + key);
    }

    @Override
    public boolean exist(String key) {
        return messageStorageMode.exist(CACH_PREFIX + key);
    }
}
