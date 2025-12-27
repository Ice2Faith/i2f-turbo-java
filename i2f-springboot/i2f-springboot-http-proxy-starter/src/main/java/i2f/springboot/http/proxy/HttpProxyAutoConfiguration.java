package i2f.springboot.http.proxy;


import i2f.spring.web.proxy.HttpProxyFilter;
import i2f.spring.web.proxy.HttpProxyHandler;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import jakarta.servlet.DispatcherType;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/10/23 8:44
 * @desc
 */
@ConditionalOnExpression("${i2f.http.proxy.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.http.proxy")
public class HttpProxyAutoConfiguration {

    private List<HttpProxyMapping> mappings;

    @Bean
    public HttpProxyHandler httpProxyHandler() {
        HttpProxyHandler ret = HttpProxyHandler.build();
        int cnt = 0;
        for (HttpProxyMapping mapping : mappings) {
            if (mapping == null) {
                continue;
            }
            String prefix = mapping.getPrefix();
            if (StringUtils.isEmpty(prefix)) {
                continue;
            }
            String target = mapping.getTarget();
            if (StringUtils.isEmpty(target)) {
                continue;
            }
            ret.mapping(prefix, target);
            cnt++;
            log.info("HttpProxyHandler handle " + prefix + " to " + target);
        }

        log.info("HttpProxyHandler handle " + cnt + " mappings done.");
        return ret;
    }

    @Bean
    public FilterRegistrationBean<HttpProxyFilter> httpProxyFilterFilterRegistrationBean(HttpProxyHandler httpProxyHandler) {
        FilterRegistrationBean<HttpProxyFilter> ret = new FilterRegistrationBean<>();
        HttpProxyFilter filter = new HttpProxyFilter(httpProxyHandler);
        ret.setFilter(filter);
        ret.addUrlPatterns("/*");
        ret.setOrder(-1);
        ret.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
        ret.setMatchAfter(false);
        log.info("HttpProxyFilter config done.");
        return ret;
    }

    @Data
    @NoArgsConstructor
    public static class HttpProxyMapping {
        private String prefix;
        private String target;
    }
}
