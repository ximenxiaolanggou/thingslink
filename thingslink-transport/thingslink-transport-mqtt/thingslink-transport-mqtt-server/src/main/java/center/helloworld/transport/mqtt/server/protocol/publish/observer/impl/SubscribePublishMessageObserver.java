package center.helloworld.transport.mqtt.server.protocol.publish.observer.impl;

import center.helloworld.transport.mqtt.server.channel.ChannelRegister;
import center.helloworld.transport.mqtt.server.dup.dao.DupPublishMessageDao;
import center.helloworld.transport.mqtt.server.message.pojo.Message;
import center.helloworld.transport.mqtt.server.packetid.MessageIdService;
import center.helloworld.transport.mqtt.server.protocol.publish.observer.PublishMessageCallBack;
import center.helloworld.transport.mqtt.server.protocol.publish.observer.PublishMessageObserver;
import center.helloworld.transport.mqtt.server.protocol.subscribe.entity.SubscribeStore;
import center.helloworld.transport.mqtt.server.protocol.subscribe.service.SubscribeService;
import center.helloworld.transport.mqtt.server.session.SessionStore;
import center.helloworld.transport.mqtt.server.session.entity.Session;
import cn.hutool.core.collection.CollectionUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * @author zhishun.cai
 * @create 2023/8/3
 * @note 主题订阅
 */
@Slf4j
@Service
public class SubscribePublishMessageObserver implements PublishMessageObserver {

    @Autowired
    private SubscribeService subscribeStoreService;

    @Autowired
    private SessionStore sessionDao;

    @Resource
    private DupPublishMessageDao dupPublishMessageStore;

    @Autowired
    private MessageIdService messageIdService;

    @Autowired
    private ChannelRegister channelRegister;

//    @Autowired
//    private MessageSubOfflineService messageOfflineService;

    /**
     * 触发事件
     * @param channel
     * @param msg
     */
    @Override
    public void onEvent(Channel channel, MqttPublishMessage msg, PublishMessageCallBack successCallback, PublishMessageCallBack failCalback) {

        byte[] messageBytes = new byte[msg.payload().readableBytes()];
        msg.payload().getBytes(msg.payload().readerIndex(), messageBytes);
        String topicName = msg.variableHeader().topicName();
        MqttQoS mqttQoS = msg.fixedHeader().qosLevel();
        int packetId = msg.variableHeader().packetId();
        boolean dup = msg.fixedHeader().isDup();

        // 获取订阅topic下的订阅存储集合
        List<SubscribeStore> subscribeStores = subscribeStoreService.search(topicName);
        if(CollectionUtil.isNotEmpty(subscribeStores)) {
            subscribeStores.forEach(subscribeStore -> {
                Session session = sessionDao.sessionByClientId(subscribeStore.getClientId());
                // TODO 判断会话状态 1、存在并且在线 2、存在但不在线 3、不存在
                // 存在会话
                if (session != null) {
                    String clientId = (String) channel.attr(AttributeKey.valueOf("clientIdentifier")).get();
                    // 会话激活状态
                    if(session.isActive()) {
                        // 将消息存储
                        // 生成MessageId 需要判断是否占用情况
                        int messageId = packetId;
                        if(!dup) {
                            // 如果不是重复消息，需要生产消息ID
                            messageId =  messageIdService.getNextMessageId(clientId);
                            for (;;) {
                                if(!dupPublishMessageStore.exist(String.valueOf(messageId)))
                                    break;
                                messageId =  messageIdService.getNextMessageId(clientId);
                            }
                        }

                        Message message = new Message(
                                clientId,
                                topicName,
                                mqttQoS.value(),
                                messageId,
                                messageBytes

                        );
                        // Qos为非0，及1、2时需要对数据做存储
                        if(subscribeStore.getMqttQoS() != 0) {
                            dupPublishMessageStore.put(String.valueOf(message.getMessageId()), message);
                        }

                        MqttPublishMessage publishMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                                new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.valueOf(subscribeStore.getMqttQoS()), false, 0),
                                new MqttPublishVariableHeader(topicName,  message.getMessageId()), Unpooled.buffer().writeBytes(messageBytes));
                        log.debug("PUBLISH - clientId: {}, topic: {}, Qos: {}, messageId: {}", subscribeStore.getClientId(), topicName, subscribeStore.getMqttQoS(), messageId);
                        Session sessionStore = sessionDao.sessionByClientId(subscribeStore.getClientId());
                        ChannelId channelId = sessionStore.getChannelId();
                        if (channelId != null) {
                            Channel targetChannel = channelRegister.getChannel(sessionStore.getSessionId());
                            if (targetChannel != null) {
                                targetChannel.writeAndFlush(publishMessage);
                                // 触发重发机制
                                if(subscribeStore.getMqttQoS() != 0) {
                                    triggerRetry(channel, publishMessage, successCallback);
                                }
                            };
                        }
                    }else {
                        // 会话处于离线状态，需要将消息持久化到数据库中（需要注意消息顺序）
                        // 不提供callback默认是存储到MySQL中，也可以执行编写callback来覆盖默认逻辑
                        if(failCalback != null) {
                            failCalback.callBack();
                        }else {
                            // TODO 执行默认逻辑
//                            MessageSubOffline messageOffline = MessageSubOffline.builder()
//                                    .clientId(subscribeStore.getClientId())
//                                    .topic(topicName)
//                                    .qos(mqttQoS.value())
//                                    .payload(new String(messageBytes))
//                                    .build();
//
//                            messageOfflineService.save(messageOffline);
                        }
                    }
                }
            });
        }
    }

    /**
     * 重发机制
     * @param channel
     * @param publishMessage
     */
    private void triggerRetry(Channel channel, MqttPublishMessage publishMessage, PublishMessageCallBack successCallback) {

        channel.eventLoop().scheduleAtFixedRate(()->{
            //重发publish报文
            Message message = dupPublishMessageStore.get(String.valueOf(publishMessage.variableHeader().packetId()));
            Optional.ofNullable(message).ifPresent(e->{
                channel.writeAndFlush(publishMessage);
                successCallback.callBack();
            });
        },2,2, TimeUnit.SECONDS);
    }
}
