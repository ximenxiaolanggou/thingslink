package center.helloworld.transport.mqtt.server.protocol.publish;

import center.helloworld.transport.mqtt.server.protocol.publish.observer.PublishMessageSubject;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note
 */
public interface Publish {


    /**
     * 存储publish消息
     * @param channel
     * @param msg
     */
    default boolean storagePublishMessage(Channel channel, MqttPublishMessage msg) {
        return false;
    }

    /**
     * 发布消息
     * @param channel
     * @param msg
     */
    default void publishMessage(PublishMessageSubject publishMessageSubject, Channel channel, MqttPublishMessage msg){
        publishMessageSubject.advice(channel, msg, null, null);
    }

    /**
     * pubRec响应事件 Qos为2时
     * @param channel
     * @param message
     */
    default void pubRec(Channel channel, MqttPublishMessage message) {

    }

    /**
     * 尝试重发 PubRec
     * @param channel
     * @param message
     */
    default void retryPubRec(Channel channel, MqttPublishMessage message) {

    }

    /**
     * 消息确认 Qos为1时
     * @param channel
     * @param message
     */
    default void pubAck(Channel channel, MqttPublishMessage message){

    }

    /**
     * retain消息处理
     * @param channel
     * @param msg
     */
    void storeRetainMessage(Channel channel, MqttPublishMessage msg);

    /**
     * 处理发布事件
     * @param channel
     * @param msg
     */
    default void process(PublishMessageSubject publishMessageSubject, Channel channel, MqttPublishMessage msg) {
        boolean repeatMsg = storagePublishMessage(channel, msg);
        // 不为重读消息则广播消息
        if(!repeatMsg)
            publishMessage(publishMessageSubject, channel, msg);
        // Qos为 1时执行pubAck
        pubAck(channel, msg);
        // Qos为 2时执行pubRec
        pubRec(channel, msg);
        retryPubRec(channel, msg);
        storeRetainMessage(channel, msg);
    }
}
