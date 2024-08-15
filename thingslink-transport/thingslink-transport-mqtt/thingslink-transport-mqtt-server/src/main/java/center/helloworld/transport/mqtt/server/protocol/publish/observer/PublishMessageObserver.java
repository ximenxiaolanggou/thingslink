package center.helloworld.transport.mqtt.server.protocol.publish.observer;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttPublishMessage;


/**
 * @author zhishun.cai
 * @create 2023/8/3
 * @note 消息发布观察者
 */
public interface PublishMessageObserver {

    /**
     * 事件发生
     * @param msg
     */
    void onEvent(Channel channel, MqttPublishMessage msg, PublishMessageCallBack successCallback, PublishMessageCallBack failCalback);

}
