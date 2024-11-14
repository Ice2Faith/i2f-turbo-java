package i2f.springboot.trace.mdc.spring.springmvc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author Ice2Faith
 * @date 2024/11/12 16:20
 */
@ConditionalOnClass({
        OncePerRequestFilter.class,
        FilterRegistrationBean.class,
        WebTraceSlf4jMdcFilter.class
})
@Configuration
public class WebTraceSlf4jMdcConfiguration {
    public static final int ORDER = -9000;

    @Bean
    public FilterRegistrationBean<WebTraceSlf4jMdcFilter> traceSlf4jMdcFilterFilterRegistrationBean() {
        FilterRegistrationBean<WebTraceSlf4jMdcFilter> ret = new FilterRegistrationBean<>();
        ret.setFilter(new WebTraceSlf4jMdcFilter());
        ret.addUrlPatterns("/*");
        ret.setOrder(ORDER);
        return ret;
    }
}
