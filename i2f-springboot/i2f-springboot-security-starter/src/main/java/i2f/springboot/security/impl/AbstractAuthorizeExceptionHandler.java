package i2f.springboot.security.impl;

import i2f.net.http.HttpStatus;
import i2f.resp.ApiResp;
import i2f.springboot.security.exception.BoostAuthenticationException;
import i2f.web.servlet.ServletContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2022/4/7 10:31
 * @desc
 */
@Slf4j
public class AbstractAuthorizeExceptionHandler implements AuthorizeExceptionHandler {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        log.info("--------------authorize exception-----------");
        String requestUri = request.getRequestURI();
        if ("/error".equals(requestUri)) {
            onAuthoriedError(HttpStatus.ERROR, requestUri, request, response, ex);
        } else {
            int statusCode = HttpStatus.FORBIDDEN;
            if (ex instanceof InsufficientAuthenticationException) {
                statusCode = HttpStatus.UNAUTHORIZED;
            }
            if (ex instanceof UsernameNotFoundException
                    || ex instanceof BadCredentialsException) {
                statusCode = HttpStatus.UNAUTHORIZED;
            }
            onAuthoriedFailure(statusCode, requestUri, request, response, ex);
        }
    }

    public void onAuthoriedError(int statusCode, String requestUri, HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        log.info("AbstractAuthorizeExceptionHandler 500 authorize error:" + requestUri + " : " + ex.getMessage(), ex);
        String msg = "request resource authorize failure,internal error.";
        if (ex instanceof BoostAuthenticationException) {
            msg = ex.getMessage();
        }
        ServletContextUtil.forward(request, response, ServletContextUtil.FORWARD_PATH, ApiResp.resp(statusCode, msg, null));
    }

    public void onAuthoriedFailure(int statusCode, String requestUri, HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        log.info("AbstractAuthorizeExceptionHandler " + statusCode + " authorize failure:" + requestUri + " : " + ex.getMessage(), ex);
        String msg = "request resource authorize failure,reject access.";
        if (ex instanceof BoostAuthenticationException) {
            msg = ex.getMessage();
        }
        ServletContextUtil.setForwardException(request, ex);
        ServletContextUtil.forward(request, response, ServletContextUtil.FORWARD_PATH, ApiResp.resp(statusCode, msg, null));
    }

}
