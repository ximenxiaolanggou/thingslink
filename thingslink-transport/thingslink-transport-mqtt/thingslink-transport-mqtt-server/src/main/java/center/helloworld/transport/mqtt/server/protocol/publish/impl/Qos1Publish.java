package center.helloworld.transport.mqtt.server.protocol.publish.impl;

import center.helloworld.transport.mqtt.server.dup.dao.DupPubRecMessageDao;
import center.helloworld.transport.mqtt.server.message.pojo.Message;
import center.helloworld.transport.mqtt.server.protocol.publish.Publish;
import center.helloworld.transport.mqtt.server.retain.dao.RetainMessageDao;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note Qos1 消息
 */
@Component("qos1Publish")
public class Qos1Publish implements Publish {

    @Resource
    private RetainMessageDao retainMessageDao;

    @Resource
    private DupPubRecMessageDao pubRecMessageDao;


    /**
     * retain消息处理
     * @param channel
     * @param msg
     */
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


    /**
     * 消息确认 Qos为1时
     * @param channel
     * @param message
     */
    @Override
    public void pubAck(Channel channel, MqttPublishMessage message){
        MqttFixedHeader pubAckFixedHeader = new MqttFixedHeader(MqttMessageType.PUBACK, false,
                MqttQoS.AT_LEAST_ONCE, false, 0);
        MqttMessageIdVariableHeader variableHeader =MqttMessageIdVariableHeader.from(message.variableHeader().packetId()) ;//MqttMessageIdVariableHeader.from(mqttMessage.variableHeader().packetId());
        MqttPubAckMessage    pubAck=  new MqttPubAckMessage(pubAckFixedHeader, variableHeader);
        channel.writeAndFlush(pubAck);
    }
}
