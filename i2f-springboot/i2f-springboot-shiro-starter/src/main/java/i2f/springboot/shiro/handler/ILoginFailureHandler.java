package i2f.springboot.shiro.handler;

import org.apache.shiro.authc.AuthenticationException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2022/4/23 17:29
 * @desc
 */
public interface ILoginFailureHandler {
    void handle(AuthenticationException ex, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
