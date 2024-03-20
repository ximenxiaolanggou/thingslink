package center.helloworld.transport.mqtt.server.protocol.connect;

import center.helloworld.transport.mqtt.server.session.entity.Session;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;

/**
 * @author zhishun.cai
 * @date 2024/3/18
 * @note 连接处理
 */

public interface Connect {

    /**
     * 连接处理
     * @param channel
     * @param message
     */
     default void process(Channel channel, MqttConnectMessage message){

     }

}
