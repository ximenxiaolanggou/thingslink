package center.helloworld.starter.openfeign.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

/**
 * @author zhishun.cai
 * @create 2024/4/12
 * @note
 */
public class RequestHeaderForwordInterceptor implements RequestInterceptor {

    // 需要转发的请求头，可以放置在配置文件中
    public List<String> neadForwordHeaders = Arrays.asList(
            "Authorization"
    );

    @Override
    public void apply(RequestTemplate requestTemplate) {

        // 1. 获取请求头
        final ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();

        // 2. 按需转发请求头
        if (!CollectionUtils.isEmpty(neadForwordHeaders)) {
            for (String herderName : neadForwordHeaders) {
                String headerValue = request.getHeader(herderName);
                if (headerValue != null && headerValue.trim() != "") {
                    // 将请求头信息转发
                    requestTemplate.header(herderName, headerValue);
                }

            }
        }
    }
}
