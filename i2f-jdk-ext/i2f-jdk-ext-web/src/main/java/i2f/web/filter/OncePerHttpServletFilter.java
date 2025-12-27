package i2f.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2024/7/8 14:01
 * @desc
 */
public abstract class OncePerHttpServletFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!(response instanceof HttpServletResponse)) {
            filterChain.doFilter(request, response);
            return;
        }

        String filterName = getClass().getName();

        Object attr = request.getAttribute(filterName);
        if ("true".equals(attr)) {
            filterChain.doFilter(request, response);
            return;
        }

        request.setAttribute(filterName, "true");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        doFilterInternal(httpRequest, httpResponse, filterChain);

    }

    public abstract void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;

}
