package center.helloworld.transport.mqtt.server.broker;

import center.helloworld.transport.mqtt.server.handler.MqttBrokerChannelInitializer;
import center.helloworld.transport.mqtt.server.session.SessionStore;
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
    private MqttBrokerChannelInitializer mqttBrokerChannelInitializer;

    @Autowired
    private SessionStore sessionDao;

    private NioEventLoopGroup bossGroup;

    private NioEventLoopGroup workGroup;


    @Override
    public void run(String... args) throws Exception {
        // 1. 清除脏数据
//        List<Session> dirtySession = sessionDao.findAllCleanSession();
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
                    .option(ChannelOption.SO_RCVBUF, 10);

            bootstrap.childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);


            bootstrap.childHandler(mqttBrokerChannelInitializer);
            ChannelFuture f = bootstrap.bind(1883).sync();
            if (f.isSuccess()) {
                log.info("Mqtt Broker start success port：{}", 1883);
                f.channel().closeFuture().sync();
            } else {
                log.warn("Mqtt Broker start error port：{}", 1883);
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
