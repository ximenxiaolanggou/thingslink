package center.helloworld.transport.mqtt.server.protocol.subscribe.dao;

import center.helloworld.transport.mqtt.server.protocol.subscribe.entity.SubscribeStore;
import center.helloworld.transport.mqtt.server.storage.IStorageMode;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author zhishun.cai
 * @create 2024/4/28
 * @note
 */

@Component
public class SubscribeWildcardMessageDao {

    private final static String CACHE_PRE = "thingslink_sub_wildcard_";
    private final static String CACHE_CLIENT_PRE = "thingslink_sub_client_";

    private IStorageMode<SubscribeStore> messageStorageMode;

    @Autowired
    public SubscribeWildcardMessageDao(IStorageMode messageStorageMode) {
        this.messageStorageMode = messageStorageMode;
    }



    public void put(String topic, String clientId ,SubscribeStore subscribeStore) {
        messageStorageMode.hput(CACHE_PRE + topic, clientId, subscribeStore, 0L);
        messageStorageMode.sput(CACHE_CLIENT_PRE + clientId, topic, 0L);
    }

    public SubscribeStore get(String id) {
        return null;
    }

    public SubscribeStore get(String key, String hKey) {
        return messageStorageMode.hget(CACHE_PRE + key, hKey);
    }

    public void remove(String topic, String clientId) {
        messageStorageMode.hremove(CACHE_PRE + topic, clientId);
        messageStorageMode.sremove(CACHE_CLIENT_PRE + clientId, topic);
    }

    public boolean exist(String key) {
        return messageStorageMode.exist(CACHE_PRE + key);
    }

    public void clear() {
        Set<String> keys = messageStorageMode.allKeys(CACHE_PRE + "*");
        messageStorageMode.remove(keys);
        keys = messageStorageMode.allKeys(CACHE_CLIENT_PRE + "*");
        messageStorageMode.remove(keys);
    }



    public void removeForClient(String clientId) {
        Set<Object> smembers = messageStorageMode.smembers(CACHE_CLIENT_PRE + clientId);
        for (Object topic : smembers) {
            messageStorageMode.hremove(CACHE_PRE + topic, clientId);
        }
        messageStorageMode.remove(CACHE_CLIENT_PRE + clientId);
    }

    /**
     * 获取所有
     * @return
     */
    public List<SubscribeStore> hallValues(String key) {
        Map<String, SubscribeStore> map = messageStorageMode.hmget(CACHE_PRE + key);
        List<SubscribeStore> stores = new ArrayList<>();
        map.forEach((k, v) -> {
            stores.add(v);
        });
        return stores;
    }

    public Map<String, Map<String,SubscribeStore>> all() {
        Map<String, Map<String,SubscribeStore>> resMap = new HashMap();
        Set<String> keys = messageStorageMode.allKeys(CACHE_PRE + "*");

        if(CollectionUtil.isNotEmpty(keys)) {
            for (String key : keys) {
                Map<String, SubscribeStore> values = messageStorageMode.hmget(key);
                resMap.put(key.substring(CACHE_PRE.length()), values);
            }
        }
        return resMap;
    }
}
