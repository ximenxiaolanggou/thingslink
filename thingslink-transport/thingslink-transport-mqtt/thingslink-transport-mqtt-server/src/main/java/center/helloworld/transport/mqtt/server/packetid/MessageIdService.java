package center.helloworld.transport.mqtt.server.packetid;

/**
 * @author zhishun.cai
 * @create 2024/4/28
 * @note
 */
public interface MessageIdService {

    String MESSAGE_ID_PREFIX = "thingslink_message_id_";

    /**
     * 获取报文标识符
     */
    int getNextMessageId(String clientId);
}
