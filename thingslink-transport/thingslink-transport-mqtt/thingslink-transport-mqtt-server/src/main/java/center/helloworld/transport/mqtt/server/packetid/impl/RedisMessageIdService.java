package center.helloworld.transport.mqtt.server.packetid.impl;

import center.helloworld.starter.redis.service.RedisService;
import center.helloworld.transport.mqtt.server.packetid.MessageIdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhishun.cai
 * @create 2024/4/28
 * @note
 */

@Slf4j
@Service
public class RedisMessageIdService implements MessageIdService {

    @Autowired
    private RedisService redisService;

    @Override
    public int getNextMessageId(String clientId) {
        double inc = redisService.hincr(MESSAGE_ID_PREFIX, clientId, 1.0);
        return ((int) inc) % 65536;
    }
}
