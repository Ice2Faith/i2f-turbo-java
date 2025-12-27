package i2f.springboot.limit.filter;

import i2f.spring.web.mapping.MappingUtil;
import i2f.springboot.limit.core.LimitConsts;
import i2f.springboot.limit.core.LimitManager;
import i2f.springboot.limit.core.LimitType;
import i2f.springboot.limit.data.LimitRuleItem;
import i2f.springboot.limit.exception.LimitException;
import i2f.springboot.limit.util.LimitUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.RequestPath;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.util.ServletRequestPathUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/11/11 17:27
 */
@Slf4j
@Data
@NoArgsConstructor
public class LimitFilter extends OncePerRequestFilter {
    private LimitManager manager;
    private MappingUtil mappingUtil;
    private AntPathMatcher antPathMatcher = new AntPathMatcher("/");

    public LimitFilter(LimitManager manager, MappingUtil mappingUtil) {
        this.manager = manager;
        this.mappingUtil = mappingUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            assertLimitGlobal(request);

            assertLimitPath(request);

            assertLimitIp(request);

            assertLimitApi(request);

            assertLimitAntPath(request);

            assertLimitUser(request);
        } catch (Exception e) {
            if (e instanceof LimitException) {
                throw e;
            } else {
                log.warn(e.getMessage(), e);
            }
        }

        chain.doFilter(request, response);
    }

    public void assertLimitGlobal(HttpServletRequest request) {
        boolean limited = manager.isLimited(LimitType.GLOBAL, "default");
        if (limited) {
            throw new LimitException("your request has been limited by website global rule!");
        }
    }

    public void assertLimitPath(HttpServletRequest request) {
        String path = getRequestPath(request);
        if (path == null || path.isEmpty()) {
            return;
        }
        boolean limited = manager.isLimited(LimitType.PATH, path);
        if (limited) {
            throw new LimitException("your request has been limited by request path rule!");
        }
    }

    public String getRequestPath(HttpServletRequest request) {
        RequestPath requestPath = ServletRequestPathUtils.parseAndCache(request);
        return requestPath.value();
    }

    public void assertLimitIp(HttpServletRequest request) {
        String ip = getRequestIp(request);
        if (ip == null || ip.isEmpty()) {
            return;
        }
        boolean limited = manager.isLimited(LimitType.IP, ip);
        if (limited) {
            throw new LimitException("your request has been limited by request ip rule!");
        }
    }

    public String getRequestIp(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    public void assertLimitApi(HttpServletRequest request) {
        Map.Entry<RequestMappingInfo, HandlerMethod> entry = mappingUtil.getRequestMapping(request);
        if (entry == null) {
            return;
        }
        HandlerMethod value = entry.getValue();
        if (value == null) {
            return;
        }
        Method method = value.getMethod();
        String signature = LimitUtil.getMethodSignature(method);
        boolean limited = manager.isLimited(LimitType.API, signature);
        if (limited) {
            throw new LimitException("your request has been limited by request api rule!");
        }
    }

    public void assertLimitAntPath(HttpServletRequest request) {
        String path = getRequestPath(request);
        if (path == null || path.isEmpty()) {
            return;
        }
        List<LimitRuleItem> rules = manager.getRules(LimitType.ANT_PATH);
        String typeKey = null;
        for (LimitRuleItem rule : rules) {
            String pattern = rule.getTypeKey();
            if (antPathMatcher.match(pattern, path)) {
                typeKey = pattern;
            }
        }
        if (typeKey == null) {
            return;
        }
        boolean limited = manager.isLimited(LimitType.ANT_PATH, typeKey);
        if (limited) {
            throw new LimitException("your request has been limited by request ant-path rule!");
        }
    }

    public void assertLimitUser(HttpServletRequest request) {
        String user = getRequestUser(request);
        if (user == null || user.isEmpty()) {
            return;
        }
        boolean limited = manager.isLimited(LimitType.USER, user);
        if (limited) {
            throw new LimitException("your request has been limited by request user rule!");
        }
    }

    public String getRequestUser(HttpServletRequest request) {
        Object user = request.getAttribute(LimitConsts.LIMIT_REQUEST_USER_ID);
        if (user == null) {
            return null;
        }
        String ret = String.valueOf(user);
        if (ret.isEmpty()) {
            return null;
        }
        return ret;
    }
}
