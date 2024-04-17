package center.helloworld.transport.mqtt.server.channel.impl;

import center.helloworld.transport.mqtt.server.channel.ChannelRegister;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhishun.cai
 * @create 2024/4/12
 * @note
 */
@Service
public class MemChannelRegister implements ChannelRegister {

    private static Map<String, Channel> channels = new ConcurrentHashMap();

    @Override
    public void registerChannel(String sessionId, Channel channel) {
        channels.put(sessionId, channel);
    }

    @Override
    public void removeChannel(String sessionId) {
        channels.remove(sessionId);
    }

    @Override
    public Channel getChannel(String sessionId) {
        return channels.get(sessionId);
    }

    /**
     * 打印信息
     */
    @Override
    public void print() {
        if(CollectionUtils.isEmpty(channels))
            System.out.println("空信息");

        for (String s : channels.keySet()) {
            System.out.println("id -> " + s);
            System.out.println("channel -> " + channels);
        }
    }

    @Override
    public void clear() {
        channels.clear();
    }
}
