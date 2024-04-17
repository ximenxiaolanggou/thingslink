package center.helloworld.transport.mqtt.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
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
    private MqttServerMessageHandler_Bak mqttServerMessageHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast("log", new LoggingHandler(LogLevel.DEBUG));
        channelPipeline.addLast("encoder", new MqttCodec());
        channelPipeline.addLast("handler", new MqttMessageHandler());
        channelPipeline.addLast(mqttBrokerHeartHandler);
    }
}
