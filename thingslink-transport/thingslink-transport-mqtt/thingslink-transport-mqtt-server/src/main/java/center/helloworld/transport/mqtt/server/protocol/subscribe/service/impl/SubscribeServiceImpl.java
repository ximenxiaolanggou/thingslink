package center.helloworld.transport.mqtt.server.protocol.subscribe.service.impl;

import center.helloworld.starter.redis.service.RedisService;
import center.helloworld.transport.mqtt.server.protocol.subscribe.dao.SubscribeNotWildcardMessageDao;
import center.helloworld.transport.mqtt.server.protocol.subscribe.dao.SubscribeWildcardMessageDao;
import center.helloworld.transport.mqtt.server.protocol.subscribe.entity.SubscribeStore;
import center.helloworld.transport.mqtt.server.protocol.subscribe.service.SubscribeService;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author zhishun.cai
 * @create 2024/4/28
 * @note
 */

@Service
public class SubscribeServiceImpl implements SubscribeService {

    @Autowired
    private SubscribeNotWildcardMessageDao subscribeNotWildcardMessageDao;

    @Autowired
    private SubscribeWildcardMessageDao subscribeWildcardMessageDao;

    @Autowired
    private RedisService redisService;


    /**
     * 校验Topic合法性
     * @param topicSubscriptions
     * @return
     */


    /**
     * 校验topic合法性，校验规则：
     * 1. 必须以 / 开头，比如 /a/b/c OK, a/b/c NG
     * 2. 只支持 0~9 | a-zA-Z | _ | 通配符（ # | + ） /a/12/b/32 OK , /a/b/1/# OK , /123/b/** NG
     * 3. 单层通配符必须占据整个层级
     *      sensor/+ 有效
     *      sensor/+/temperature 有效
     *      sensor+ 无效（没有占据整个层级）
     * 4. 多层通配符表示它的父级和任意数量的子层级，在使用多层通配符时，它必须占据整个层级并且必须是主题的最后一个字符，例如：
     *      sensor/# 有效
     *      sensor/bedroom# 无效（没有占据整个层级）
     *      sensor/#/temperature 无效（不是主题最后一个字符）
     *  5. 两个通配符不可以同时使用（同一个topic中）
     *
     *  正则为：^(\\/[a-zA-Z0-9_]+)(((\\/[a-zA-Z0-9_]+)*(\\/\\#)?)|((\\/([a-zA-Z0-9_]+|\\+))*))
     * @param topicNames
     * @return
     *
     *
     */

    @Override
    public boolean validTopicFilter(String topicReg , List<String> topicNames) {
        for (String topicName : topicNames) {
            if(!Pattern.matches(topicReg, topicName)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String reg = "^(\\/[a-zA-Z0-9_]+)(((\\/[a-zA-Z0-9_]+)*(\\/\\#)?)|((\\/([a-zA-Z0-9_]+|\\+))*))";
        List<String> strings = Arrays.asList(
                "/a",
                "/a/b/c",
                "/+/a/b",
                "/+/+/a/b",
                "/a/+/a/+/c",
                "/a/#",
                "/a/+/a/#"

        );
        for (String string : strings) {
            boolean matches = Pattern.matches(reg, string);
            System.out.println(matches);
        }

    }

    /**
     * acl权限控制
     * @param clientId 设备客户端Id
     * @param topicNames 主题名称
     * @return
     */
    @Override
    public boolean validateAcl(String clientId, List<String> topicNames) {

        return true;
    }

    @Override
    public void put(String topicFilter, SubscribeStore subscribeStore) {
        if (StrUtil.contains(topicFilter, '#') || StrUtil.contains(topicFilter, '+')) {
            subscribeWildcardMessageDao.put(topicFilter, subscribeStore.getClientId(), subscribeStore);
        } else {
            subscribeNotWildcardMessageDao.put(topicFilter, subscribeStore.getClientId(), subscribeStore);
        }
    }

    @Override
    public void remove(String topicFilter, String clientId) {
        if (StrUtil.contains(topicFilter, '#') || StrUtil.contains(topicFilter, '+')) {
            subscribeWildcardMessageDao.remove(topicFilter, clientId);
        } else {
            subscribeNotWildcardMessageDao.remove(topicFilter, clientId);
        }
    }

    @Override
    public void removeForClient(String clientId) {
        subscribeNotWildcardMessageDao.removeForClient(clientId);
        subscribeWildcardMessageDao.removeForClient(clientId);
    }

    /**
     * 清空
     */
    @Override
    public void clear() {
        subscribeWildcardMessageDao.clear();
        subscribeNotWildcardMessageDao.clear();
    }

    /**
     * 从非通配符缓存中获取
     * @param topic
     * @return
     */
    @Override
    public Map<String, SubscribeStore> getFromNotWildcardCache(String topic) {
        return subscribeNotWildcardMessageDao.getByKey(topic);
    }

    /**
     * 判断客户端是否已经订阅该主题
     * @param clientId
     * @param topic
     * @return
     */
    @Override
    public SubscribeStore subscribed(String clientId, String topic) {

        SubscribeStore subscribeStore = null;
        if (StrUtil.contains(topic, '#') || StrUtil.contains(topic, '+')) {
            subscribeStore = subscribeNotWildcardMessageDao.get(topic, clientId);
        } else {
            subscribeStore = subscribeNotWildcardMessageDao.get(topic, clientId);
        }
        return subscribeStore;
    }

    @Override
    public List<SubscribeStore> search(String topic) {
        List<SubscribeStore> subscribeStores = new ArrayList<SubscribeStore>();
        List<SubscribeStore> list = subscribeNotWildcardMessageDao.hallValues(topic);
        if (list.size() > 0) {
            subscribeStores.addAll(list);
        }
        subscribeWildcardMessageDao.all().forEach((topicFilter, map) -> {
            if (StrUtil.split(topic, '/').size() >= StrUtil.split(topicFilter, '/').size()) {
                List<String> splitTopics = StrUtil.split(topic, '/');//a
                List<String> spliteTopicFilters = StrUtil.split(topicFilter, '/');//#
                String newTopicFilter = "";
                for (int i = 0; i < spliteTopicFilters.size(); i++) {
                    String value = spliteTopicFilters.get(i);
                    if (value.equals("+")) {
                        newTopicFilter = newTopicFilter + "+/";
                    } else if (value.equals("#")) {
                        newTopicFilter = newTopicFilter + "#/";
                        break;
                    } else {
                        newTopicFilter = newTopicFilter + splitTopics.get(i) + "/";
                    }
                }
                newTopicFilter = StrUtil.removeSuffix(newTopicFilter, "/");
                if (topicFilter.equals(newTopicFilter)) {
                    Collection<SubscribeStore> collection = map.values();
                    List<SubscribeStore> list2 = new ArrayList<SubscribeStore>(collection);
                    subscribeStores.addAll(list2);
                }
            }
        });
        return subscribeStores;
    }
}
