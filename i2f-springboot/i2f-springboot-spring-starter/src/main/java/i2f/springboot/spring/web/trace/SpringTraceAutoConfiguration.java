package i2f.springboot.spring.web.trace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @author Ice2Faith
 * @date 2024/10/23 19:38
 * @desc
 */
@ConditionalOnClass(FilterRegistrationBean.class)
@ConditionalOnExpression("${i2f.spring.trace.enable:true}")
@Slf4j
@ConfigurationProperties(prefix = "i2f.spring.trace")
public class SpringTraceAutoConfiguration {

    @Bean
    public FilterRegistrationBean<SpringTraceFilter> springTraceFilter() {
        FilterRegistrationBean<SpringTraceFilter> registry = new FilterRegistrationBean<>();
        registry.setFilter(new SpringTraceFilter());
        registry.setOrder(1);
        registry.addUrlPatterns("/*");
        return registry;
    }
}
