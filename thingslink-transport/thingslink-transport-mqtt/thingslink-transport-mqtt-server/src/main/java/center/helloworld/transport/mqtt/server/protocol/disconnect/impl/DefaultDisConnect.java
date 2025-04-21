package center.helloworld.transport.mqtt.server.protocol.disconnect.impl;

import center.helloworld.transport.mqtt.server.protocol.disconnect.DisConnect;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note
 */

@Component
public class DefaultDisConnect implements DisConnect {


    @Override
    public void process(Channel channel, MqttMessage msg) {
        System.out.println("disConnectProcess");
        channel.close();
    }
}
