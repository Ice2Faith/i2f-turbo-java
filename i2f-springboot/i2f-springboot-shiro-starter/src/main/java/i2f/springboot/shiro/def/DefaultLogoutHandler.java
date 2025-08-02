package i2f.springboot.shiro.def;

import i2f.resp.ApiResp;
import i2f.springboot.shiro.ShiroAutoConfiguration;
import i2f.springboot.shiro.handler.ILogoutHandler;
import i2f.springboot.shiro.token.AbstractShiroTokenHolder;
import i2f.web.servlet.ServletContextUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2022/4/23 17:51
 * @desc
 */
@ConditionalOnBean(ShiroAutoConfiguration.class)
@ConditionalOnMissingBean(ILogoutHandler.class)
@Component
@Data
@NoArgsConstructor
@Slf4j
public class DefaultLogoutHandler implements ILogoutHandler, InitializingBean {
    private String tokenName = "token";
    @Autowired
    private AbstractShiroTokenHolder tokenHolder;

    @Override
    public void handle(boolean fullMode, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = ServletContextUtil.getToken(request, tokenName);
        if (token != null && !"".equals(token)) {
            tokenHolder.removeToken(token);
        }
        if (fullMode) {
            ServletContextUtil.forward(request, response, ServletContextUtil.FORWARD_PATH, ApiResp.success("ok"));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DefaultLogoutHandler config done.");
    }
}
