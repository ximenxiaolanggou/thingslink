package center.helloworld.transport.mqtt.server.protocol.publish.impl;

import center.helloworld.transport.mqtt.server.message.pojo.Message;
import center.helloworld.transport.mqtt.server.protocol.publish.Publish;
import center.helloworld.transport.mqtt.server.retain.dao.RetainMessageDao;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhishun.cai
 * @date 2024/3/20
 * @note Qos0 消息
 */
@Component("qos0Publish")
public class Qos0Publish implements Publish {

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
}
