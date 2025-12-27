package i2f.springboot.nginx.rtmp.auth.server;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/8/3 15:23
 */
@ConditionalOnExpression("${i2f.springboot.nginx.rtmp.auth.default-validator.enable:true}")
@ConfigurationProperties(prefix = "i2f.springboot.nginx.rtmp.auth.default-validator")
@Slf4j
@Component
public class DefaultNginxRtmpAuthTokenValidator implements NginxRtmpAuthTokenValidator {
    private String accessToken;

    @Override
    public boolean validate(HttpServletRequest request, String token, Map<String, Object> nginxData) {
        if (!StringUtils.isEmpty(token)) {
            if (!token.equals(accessToken)) {
                return false;
            }
        }
        return true;
    }
}
