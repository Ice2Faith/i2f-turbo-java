package i2f.springboot.shiro.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2022/4/23 17:29
 * @desc
 */
public interface IHttpRequestHandler {
    void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException;
}
