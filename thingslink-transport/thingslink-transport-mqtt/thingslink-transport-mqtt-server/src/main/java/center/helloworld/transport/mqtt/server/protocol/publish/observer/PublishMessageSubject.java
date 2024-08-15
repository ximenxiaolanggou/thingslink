package center.helloworld.transport.mqtt.server.protocol.publish.observer;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

import java.util.Collection;


/**
 * @author zhishun.cai
 * @create 2023/8/3
 * @note 发布消息 subject, 用来抽象对监听者操作
 */
public interface PublishMessageSubject {

    /**
     * 添加观察者
     * @param observer
     */
    void attach(String key , PublishMessageObserver observer);

    /**
     * 取消观察者
     * @param key
     */
    void detach(String key);

    /**
     * 通知
     */
    void advice(Channel channel, MqttPublishMessage message, PublishMessageCallBack successCallback, PublishMessageCallBack failCalback);

    /**
     * 通知
     * @param channel
     * @param message
     * @param includeObservers 需要通知的观察者
     */
    void adviceIncludeObservers(Channel channel, MqttPublishMessage message, Collection<Class<? extends PublishMessageObserver> > includeObservers, PublishMessageCallBack successCallback, PublishMessageCallBack failCalback);
}
