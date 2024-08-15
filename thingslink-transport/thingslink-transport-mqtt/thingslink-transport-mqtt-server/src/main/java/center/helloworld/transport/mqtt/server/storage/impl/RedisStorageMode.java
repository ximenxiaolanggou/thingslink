package center.helloworld.transport.mqtt.server.storage.impl;

import center.helloworld.starter.redis.service.RedisService;
import center.helloworld.transport.mqtt.server.storage.IStorageMode;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author zhishun.cai
 * @create 2023/8/2
 * @note Redis 中存储
 */

@Slf4j
@Component
public class RedisStorageMode<T> implements IStorageMode<T> {

    @Autowired
    private RedisService redisService;


    /**
     * h mget
     * @param key
     * @return
     */
    @Override
    public Map<String, T> hmget(String key) {
        Map<Object, Object> map = redisService.hmget(key);
        Map<String, T> resMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(map)) {
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                resMap.put(String.valueOf(entry.getKey()), (T) entry.getValue());
            }
        }
        return resMap;
    }

    /**
     * hash get
     * @param key
     * @param hKey
     * @return
     */
    @Override
    public T hget(String key, String hKey) {
        return (T) redisService.hget(key, hKey);
    }

    /**
     * set put
     * @param key
     * @param topic
     */
    @Override
    public void sput(String key, String topic, long expire) {
        if(expire > 0) {
            redisService.sSet(key, topic, expire);
        }else {
            redisService.sSet(key, topic);
        }
    }

    /**
     * hash put
     * @param key
     * @param hKey
     * @param t
     * @param expire
     */
    @Override
    public void hput(String key, String hKey, T t, long expire) {
        if(expire > 0) {
            redisService.hset(key, hKey, t, expire);
        }else {
            redisService.hset(key, hKey, t);
        }
    }

    /**
     * 存储消息
     * @param key key
     * @param t 消息体
     * @param expire 过期时间
     */
    @Override
    public void put(String key, T t, long expire) {
        if(expire > 0) {
            redisService.set(key, t, expire);
        }else {
            redisService.set(key, t);
        }
    }

    /**
     * 获取消息
     * @param key key
     * @return
     */
    @Override
    public T get(String key) {
        return (T) redisService.get(key);
    }

    /**
     * 删除消息
     * @param key key
     */
    @Override
    public void remove(String key) {
        redisService.del(key);
    }

    /**
     * hash 删除
     * @param key
     * @param hkey
     */
    @Override
    public void hremove(String key, String hkey) {
        redisService.hdel(key, hkey);
    }

    /**
     * 根据集合删除
     * @param keys
     */
    @Override
    public void remove(Collection<String> keys) {
        redisService.del(keys);
    }


    @Override
    public boolean hexist(String key, String hKey) {
        return redisService.hHasKey(key, hKey);
    }

    /**
     * set 成员
     * @param key
     * @return
     */
    @Override
    public Set<Object> smembers(String key) {
        return redisService.smembers(key);
    }

    /**
     * set removes成员
     * @param key
     * @param values
     */
    @Override
    public void sremove(String key, Object... values) {
        redisService.setRemove(key, values);
    }

    /**
     * 正则获取所有key
     * @param keyPattern
     * @return
     */
    @Override
    public Set<String> allKeys(String keyPattern) {
       return redisService.allKeys(keyPattern);
    }

    /**
     * 判断是否存在
     * @param key
     */
    @Override
    public boolean exist(String key) {
        return redisService.exist(key);
    }
}
