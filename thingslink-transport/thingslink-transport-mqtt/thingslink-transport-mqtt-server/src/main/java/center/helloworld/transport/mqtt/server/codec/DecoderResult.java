package center.helloworld.transport.mqtt.server.codec;

import java.util.Objects;

/**
 * @author zhishun.cai
 * @create 2024/4/15
 * @note 解码结果
 */
public class DecoderResult {

    private final boolean success;
    private final Throwable cause;

    public DecoderResult() {
        this(true, null);
    }

    public DecoderResult(Throwable cause) {
        this(false, Objects.requireNonNull(cause, "cause is null"));
    }

    public DecoderResult(boolean success, Throwable cause) {
        this.success = success;
        this.cause = cause;
    }

    public static DecoderResult FAILURE(Throwable cause) {
        return new DecoderResult(cause);
    }

    public static DecoderResult SUCCESS() {
        return new DecoderResult();
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailure() {
        return !success;
    }

    public Throwable getCause() {
        return cause;
    }
}
