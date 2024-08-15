package center.helloworld.transport.mqtt.server.dup.dao;

import center.helloworld.transport.mqtt.server.message.MessageStore;
import center.helloworld.transport.mqtt.server.message.pojo.Message;
import center.helloworld.transport.mqtt.server.storage.IStorageMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhishun.cai
 * @create 2023/8/2
 * @note Rel 消息存储实现类
 */

@Component
public class DupPubRelMessageDao extends MessageStore {

    private String CACH_PREFIX = "thingslink_dup_rel_msg_";

    @Autowired
    public DupPubRelMessageDao(IStorageMode messageStorageMode) {
        super(messageStorageMode);
    }


    @Override
    public void put(String id, Message message) {
        messageStorageMode.put(CACH_PREFIX + id, message, 0L);
    }

    /**
     * hash set
     * @param id
     * @param hkey
     * @param message
     */
    public void hput(String id, String hkey, Message message) {
        messageStorageMode.hput(id, hkey, message, 0L);
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

    /**
     *
     * @param clientId
     * @return
     */
    public Map<String, Message> hmget(String clientId) {
        return messageStorageMode.hmget(clientId);
    }


    /**
     * hash 删除item
     * @param key
     * @param hKey
     */
    public void hdel(String key, String hKey) {
        messageStorageMode.hremove(key, hKey);
    }
}
