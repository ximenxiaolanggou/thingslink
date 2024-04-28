package center.helloworld.transport.mqtt.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @create 2024/3/18
 * @note Mt
 */
@Component
public class MqttBrokerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private MqttBrokerHeartHandler mqttBrokerHeartHandler;

    @Autowired
    private MqttServerMessageHandler mqttServerMessageHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        // 设置读写空闲超时时间 TODO 后续根据设备连接上来携带心跳时间进行替换
//        channelPipeline.addLast("idle",new IdleStateHandler(600, 0, 0));
        channelPipeline.addLast("encoder", MqttEncoder.INSTANCE);
        // 设置消息字节数 20KB (默认)
        channelPipeline.addLast("decoder", new MqttDecoder(1024 * 20));
        channelPipeline.addLast(mqttBrokerHeartHandler);
        channelPipeline.addLast(mqttServerMessageHandler);
    }
}
