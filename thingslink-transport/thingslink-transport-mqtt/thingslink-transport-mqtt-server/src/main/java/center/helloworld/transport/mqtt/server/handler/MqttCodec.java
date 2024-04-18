package center.helloworld.transport.mqtt.server.handler;

import center.helloworld.transport.mqtt.server.codec.*;
import center.helloworld.transport.mqtt.server.codec.message.MqttMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhishun.cai
 * @create 2024/4/15
 * @note MQTT 编解码器
 */

@Slf4j
@Component
public class MqttCodec extends ByteToMessageCodec<MqttMessage> {

    @Autowired
    private MqttDecoder mqttDecoder;

    /**
     * 编码
     * @param channelHandlerContext
     * @param mqttMessage
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage, ByteBuf out) throws Exception {
        System.out.println("123");
    }

    /**
     * 解码
     * @param channelHandlerContext
     * @param in
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        MqttMessage mqttMessage = mqttDecoder.decode(in);
        if(mqttMessage != null) {
            list.add(mqttMessage);
        }
    }

}
