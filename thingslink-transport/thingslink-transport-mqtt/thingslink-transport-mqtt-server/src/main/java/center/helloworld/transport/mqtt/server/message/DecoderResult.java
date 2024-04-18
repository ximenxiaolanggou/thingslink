package center.helloworld.transport.mqtt.server.message;

/**
 * @author zhishun.cai
 * @create 2024/4/15
 * @note 解码结果
 */
public enum DecoderResult {

    SUCCESS(true),
    ERROR(false);

    private boolean value;

    DecoderResult(boolean value) {
        this.value = value;
    }
}
