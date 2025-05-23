package center.helloworld.transport.mqtt.server.broker;

import center.helloworld.transport.mqtt.server.handler.MqttBrokerChannelInitializer;
import center.helloworld.transport.mqtt.server.properties.MqttConfigProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @create 2024/3/18
 * @note Mqtt Broker
 */

@Slf4j
@Component
public class MqttBroker implements CommandLineRunner {

    @Autowired
    private MqttConfigProperty mqttConfigProperty;

    @Autowired
    private MqttBrokerChannelInitializer mqttBrokerChannelInitializer;

    private NioEventLoopGroup bossGroup;

    private NioEventLoopGroup workGroup;

    @Override
    public void run(String... args) throws Exception {
        mqttBrokerStart();
    }

    private void mqttBrokerStart() throws InterruptedException {
        try {
            bossGroup = new NioEventLoopGroup(2);
            workGroup = new NioEventLoopGroup(4);

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup);
            bootstrap.channel(NioServerSocketChannel.class);

            bootstrap.option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .option(ChannelOption.SO_RCVBUF, 2048);

            bootstrap.childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);


            bootstrap.childHandler(mqttBrokerChannelInitializer);
            ChannelFuture f = bootstrap.bind(mqttConfigProperty.getBrokerPort()).sync();
            if (f.isSuccess()) {
                log.info("Mqtt Broker start success port：{}", mqttConfigProperty.getBrokerPort());
                f.channel().closeFuture().sync();
            } else {
                log.warn("Mqtt Broker start error port：{}", mqttConfigProperty.getBrokerPort());
            }
        }catch (Exception e) {
            log.error("Mqtt Broker 启动失败 ~~");
            throw e;
        }finally {
            if(bossGroup != null) {
                bossGroup.shutdownGracefully();
            }
            if(workGroup != null) {
                bossGroup.shutdownGracefully();
            }
        }
    }
}
