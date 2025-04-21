package center.helloworld.transport.mqtt.server.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @create 2024/3/18
 * @note Mqtt Broker 心跳处理
 */

@Slf4j
@ChannelHandler.Sharable
@Component
public class MqttBrokerHeartHandler extends ChannelDuplexHandler {

    public static final String HANDLER_NAME = "heartHandler";

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        // 读事件
        if(event.state() == IdleState.READER_IDLE) {
            log.info("{}，未收到数据，进行关闭操作", ctx.channel().id().asLongText());
            ctx.channel().close();
        }else if(event.state() == IdleState.WRITER_IDLE) {
            System.out.printf("WRITER_IDLE");
        }
    }
}
