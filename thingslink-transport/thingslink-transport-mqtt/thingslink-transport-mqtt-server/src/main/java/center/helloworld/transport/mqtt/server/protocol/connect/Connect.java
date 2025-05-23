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
     * 重复连接处理
     * @param channel
     * @param message
     */
    boolean multiConnectHandle(Channel channel, MqttConnectMessage message);

    /**
     * 心跳处理
     * @param channel
     * @param msg
     * @return
     */
    int heartHandle(Channel channel, MqttConnectMessage msg);


    /**
     * MQTT处理器
     * @param channel
     * @param msg
     * @return
     */
    void pipelineAddHadnler(Channel channel, MqttConnectMessage msg);

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
    void responseAckMessageHandle(Channel channel, MqttConnectMessage msg);

    /**
     * 连接处理
     * @param channel
     * @param msg
     */
     default void process(Channel channel, MqttConnectMessage msg){
         // 1、解码校验
         if(!validateDecoderResult(channel, msg)) {
             return;
         }

         // 2、设备认证校验
         if(!deviceAuth(channel, msg)) {
             return;
         }

         // 3、重复连接
         multiConnectHandle(channel, msg);

         // 4、添加心跳处理器
         heartHandle(channel, msg);

         // 4、添加处理器
         pipelineAddHadnler(channel, msg);

         // 5、遗言消息处理
         Session session = willMessageHandle(channel, msg);
         if(session == null) {
             return;
         }

         // 6、响应ACK
         responseAckMessageHandle(channel, msg);

         // TODO 消息自订阅

         // TODO DUP消息处理

         // TODO 离线消息处理

         // TODO 广播连接事件

     }

}
