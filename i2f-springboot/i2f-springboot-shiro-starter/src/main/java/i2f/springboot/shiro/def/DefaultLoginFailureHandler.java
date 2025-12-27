package i2f.springboot.shiro.def;

import i2f.resp.ApiResp;
import i2f.springboot.shiro.ShiroAutoConfiguration;
import i2f.springboot.shiro.handler.ILoginFailureHandler;
import i2f.web.servlet.ServletContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2022/4/23 17:43
 * @desc
 */
@ConditionalOnBean(ShiroAutoConfiguration.class)
@ConditionalOnMissingBean(ILoginFailureHandler.class)
@Component
@Slf4j
public class DefaultLoginFailureHandler implements ILoginFailureHandler, InitializingBean {
    @Override
    public void handle(AuthenticationException ex, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContextUtil.forward(request, response, ServletContextUtil.FORWARD_PATH, ApiResp.error(401, ex.getMessage()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DefaultLoginFailureHandler config done.");
    }
}

