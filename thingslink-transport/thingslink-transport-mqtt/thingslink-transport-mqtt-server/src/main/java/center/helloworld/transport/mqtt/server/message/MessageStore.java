package center.helloworld.transport.mqtt.server.message;

import center.helloworld.transport.mqtt.server.message.pojo.Message;
import center.helloworld.transport.mqtt.server.storage.IStorageMode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author zhishun.cai
 * @create 2023/8/2
 * @note 消息存储抽象类
 */

@Data
@NoArgsConstructor
public abstract class MessageStore {

    public IStorageMode<Message> messageStorageMode;

    public MessageStore(IStorageMode messageStorageMode) {
        this.messageStorageMode = messageStorageMode;
    }

    /**
     * 存储数据
     * @param message
     */
    public abstract void put(String id, Message message);

    /**
     * hash set
     * @param id
     * @param hkey
     * @param message
     */
    public void hput(String id, String hkey, Message message) {

    }



    /**
     * 获取数据
     * @param id
     */
    public abstract Message get(String id);

    /**
     * hash get
     * @param id
     * @param hKey
     * @return
     */
    public Message get(String id, String hKey) {
        return null;
    }


    /**
     * 删除
     * @param id
     */
    public abstract void remove(String id);

    /**
     * 判断是否存在
     * @param key
     * @return
     */
    public abstract boolean exist(String key);

    /**
     * 清除
     */
    public void clear() {

    }

    /**
     * 获取所有
     * @return
     */
    public List<Message> hallValues(String key) {
        return null;
    }


    /**
     * hash hash获取
     * @param clientId
     * @return
     */
    public Map<String, Message> hmget(String clientId) {
        return null;
    }

    /**
     * hash 删除item
     * @param key
     * @param hKey
     */
    public void hdel(String key, String hKey) {
    }
}


