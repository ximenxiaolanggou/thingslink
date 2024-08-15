package center.helloworld.transport.mqtt.server.dup.service.impl;

import center.helloworld.transport.mqtt.server.dup.dao.DupPubRelMessageDao;
import center.helloworld.transport.mqtt.server.dup.service.DupPubRelMessageService;
import center.helloworld.transport.mqtt.server.message.pojo.Message;
import center.helloworld.transport.mqtt.server.packetid.MessageIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhishun.cai
 * @create 2023/7/27
 * @note qos 为2时pubrel重复发送存储
 */

@Service
public class DupPubRelMessageServiceImpl implements DupPubRelMessageService {

    @Resource
    private DupPubRelMessageDao dupPubRelMessageDao;


    @Autowired
    private MessageIdService messageIdService;


    @Override
    public void put(String clientId, Message dupPubRelMessage) {
        dupPubRelMessageDao.hput(clientId, String.valueOf(messageIdService.getNextMessageId(clientId)), dupPubRelMessage);
    }

    @Override
    public List<Message> get(String clientId) {
        if (dupPubRelMessageDao.exist(clientId)) {
            ConcurrentHashMap<Integer, Message> map = new ConcurrentHashMap<>();
            Map<String, Message> hentriesMap = dupPubRelMessageDao.hmget(clientId);
            if (hentriesMap != null && !hentriesMap.isEmpty()) {
                hentriesMap.forEach((k, v) -> {
                    map.put(Integer.valueOf(k), v);
                });
            }
            Collection<Message> collection = map.values();
            return new ArrayList<>(collection);
        }
        return new ArrayList<>();
    }

    @Override
    public void remove(String clientId, int messageId) {
        dupPubRelMessageDao.hdel(clientId, String.valueOf(messageId));
    }

    @Override
    public void removeByClient(String clientId) {
        dupPubRelMessageDao.remove(clientId);
    }
}
