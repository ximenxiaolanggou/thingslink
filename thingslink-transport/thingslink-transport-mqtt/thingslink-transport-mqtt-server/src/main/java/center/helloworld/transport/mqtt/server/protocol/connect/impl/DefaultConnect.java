package center.helloworld.transport.mqtt.server.protocol.connect.impl;

import center.helloworld.transport.mqtt.server.auth.IAuthStrategy;
import center.helloworld.transport.mqtt.server.protocol.connect.Connect;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhishun.cai
 * @date 2024/3/18
 * @note
 */

@Slf4j
@Service
public class DefaultConnect implements Connect {

    @Autowired
    private IAuthStrategy auth;

    @Override
    public void process(Channel channel, MqttConnectMessage message) {
        // 处理连接心跳包(keepalive 客户端可以自行设置，会根据客户端设置时间的1.5倍作为心跳时长)
        int expire = 0;
        if (message.variableHeader().keepAliveTimeSeconds() > 0) {
            if (channel.pipeline().names().contains("idle")) {
                channel.pipeline().remove("idle");
            }
            expire = Math.round(message.variableHeader().keepAliveTimeSeconds() * 1.5f);
            channel.pipeline().addFirst("idle", new IdleStateHandler(expire, 0, 0));
        }
        // 设置换回消息体
        boolean cleanSession = message.variableHeader().isCleanSession();
        MqttConnAckMessage mqttConnAckMessage = new MqttConnAckMessage(new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, !cleanSession));
        System.out.println(channel);
        System.out.println(message);
        channel.writeAndFlush(mqttConnAckMessage);
    }
}
