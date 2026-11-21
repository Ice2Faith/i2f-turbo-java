package i2f.springboot.ops.common.limit;


import i2f.limit.IKeyedLimiter;
import i2f.limit.ILimiter;
import i2f.limit.impl.ConcurrentMapKeyedLimiter;
import i2f.limit.impl.TokenBucketRateLimiter;
import i2f.lru.LruMap;
import i2f.match.impl.AntMatcher;
import i2f.web.filter.OncePerHttpServletFilter;
import i2f.web.servlet.ServletContextUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/11/21 17:17
 * @desc
 */
@Data
@NoArgsConstructor
public class OpsLimitedRequestFilter extends OncePerHttpServletFilter {
    protected IKeyedLimiter keyedLimiter = new ConcurrentMapKeyedLimiter(() -> new TokenBucketRateLimiter(100, 300));
    protected LruMap<String, ILimiter> ipLruLimiters = new LruMap<>(8192);
    protected OpsLimitProperties properties;
    protected final AntMatcher matcher = new AntMatcher("/");

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            OpsLimitProperties.LimitRule allRule = properties.getAllRule();
            if (allRule != null && allRule.isEnable()) {
                ILimiter limiter = keyedLimiter.get(OpsLimitProperties.KEY_ALL, k -> new TokenBucketRateLimiter(allRule.getRatePerSecond(), allRule.getCapacity()));
                try {
                    if (!limiter.tryAcquire(1, TimeUnit.SECONDS)) {
                        throw new OpsLimitedException("global limited.");
                    }
                } catch (InterruptedException e) {
                    throw new OpsLimitedException("global limited.");
                }
            }

            OpsLimitProperties.EveryIpLimitRule ipRule = properties.getEveryIpRule();

            if (ipRule != null) {
                OpsLimitProperties.LimitRule rule = ipRule.getRule();
                if (rule != null && rule.isEnable()) {
                    String ip = ServletContextUtil.getIp(request);
                    String key = OpsLimitProperties.KEY_IP + ip;
                    ILimiter limiter = ipLruLimiters.computeIfAbsent(key, k -> new TokenBucketRateLimiter(rule.getRatePerSecond(), rule.getCapacity()));
                    try {
                        if (!limiter.tryAcquire(1, TimeUnit.SECONDS)) {
                            throw new OpsLimitedException("ip limited.");
                        }
                    } catch (InterruptedException e) {
                        throw new OpsLimitedException("ip limited.");
                    }
                }
            }

            List<OpsLimitProperties.PathLimitRule> pathRules = properties.getPathRules();
            if (pathRules != null && !pathRules.isEmpty()) {
                String fullPath = ServletContextUtil.getFullPathWithoutContextPath(request);
                for (OpsLimitProperties.PathLimitRule pathRule : pathRules) {
                    if (pathRule == null) {
                        continue;
                    }
                    String antPath = pathRule.getAntPath();
                    if (antPath == null || antPath.isEmpty()) {
                        continue;
                    }
                    OpsLimitProperties.LimitRule rule = pathRule.getRule();
                    if (rule == null || !rule.isEnable()) {
                        continue;
                    }
                    if (matcher.matches(fullPath, antPath)) {
                        ILimiter limiter = keyedLimiter.get(OpsLimitProperties.KEY_ANT_PATH + antPath, k -> new TokenBucketRateLimiter(rule.getRatePerSecond(), rule.getCapacity()));
                        try {
                            if (!limiter.tryAcquire(1, TimeUnit.SECONDS)) {
                                throw new OpsLimitedException("path limited.");
                            }
                        } catch (InterruptedException e) {
                            throw new OpsLimitedException("path limited.");
                        }
                    }
                }
            }

            chain.doFilter(request, response);
        } catch (OpsLimitedException e) {
            if (response.isCommitted()) {
                return;
            }
            response.reset();
            response.setStatus(429);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().print(e.getMessage());
        }
    }
}
