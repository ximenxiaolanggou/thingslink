package center.helloworld.transport.mqtt.server.dup.impl;

import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhishun.cai
 * @create 2023/7/27
 * @note PUBREL重发消息存储
 */
@NoArgsConstructor
public class DupPubRelMessageStore implements Serializable {

    private static final long serialVersionUID = -4111642532532950980L;

    private String clientId;

    private int messageId;

    public String getClientId() {
        return clientId;
    }

    public DupPubRelMessageStore setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public int getMessageId() {
        return messageId;
    }

    public DupPubRelMessageStore setMessageId(int messageId) {
        this.messageId = messageId;
        return this;
    }

}
