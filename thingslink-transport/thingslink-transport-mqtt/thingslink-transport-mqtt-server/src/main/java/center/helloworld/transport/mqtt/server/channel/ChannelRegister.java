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
     * @param clientId 唯一标识 客户端ID
     * @param channel 通道
     */
    void registerChannel(String clientId, Channel channel);

    /**
     * 移除通道
     * @param clientId 唯一标识 客户端ID
     */
    void removeChannel(String clientId);

    /**
     * 获取通道
     * @param clientId 唯一标识 客户端ID
     * @return
     */
    Channel getChannel(String clientId);

    /**
     * 清空
     */
    void clear();

    /**
     * 打印信息
     */
    void print();
}
