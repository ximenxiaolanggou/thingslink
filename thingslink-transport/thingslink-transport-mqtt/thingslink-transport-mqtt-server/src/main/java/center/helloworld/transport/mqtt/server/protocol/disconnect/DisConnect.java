package center.helloworld.transport.mqtt.server.protocol.disconnect;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note 关闭连接处理
 */
public interface DisConnect {

    default void disConnectProcess(Channel channel, MqttMessage msg) {
        broadcastWillMessage(channel, msg);
        clearResource(channel);
        disConnect(channel);
        broadcastDisConnected(channel);
    }

    /**
     * 广播 will 消息
     * @param channel
     * @param msg
     */
    void broadcastWillMessage(Channel channel, MqttMessage msg);

    /**
     * 清除占用资源
     * @param channel
     */
    void clearResource(Channel channel);

    /**
     * 关闭连接
     * @param channel
     */
    void disConnect(Channel channel);

    /**
     * 广播关闭连接消息
     * @param channel
     */
    default void broadcastDisConnected(Channel channel) {

    }
}
