package center.helloworld.transport.mqtt.server.storage;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author zhishun.cai
 * @create 2023/8/2
 * @note 消息存储模式，比如内存、Redis、MySQL、InfluxDB等等
 */
public interface IStorageMode<T> {


    /**
     * 存储消息
     * @param key key
     * @param t 消息体
     * @param expire 过期时间
     */
    public void put(String key, T t, long expire);

    /**
     * hash put
     * @param key
     * @param hKey
     * @param t
     */
    public void hput(String key, String hKey, T t, long expire);

    /**
     * set put
     * @param key
     * @param val 值
     */
    public void sput(String key, String val, long expire);

    /**
     * 获取消息
     * @param key key
     * @return
     */
    public T get(String key);

    /**
     * hash get
     * @param key
     * @param hKey
     * @return
     */
    public T hget(String key, String hKey);

    /**
     * h mget
     * @param key
     * @return
     */
    Map<String, T> hmget(String key);


    /**
     * 删除消息
     * @param key key
     */
    public void remove(String key);

    /**
     * hash 删除项
     * @param key
     * @param hkey
     */
    default void hremove(String key, String hkey) {

    }

    /**
     * 根据集合删除
     * @param keys
     */
    public void remove(Collection<String> keys);

    /**
     * 判断是否存在
     * @param key
     */
    public boolean exist(String key);

    /**
     * hash 存储 key是否存在
     * @param key
     * @param hKey
     * @return
     */
    public boolean hexist(String key, String hKey);

    /**
     * 获取所有key
     * @return
     */
    public Set<String> allKeys(String keyPattern);

    /**
     * set 成员
     * @param key
     * @return
     */
    Set<Object> smembers(String key);

    /**
     * set removes成员
     * @param key
     * @param values
     */
    void sremove(String key,  Object... values);
}

