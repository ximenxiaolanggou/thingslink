package center.helloworld.transport.mqtt.server.codec;

import center.helloworld.transport.mqtt.server.codec.fixedheader.MqttControlPacketTypeFactory;
import center.helloworld.transport.mqtt.server.codec.fixedheader.MqttControlPacketTypeFlagFactory;
import center.helloworld.transport.mqtt.server.codec.fixedheader.MqttFixedHeader;
import center.helloworld.transport.mqtt.server.codec.message.MqttMessage;
import center.helloworld.transport.mqtt.server.codec.message.MqttMessageFactory;
import center.helloworld.transport.mqtt.server.codec.payload.MqttConnectPayload;
import center.helloworld.transport.mqtt.server.codec.payload.MqttWillProperties;
import center.helloworld.transport.mqtt.server.codec.variableheader.MqttConnectProperties;
import center.helloworld.transport.mqtt.server.codec.variableheader.MqttConnectVariableHeader;
import center.helloworld.transport.mqtt.server.exception.DecoderException;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhishun.cai
 * @create 2024/4/18
 * @note MQTT 解码
 */

@Slf4j
@Component
public class MqttDecoder {

    /**
     * 解码
     * @param in
     * @return
     */
    public MqttMessage decode(ByteBuf in) {
        int readableLength = in.readableBytes();

        // 1. 首先判断缓存中协议头是否读完（MQTT协议头为2字节）
        if(readableLength < MqttConst.MQTT_PROTOCOL_LENGTH) {
            return null;
        }

        // 2. 判断数据包是否完整（半包问题）
        List<Byte> remainingLengthBytes = null;
        try {
            remainingLengthBytes = getRemainingLengthBytesUnread(in);
        }catch (Exception e) {

        }

        // 2. 解析 FixedHeader 2~5 个字节
        try {
//            MqttFixedHeader mqttFixedHeader = decodeFixed();
        }catch (Exception e) {
            return MqttMessageFactory.newInvalidMessage(e);
        }

        System.out.println("buffer => " + in.capacity() + " - " + in.readableBytes());
//        List<Byte> remainingLengthBytes = getRemainingLengthBytesUnread(in);
        if(computeRemainingLength(remainingLengthBytes) > (in.readableBytes() - 1 - remainingLengthBytes.size())) {
            // 数据不完整
            return null;
        }

        MqttMessage mqttMessage = new MqttMessage();
        try{
            MqttFixedHeader mqttFixedHeader = new MqttFixedHeader();
            // 事件类型
            byte controlPackage = in.readByte();
            // 控制包类型
            mqttFixedHeader.setControlPacketType(MqttControlPacketTypeFactory.getPackageType(controlPackage >> 4));
            // 控制包类型标志
            mqttFixedHeader.setControlPacketTypeFlag(MqttControlPacketTypeFlagFactory.getControlPacketTypeFlag(mqttFixedHeader.getControlPacketType(), controlPackage));
            // 获取剩余数据包长度
            int remainingLength = getRemainingLength(in);
            log.info("剩余长度为：{}", remainingLength);
            mqttFixedHeader.setRemainingLength(remainingLength);

            System.out.println("固定头 => " + mqttFixedHeader);

            mqttMessage.setMqttFixedHeader(mqttFixedHeader);
            ByteBuf remainingBuf = in.readBytes(mqttFixedHeader.getRemainingLength());
            // ===== 可变头 ====
            System.out.println(in.readableBytes());
            switch (mqttFixedHeader.getControlPacketType()) {
                case CONNECT -> {
                    remainingBuf.readByte();// 00  处于固定报文头和可变报文头之间（对于CONNECT控制报文无意义）
                    MqttConnectVariableHeader connectVariableHeader = new MqttConnectVariableHeader();
                    // 协议名长度
                    byte protocolNameLen = remainingBuf.readByte();
                    connectVariableHeader.setProtocolNameLen(protocolNameLen);
                    // 协议名
                    connectVariableHeader.setProtocolName(remainingBuf.readBytes(protocolNameLen).toString(StandardCharsets.UTF_8));
                    // 协议版本
                    connectVariableHeader.setVersion(remainingBuf.readByte());
                    // 连接标志
                    Short connectFlag = remainingBuf.readUnsignedByte();
                    connectVariableHeader.setUsernameFlag(((connectFlag >> 7) & 1) == 1);
                    connectVariableHeader.setPasswordFlag(((connectFlag >> 6) & 1) == 1);
                    connectVariableHeader.setWillRetain(((connectFlag >> 5) & 1) == 1);
                    connectVariableHeader.setWillQos((connectFlag >> 3) & 3);
                    connectVariableHeader.setWillFlag(((connectFlag >> 2) & 1) == 1);
                    connectVariableHeader.setCleanStart(((connectFlag >> 1) & 1) == 1);
                    connectVariableHeader.setKeepAliveTimeSeconds((int)remainingBuf.readChar());

                    // 连接属性
                    byte connectPropertiesLen = remainingBuf.readByte();
                    ByteBuf connectPropertiesData = remainingBuf.readBytes(connectPropertiesLen);
                    MqttConnectProperties mqttConnectProperties = MqttConnectProperties.mqttConnectPropertiesresolver(connectPropertiesData);
                    connectVariableHeader.setMqttConnectProperties(mqttConnectProperties);
                    mqttMessage.setVariableHeader(connectVariableHeader);
                    System.out.println("可变头 => " + connectVariableHeader);

                    // ===== 载荷 ====
                    ByteBuf payloadBuf = remainingBuf.readBytes(remainingBuf.readableBytes());
                    MqttConnectPayload connectPayload = new MqttConnectPayload();
                    mqttMessage.setPayload(connectPayload);
                    // 客户端ID
                    char clientIdLength = payloadBuf.readChar();
                    String clientId = payloadBuf.readBytes(clientIdLength).toString(StandardCharsets.UTF_8);
                    connectPayload.setClientId(clientId);
                    if(connectVariableHeader.isWillFlag()) {
                        MqttWillProperties willProperties = new MqttWillProperties();
                        connectPayload.setWillProperties(willProperties);
                        int willPropertyLength = (int)payloadBuf.readByte();
                        willProperties.setPropertyLength(willPropertyLength);
                        ByteBuf willPropertyBuf = payloadBuf.readBytes(willPropertyLength);

                        // 解析 遗嘱属性 Will Properties
                        while (willPropertyBuf.readableBytes() > 0) {
                            byte payloadFormatIndicator = willPropertyBuf.readByte();
                            MqttWillProperties.MqttWillPropertyType willPropertyType = MqttWillProperties.willPropertyTypeMap.get((int) payloadFormatIndicator);
                            switch (willPropertyType) {
                                case WILL_DELAY_INTERVAL -> willProperties.setWillDelayInterval(willPropertyBuf.readInt());
                                case MESSAGE_EXPIRY_INTERVAL -> willProperties.setMessageExpireInterval(willPropertyBuf.readInt());
                                case CONTENT_TYPE -> willProperties.setContentType(willPropertyBuf.readBytes(willPropertyBuf.readChar()).toString(StandardCharsets.UTF_8));
                                case RESPONSE_TOPIC -> willProperties.setResponesType(willPropertyBuf.readBytes(willPropertyBuf.readChar()).toString(StandardCharsets.UTF_8));
                                case CORRELATION_DATA -> willProperties.setCorrelationData(willPropertyBuf.readBytes(willPropertyBuf.readChar()).toString(StandardCharsets.UTF_8));
                                case USER_PROPERTY -> willProperties.setUserProperty(willPropertyBuf.readBytes(willPropertyBuf.readableBytes()).toString(StandardCharsets.UTF_8));
                                case PAYLOAD_FORMAT_INDICATOR -> willProperties.setPayloadFormatIndicator((int)willPropertyBuf.readByte());
                            }
                        }
                        // 遗嘱主题 Will Topic
                        connectPayload.setWillTopic(payloadBuf.readBytes(payloadBuf.readChar()).toString(StandardCharsets.UTF_8));
                        // 遗嘱载荷 Will Payload
                        connectPayload.setWillPayloadData(payloadBuf.readBytes(payloadBuf.readChar()).toString(StandardCharsets.UTF_8));
                    }

                    // 用户名 User Name
                    if(connectVariableHeader.isUsernameFlag()) {
                        connectPayload.setUsername(payloadBuf.readBytes(payloadBuf.readChar()).toString(StandardCharsets.UTF_8));
                    }

                    // 密码 Password
                    if(connectVariableHeader.isUsernameFlag()) {
                        connectPayload.setPassword(payloadBuf.readBytes(payloadBuf.readChar()).toString(StandardCharsets.UTF_8));
                    }
                }
                default -> throw new RuntimeException("");
            };
        }catch (Exception e) {
            mqttMessage.setDecoderResult(DecoderResult.FAILURE(e));
        }
        mqttMessage.setDecoderResult(DecoderResult.SUCCESS());
        System.out.println("Connect 解析完成 : " + mqttMessage);
        return mqttMessage;
    }


    /**
     * 获取数据包长度字节
     * @param in
     * @return
     */
    private List<Byte> getRemainingLengthBytesUnread(ByteBuf in) {
        List<Byte> cache = new ArrayList();
        int readableBytes = in.readableBytes();
        // 获取记录长度的字节集合
        in.readByte();
        // 允许携带的数据包长度为4个字节（高位为进位标志）
        for (int readIndex = 1; readIndex <= 4 ; readIndex++) {
            if(readableBytes < readIndex) {
                // 出现半包情况，返回null，等待后续数据包
                return null;
            }
            byte b = in.getByte(readIndex);
            cache.add(b);
            if((b & 0x80) == 0) {
                // 后面字节不是记录剩余长度
                break;
            }
        }
        // 读取四个字节，但是最后字节表示后续还有数据 => 异常
        if(cache.size() == 4 && (cache.get(4) & 0x80) == 0) {
            throw new DecoderException("remaining length exceeds 4 bytes");
        }
        return cache;
    }

    /**
     * 计算剩余长度
     * @param cache
     * @return
     */
    public int computeRemainingLength(List<Byte> cache) {
        /**
         * 计算长度
         * 假设消息的剩余长度是300，它需要用两个字节来表示。300的二进制表示是 100101100，所以剩余长度的编码应该是 10101100 00000010。
         * 在这个编码中，第一个字节的最高位是1，表示后续还有剩余长度信息，低7位是 1010110，第二个字节的最高位是0，表示这是最后一个字节，低7位是 00000010。
         * 将这两个字节的低7位拼接起来得到最终的剩余长度值为 300。
         */
        if(cache.size() == 1) {
            // 一个字节表示长度
            return cache.get(0);
        }else {
            // 多个字节表示长度
            int len = 0;
            for (int i = 0; i < cache.size(); i++) {
                byte lenItem = cache.get(i);
                lenItem &= 0x7f;
                len |= (lenItem << (i * 7));
            }
            return len;
        }
    }

    /**
     * 获取数据包长度
     * @param in
     * @return
     */
    private int getRemainingLength(ByteBuf in) {
        List<Byte> cache = new ArrayList();
        // 获取记录长度的字节集合， TODO 允许携带的数据包长度为4个字节（高位为进位标志）
        for (;;) {
            byte b = in.readByte();
            cache.add(b);
            if((b & 0x80) == 0) {
                // 后面字节不是记录剩余长度
                break;
            }
        }
        /**
         * 计算长度
         * 假设消息的剩余长度是300，它需要用两个字节来表示。300的二进制表示是 100101100，所以剩余长度的编码应该是 10101100 00000010。
         * 在这个编码中，第一个字节的最高位是1，表示后续还有剩余长度信息，低7位是 1010110，第二个字节的最高位是0，表示这是最后一个字节，低7位是 00000010。
         * 将这两个字节的低7位拼接起来得到最终的剩余长度值为 300。
         */
        if(cache.size() == 1) {
            // 一个字节表示长度
            return cache.get(0);
        }else {
            // 多个字节表示长度
            int len = 0;
            for (int i = 0; i < cache.size(); i++) {
                byte lenItem = cache.get(i);
                lenItem &= 0x7f;
                len |= (lenItem << (i * 7));
            }
            return len;
        }
    }
}
