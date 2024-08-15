package center.helloworld.transport.mqtt.server.retain.service.impl;

import center.helloworld.transport.mqtt.server.message.pojo.Message;
import center.helloworld.transport.mqtt.server.retain.dao.RetainMessageDao;
import center.helloworld.transport.mqtt.server.retain.service.RetainMessageService;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author zhishun.cai
 * @create 2023/8/4
 * @note
 */

@Slf4j
@Service
public class RetainMessageServiceImpl implements RetainMessageService {

    @Resource
    private RetainMessageDao retainMessageDao;


    @Override
    public void put(String topic, Message retainMessage) {
        retainMessageDao.put(topic, retainMessage);
    }

    @Override
    public Message get(String topic) {
        return (Message) retainMessageDao.get(topic);
    }

    @Override
    public void remove(String topic) {
        retainMessageDao.remove(topic);
    }

    @Override
    public boolean containsKey(String topic) {
        return retainMessageDao.exist( topic);
    }

    @Override
    public List<Message> search(String topicFilter) {
        List<Message> retainMessageStores = new ArrayList<Message>();
        // 1. 判断是否有通配符
        if(!topicFilter.contains("#") && !topicFilter.contains("+")) {
            // 不包含通配符
            if (retainMessageDao.exist( topicFilter)) {
                retainMessageStores.add(retainMessageDao.get(topicFilter));
            }
        }else {
            // 包含通配符
            Set<String> keys = retainMessageDao.getMessageStorageMode().allKeys(null);
            Map<String, Message> map = new HashMap();
            for (String key : map.keySet()) {
                Message retainMessage = retainMessageDao.get(key);
                map.put(retainMessage.getTopic(), retainMessage);
            }
            map.forEach((topic, val) -> {
                if (StrUtil.split(topic, '/').size() >= StrUtil.split(topicFilter, '/').size()) {
                    List<String> splitTopics = StrUtil.split(topic, '/');
                    List<String> spliteTopicFilters = StrUtil.split(topicFilter, '/');
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
                        retainMessageStores.add(val);
                    }
                }
            });
        }
        return retainMessageStores;
    }
}
