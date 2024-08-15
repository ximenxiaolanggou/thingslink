package center.helloworld.transport.mqtt.server.protocol.publish.impl;

import center.helloworld.transport.mqtt.server.dup.dao.DupPubRecMessageDao;
import center.helloworld.transport.mqtt.server.message.pojo.Message;
import center.helloworld.transport.mqtt.server.protocol.publish.Publish;
import center.helloworld.transport.mqtt.server.protocol.publish.dao.PublishMessageDao;
import center.helloworld.transport.mqtt.server.retain.dao.RetainMessageDao;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader.from;
import static io.netty.handler.codec.mqtt.MqttQoS.AT_MOST_ONCE;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note Qos2 消息
 */
@Component("qos2Publish")
public class Qos2Publish implements Publish {

    @Resource
    private PublishMessageDao puplishMessageDao;

    @Resource
    private DupPubRecMessageDao pubRecMessageDao;

    @Resource
    private RetainMessageDao retainMessageDao;


    @Override
    public void storeRetainMessage(Channel channel, MqttPublishMessage msg) {
        boolean retain = msg.fixedHeader().isRetain();
        if(retain) {
            byte[] messageBytes = new byte[msg.payload().readableBytes()];
            msg.payload().getBytes(msg.payload().readerIndex(), messageBytes);
            if (messageBytes.length == 0) {
                retainMessageDao.remove(msg.variableHeader().topicName());
            } else {
                Message retainMsg = new Message();
                retainMsg.setTopic(msg.variableHeader().topicName());
                retainMsg.setMqttQoS(msg.fixedHeader().qosLevel().value());
                retainMsg.setMessageBytes(messageBytes);
                retainMsg.setTopic(msg.variableHeader().topicName());
                retainMessageDao.put(msg.variableHeader().topicName(), retainMsg);
            }
        }
    }

    @Override
    public boolean storagePublishMessage(Channel channel, MqttPublishMessage msg) {
        // 获取客户端ID
        //作为接收者
        String clientId = (String) channel.attr(AttributeKey.valueOf("clientIdentifier")).get();
        int packetId = msg.variableHeader().packetId();
        String topic = msg.variableHeader().topicName();
        MqttQoS mqttQoS = msg.fixedHeader().qosLevel();
        byte[] messageBytes = new byte[msg.payload().readableBytes()];
        msg.payload().getBytes(msg.payload().readerIndex(), messageBytes);

        // 判断是否为重复消息
        boolean repeatMsg = puplishMessageDao.exist(String.valueOf(packetId));
        if(!repeatMsg) {
            // 1、 存储消息 2、 响应PUBREC
            // 创建消息对象，并存储
            Message pubMsg = new Message(clientId, topic, mqttQoS.value(), packetId, messageBytes);
            puplishMessageDao.put(String.valueOf(packetId), pubMsg);
        }
        return repeatMsg;
    }


    @Override
    public void pubRec(Channel channel, MqttPublishMessage msg) {
        int packetId = msg.variableHeader().packetId();
        String clientId = (String) channel.attr(AttributeKey.valueOf("clientIdentifier")).get();
        // 判断是否是重复消息
        Message pubRecMsg = pubRecMessageDao.get(String.valueOf(packetId));

        // 创建pubRec报文
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBREC, false, AT_MOST_ONCE,
                false, 0);
        MqttPubAckMessage pubRecMessage = new MqttPubAckMessage(fixedHeader, from(packetId));
        if(null == pubRecMsg) {
            // 不存在pubRec消息 创建 等待确认报文并添加到队列（待后续确认）
            pubRecMsg = new Message();
            pubRecMsg.setMessageId(packetId);
            pubRecMsg.setClientId(clientId);
            pubRecMessageDao.put(String.valueOf(packetId),pubRecMsg);
        }
        //发送
        channel.writeAndFlush(pubRecMessage);
    }

    /**
     * 尝试重发 PubRec
     * @param channel
     * @param msg
     */
    @Override
    public void retryPubRec(Channel channel, MqttPublishMessage msg) {
        int packetId = msg.variableHeader().packetId();
        channel.eventLoop().scheduleAtFixedRate(()->{
            //重发rec报文
            Message message = pubRecMessageDao.get(String.valueOf(packetId));
            // 创建pubRec报文
            MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBREC, true, AT_MOST_ONCE,
                    false, 0);
            MqttPubAckMessage pubRecMessage = new MqttPubAckMessage(fixedHeader, from(packetId));
            Optional.ofNullable(message).ifPresent(e->{
                channel.writeAndFlush(pubRecMessage);
            });
        },2,2, TimeUnit.SECONDS);
    }
}
