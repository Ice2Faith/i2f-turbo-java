package i2f.springboot.nginx.rtmp.auth.server;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/8/3 15:22
 */
@FunctionalInterface
public interface NginxRtmpAuthTokenValidator {
    boolean validate(HttpServletRequest request, String token, Map<String, Object> nginxData);
}
