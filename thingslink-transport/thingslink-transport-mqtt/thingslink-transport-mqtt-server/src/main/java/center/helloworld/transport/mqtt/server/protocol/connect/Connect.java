package center.helloworld.transport.mqtt.server.protocol.connect;

import center.helloworld.transport.mqtt.server.session.entity.Session;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhishun.cai
 * @date 2024/3/18
 * @note 连接处理
 */
public interface Connect {

    /**
     * 校验解析参数结果
     * @param channel
     * @param msg
     * @return
     */
    boolean validateDecoderResult(Channel channel, MqttConnectMessage msg);



    /**
     * 设备认证
     * @param channel
     * @param message
     */
    boolean deviceAuth(Channel channel, MqttConnectMessage message);

    /**
     * 清除会话处理
     * @param channel
     * @param message
     */
    boolean cleanSessionHandle(Channel channel, MqttConnectMessage message);

    /**
     * 心跳处理
     * @param channel
     * @param msg
     * @return
     */
    int heartHandle(Channel channel, MqttConnectMessage msg);

    /**
     * Willing 消息处理
     * @param channel
     * @param msg
     */
    Session willMessageHandle(Channel channel, MqttConnectMessage msg);

    /**
     * 连接ACK消息处理
     * @param channel
     * @param msg
     */
    void connAckMessageHandle(Channel channel, MqttConnectMessage msg, boolean sessionPresent);

    /**
     * 连接处理
     * @param channel
     * @param msg
     */
     default void process(Channel channel, MqttConnectMessage msg){
         // 解码校验
         if(!validateDecoderResult(channel, msg)) {
             return;
         }

         // 设备认证校验
         if(!deviceAuth(channel, msg)) {
             return;
         }

         // 清除会话处理
         boolean sessionPresent = cleanSessionHandle(channel, msg);

         // 心跳处理
         heartHandle(channel, msg);

         // 遗言消息处理
         Session session = willMessageHandle(channel, msg);
         if(session == null) {
             return;
         }

         // 连接ACK
         connAckMessageHandle(channel, msg, sessionPresent);

         // TODO 消息自订阅

         // TODO DUP消息处理

         // TODO 离线消息处理

         // TODO 广播连接事件

     }

}
