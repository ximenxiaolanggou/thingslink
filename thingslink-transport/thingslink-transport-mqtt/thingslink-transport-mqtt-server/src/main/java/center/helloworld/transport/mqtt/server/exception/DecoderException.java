package center.helloworld.transport.mqtt.server.exception;

/**
 * @author zhishun.cai
 * @create 2024/4/18
 * @note 解析异常
 */
public class DecoderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DecoderException() {
    }

    public DecoderException(String message) {
        super(message);
    }

    public DecoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecoderException(Throwable cause) {
        super(cause);
    }

    public DecoderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
