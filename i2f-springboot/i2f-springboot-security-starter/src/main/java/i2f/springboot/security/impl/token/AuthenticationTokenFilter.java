package i2f.springboot.security.impl.token;

import i2f.spring.security.SecurityUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2022/4/7 9:38
 * @desc
 */
@Slf4j
public abstract class AuthenticationTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("----------AuthenticationTokenFilter--------------");
        Authentication auth = SecurityUtil.getAuthentication();
        if (auth == null) {
            log.info("AuthenticationTokenFilter not found auth,try find in token...");
            Authentication tokenAuth = getTokenAuthentication(request, response);
            if (tokenAuth != null) {
                log.info("AuthenticationTokenFilter token found auth.");
                SecurityContextHolder.getContext().setAuthentication(tokenAuth);
            }
        }
        chain.doFilter(request, response);
    }

    protected abstract Authentication getTokenAuthentication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
