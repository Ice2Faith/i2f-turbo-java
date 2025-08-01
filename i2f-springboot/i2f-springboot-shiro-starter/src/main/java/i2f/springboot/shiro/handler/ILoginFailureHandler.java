package i2f.springboot.shiro.handler;

import org.apache.shiro.authc.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2022/4/23 17:29
 * @desc
 */
public interface ILoginFailureHandler {
    void handle(AuthenticationException ex, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
