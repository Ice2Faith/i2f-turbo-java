package i2f.springboot.security.def.token;

import i2f.resp.ApiResp;
import i2f.web.guarder.LoginGuarder;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2022/4/7 13:55
 * @desc
 */
@ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
@Slf4j
@Component
public class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    protected AbstractTokenHolder tokenHolder;

    @Autowired(required = false)
    protected LoginGuarder loginGuarder;

    @Value("${i2f.springboot.config.security.login-single.enable:true}")
    protected boolean enableSingleLogin;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("DefaultAuthenticationSuccessHandler auth success.");

        UserDetails details = (UserDetails) authentication.getPrincipal();
        String token = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        String username = details.getUsername();
        log.info("DefaultAuthenticationSuccessHandler user:" + username + " with token:" + token);
        if (enableSingleLogin) {
            log.info("DefaultAuthenticationSuccessHandler single-login.");
            // 单点登录时，移除旧token,不需要单点登录，则不用移除旧token
            tokenHolder.setSingleToken(username, token, details);
        } else {
            tokenHolder.setToken(token, details);
        }

        if (loginGuarder != null) {
            loginGuarder.success(request, username);
        }
        ServletContextUtil.forward(request, response, ServletContextUtil.FORWARD_PATH, ApiResp.success(token));
    }
}
