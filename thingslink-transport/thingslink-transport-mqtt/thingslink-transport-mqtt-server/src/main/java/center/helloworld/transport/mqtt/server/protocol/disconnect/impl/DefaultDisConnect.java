package center.helloworld.transport.mqtt.server.protocol.disconnect.impl;

import center.helloworld.transport.mqtt.server.channel.ChannelRegister;
import center.helloworld.transport.mqtt.server.protocol.disconnect.DisConnect;
import center.helloworld.transport.mqtt.server.session.SessionStore;
import center.helloworld.transport.mqtt.server.session.entity.Session;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note
 */

@Slf4j
@Component
public class DefaultDisConnect implements DisConnect {

    @Autowired
    private ChannelRegister channelRegister;

    @Autowired
    private SessionStore sessionDao;

    /**
     * 广播遗言消息
     * @param channel
     * @param msg
     */
    @Override
    public void broadcastWillMessage(Channel channel, MqttMessage msg) {

    }

    /**
     * 清除资源
     * @param channel
     */
    @Override
    public void clearResource(Channel channel) {
        String sessionId = (String) channel.attr(AttributeKey.valueOf("sessionId")).get();
        // 1. 会话清除
        Session session = sessionDao.sessionBySessionId(sessionId);
        if(session != null && session.getExpire() == 0) {
            // 会话过期时长设置为0
            // 清除会话
            sessionDao.removeBySessionId(sessionId);
            // TODO 清除会话相关数据
        }

        // 标志清除完成（避免在handler中做重复操作）
        channel.attr(AttributeKey.valueOf("cleanCache")).set(true);
    }

    /**
     * 关闭连接
     * @param channel
     */
    @Override
    public void disConnect(Channel channel) {
        String clientId = (String) channel.attr(AttributeKey.valueOf("clientIdentifier")).get();
        log.debug("DISCONNECT - clientId: {}", clientId);
        channel.close();
    }
}
