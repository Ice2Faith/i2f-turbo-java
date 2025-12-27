package i2f.springboot.security.def.token;

import i2f.resp.ApiResp;
import i2f.web.servlet.ServletContextUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2022/4/7 14:25
 * @desc
 */
@ConditionalOnMissingBean(LogoutSuccessHandler.class)
@Slf4j
@Component
public class DefaultLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    protected AbstractTokenHolder tokenHolder;

    @Value("${i2f.springboot.config.security.login-single.enable:true}")
    protected boolean enableSingleLogin;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("DefaultLogoutSuccessHandler logout.");
        String token = (String) request.getAttribute("token");
        if (enableSingleLogin) {
            log.info("DefaultLogoutSuccessHandler single-login logout.");
            UserDetails details = null;
            if (authentication != null) {
                details = (UserDetails) authentication.getPrincipal();
            }
            if (details != null) {
                String username = details.getUsername();
                log.info("DefaultLogoutSuccessHandler logout user:" + username + " with token:" + token);
                // 单点登录时，移除旧token,不需要单点登录，则不用移除旧token
                tokenHolder.removeSingleToken(username, token);
            }
        }
        tokenHolder.removeToken(token);
        ServletContextUtil.forward(request, response, ServletContextUtil.FORWARD_PATH, ApiResp.success(null, "ok"));
    }
}
