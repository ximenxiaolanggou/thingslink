package center.helloworld.transport.mqtt.server.protocol.disconnect.impl;

import center.helloworld.transport.mqtt.server.channel.ChannelRegister;
import center.helloworld.transport.mqtt.server.protocol.disconnect.DisConnect;
import center.helloworld.transport.mqtt.server.session.dao.ISessionDao;
import center.helloworld.transport.mqtt.server.session.entity.Session;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    private ISessionDao sessionDao;

    /**
     * 关闭连接处理
     * @param channel
     * @param msg
     */
    @Override
    public void disConnectProcess(Channel channel, MqttMessage msg) {

    }

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

    }

    /**
     * 关闭连接
     * @param channel
     */
    @Override
    public void disConnect(Channel channel) {
        String clientId = (String) channel.attr(AttributeKey.valueOf("clientId")).get();
        log.debug("DISCONNECT - clientId: {}", clientId);
        channel.close();
    }
}
