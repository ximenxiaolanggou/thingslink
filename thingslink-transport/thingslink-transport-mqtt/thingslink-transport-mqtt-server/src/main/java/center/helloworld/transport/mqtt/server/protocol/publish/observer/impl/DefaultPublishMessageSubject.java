package center.helloworld.transport.mqtt.server.protocol.publish.observer.impl;

import center.helloworld.transport.mqtt.server.protocol.publish.observer.PublishMessageCallBack;
import center.helloworld.transport.mqtt.server.protocol.publish.observer.PublishMessageObserver;
import center.helloworld.transport.mqtt.server.protocol.publish.observer.PublishMessageSubject;
import cn.hutool.core.collection.CollectionUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

/**
 * @author zhishun.cai
 * @create 2023/8/3
 * @note 发布消息 subject, 用来抽象对监听者操作
 */

@Service
public class DefaultPublishMessageSubject implements PublishMessageSubject {

    @Autowired
    private Map<String, PublishMessageObserver> observerMap;

    @Override
    public synchronized void attach(String key, PublishMessageObserver observer) {
        observerMap.put(key, observer);
    }

    @Override
    public void detach(String key) {
        observerMap.remove(key);
    }

    @Override
    public void advice(Channel channel, MqttPublishMessage message, PublishMessageCallBack successCallback, PublishMessageCallBack failCalback) {
        if(CollectionUtil.isNotEmpty(observerMap)) {
            for (PublishMessageObserver observer : observerMap.values()) {
                try {
                    observer.onEvent(channel, message,successCallback, failCalback);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 通知
     * @param channel
     * @param message
     * @param includeObservers 需要通知的观察者
     */
    @Override
    public void adviceIncludeObservers(Channel channel, MqttPublishMessage message, Collection<Class<? extends PublishMessageObserver>> includeObservers, PublishMessageCallBack successCallback, PublishMessageCallBack failCalback) {
        if(CollectionUtil.isNotEmpty(observerMap) && CollectionUtil.isNotEmpty(includeObservers)) {
            for (Class<? extends PublishMessageObserver> includeObserver : includeObservers) {
                for (PublishMessageObserver observer : observerMap.values()) {
                    if(includeObserver == observer.getClass()) {
                        observer.onEvent(channel, message, successCallback, failCalback);
                    }
                }
            }

        }
    }

}
