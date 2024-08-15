package center.helloworld.transport.mqtt.server.dup.service.impl;

import center.helloworld.transport.mqtt.server.dup.dao.DupPublishMessageDao;
import center.helloworld.transport.mqtt.server.dup.service.DupPublishMessageService;
import center.helloworld.transport.mqtt.server.message.pojo.Message;
import center.helloworld.transport.mqtt.server.packetid.MessageIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhishun.cai
 * @create 2023/7/27
 * @note
 */

@Service
public class DupPublishMessageServiceImpl implements DupPublishMessageService {


    @Autowired
    private MessageIdService messageIdService;

    @Autowired
    private DupPublishMessageDao dupPublishMessageDao;



    @Override
    public void put(String clientId, Message dupPublishMessage) {
        dupPublishMessageDao.hput(clientId, String.valueOf(messageIdService.getNextMessageId(clientId)), dupPublishMessage);
    }

    @Override
    public List<Message> get(String clientId) {
        if (dupPublishMessageDao.exist(clientId)) {
            ConcurrentHashMap<Integer, Message> map = new ConcurrentHashMap<>();
            Map<String, Message> hentriesMap = dupPublishMessageDao.hmget(clientId);
            if (hentriesMap != null && !hentriesMap.isEmpty()) {
                hentriesMap.forEach((k, v) -> {
                    map.put(Integer.valueOf(k),  v);
                });
            }
            Collection<Message> collection = map.values();
            return new ArrayList<>(collection);
        }
        return new ArrayList<>();
    }

    @Override
    public void remove(String clientId, int messageId) {
        dupPublishMessageDao.hdel(clientId, String.valueOf(messageId));
    }

    @Override
    public void removeByClient(String clientId) {
        dupPublishMessageDao.remove(clientId);
    }
}
