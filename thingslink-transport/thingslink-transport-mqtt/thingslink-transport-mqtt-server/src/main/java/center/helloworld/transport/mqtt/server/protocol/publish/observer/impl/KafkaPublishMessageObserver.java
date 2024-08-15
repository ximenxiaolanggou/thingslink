package center.helloworld.transport.mqtt.server.protocol.publish.observer.impl;

import center.helloworld.transport.mqtt.server.protocol.publish.observer.PublishMessageCallBack;
import center.helloworld.transport.mqtt.server.protocol.publish.observer.PublishMessageObserver;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.util.AttributeKey;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author zhishun.cai
 * @create 2023/8/3
 * @note kafka 监听
 */

@Slf4j
@Component
public class KafkaPublishMessageObserver /*implements PublishMessageObserver*/ {

//    @Autowired
//    private KafkaTemplate<String,String> kafkaTemplate;
//
//    @Override
//    @Transactional
//    public void onEvent(Channel channel, MqttPublishMessage msg, PublishMessageCallBack successCallback, PublishMessageCallBack failCalback) {
//        String clientId = (String) channel.attr(AttributeKey.valueOf("clientId")).get();
//        byte[] messageBytes = new byte[msg.payload().readableBytes()];
//        msg.payload().getBytes(msg.payload().readerIndex(), messageBytes);
//        String topicName = msg.variableHeader().topicName();
//        int qos = msg.fixedHeader().qosLevel().value();
//        String kafkaTopicName = topicName.substring(1).replace("/", "-");
//        try {
////        String[] topicLevels = topicName.split("/");
////        if(topicLevels.length < 5) {
////            log.warn("不合法的topic: {}", topicName);
////            return;
////        }
//            ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(kafkaTopicName, clientId, new String(messageBytes, StandardCharsets.UTF_8));
//            send.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
//                @Override
//                public void onFailure(Throwable throwable) {
//                    // 不定义失败回调会将失败的数据存储到MySQL中，如果重写失败回调就会覆盖默认逻辑
//                    if(failCalback != null) {
//                        failCalback.callBack();
//                    }else {
//                        // 保存到数据库
//                        messageSaveToDB(clientId, topicName,new String(messageBytes, StandardCharsets.UTF_8), qos);
//                    }
//                }
//                @SneakyThrows
//                @Override
//                public void onSuccess(SendResult<String, String> stringStringSendResult) {
//                    if(successCallback != null)
//                        successCallback.callBack();
//                    else
//                        log.info("数据发送成功");
//                }
//            });
//        }catch (Exception e) {
//            if(failCalback != null)
//                failCalback.callBack();
//            e.printStackTrace();
//        }
//
////        if("base".equals(topicLevels[3])) {
////            // 基础通信主题
////
////        }else if("sys".equals(topicLevels[3])) {
////            // 系统通信主题
////
////        }else if("custom".equals(topicLevels[3])) {
////            // 自定义通信主题
////
////        }
//
//
//    }
//
//    /**
//     * 数据保存到数据库中
//     * @param clientId
//     * @param topic
//     * @param payload
//     * @param qos
//     */
//    public void messageSaveToDB(String clientId, String topic, String payload, int qos) {
//        MessageKafkaOffline message = MessageKafkaOffline.builder()
//                .clientId(clientId)
//                .topic(topic)
//                .payload(payload)
//                .qos(qos)
//                .build();
//        message.insert();
//    }
}
