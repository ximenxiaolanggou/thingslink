package center.helloworld.transport.mqtt.server.utils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author zhishun.cai
 * @create 2024/4/19
 * @note Topic 工具类
 */
public class TopicUtil {

    public static final String TOPIC_REG = "^(\\/[a-zA-Z0-9_]+)(((\\/[a-zA-Z0-9_]+)*(\\/\\#)?)|((\\/([a-zA-Z0-9_]+|\\+))*))";

    /**
     * 校验topic合法性，校验规则：
     * 1. 必须以 / 开头，比如 /a/b/c OK, a/b/c NG
     * 2. 只支持 0~9 | a-zA-Z | _ | 通配符（ # | + ） /a/12/b/32 OK , /a/b/1/# OK , /123/b/** NG
     * 3. 单层通配符必须占据整个层级
     *      sensor/+ 有效
     *      sensor/+/temperature 有效
     *      sensor+ 无效（没有占据整个层级）
     * 4. 多层通配符表示它的父级和任意数量的子层级，在使用多层通配符时，它必须占据整个层级并且必须是主题的最后一个字符，例如：
     *      sensor/# 有效
     *      sensor/bedroom# 无效（没有占据整个层级）
     *      sensor/#/temperature 无效（不是主题最后一个字符）
     *  5. 两个通配符不可以同时使用（同一个topic中）
     *
     *  正则为：^(\\/[a-zA-Z0-9_]+)(((\\/[a-zA-Z0-9_]+)*(\\/\\#)?)|((\\/([a-zA-Z0-9_]+|\\+))*))
     * @param topic
     * @return
     *
     *
     */
    public static boolean validTopic(String topic) {
        return Pattern.matches(TOPIC_REG, topic);
    }

    /**
     * 验证topic
     * @param topics
     * @return
     */
    public static boolean validTopics(List<String> topics) {
        for (String topic : topics) {
            if(!validTopic(topic)) {
                return false;
            }
        }
        return true;
    }


}
