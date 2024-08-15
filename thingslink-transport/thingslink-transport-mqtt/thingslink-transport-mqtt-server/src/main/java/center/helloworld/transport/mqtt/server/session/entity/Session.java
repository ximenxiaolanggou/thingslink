package center.helloworld.transport.mqtt.server.session.entity;

import center.helloworld.transport.mqtt.server.message.MqttWillMessage;
import io.netty.channel.ChannelId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.message.Message;

import java.util.UUID;

/**
 * @author zhishun.cai
 * @date 2024/3/18
 * @note 会话
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {

    private String sessionId = UUID.randomUUID().toString();

    private String clientId;

    private ChannelId channelId;

    private boolean cleanSession;

    private MqttWillMessage willMessage;

    private int expire;

    /**
     * cleanSession 为 true时，session 在线是为true  不在线为false
     */
    private boolean active = true;

    public Session(String clientId, boolean cleanSession, MqttWillMessage willMessage, int expire) {
        this.clientId = clientId;
        this.cleanSession = cleanSession;
        this.willMessage = willMessage;
        this.expire = expire;
    }
}
