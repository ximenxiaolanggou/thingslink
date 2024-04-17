package center.helloworld.transport.mqtt.server.channel;

import io.netty.channel.Channel;

/**
 * @author zhishun.cai
 * @create 2024/4/12
 * @note 通道注册器
 */
public interface ChannelRegister {

    /**
     * 注册通道
     * @param sessionId 唯一标识 会话ID
     * @param channel 通道
     */
    void registerChannel(String sessionId, Channel channel);

    /**
     * 移除通道
     * @param sessionId 唯一标识 会话ID
     */
    void removeChannel(String sessionId);

    /**
     * 获取通道
     * @param sessionId 唯一标识 会话ID
     * @return
     */
    Channel getChannel(String sessionId);

    /**
     * 清空
     */
    void clear();

    /**
     * 打印信息
     */
    void print();
}
