package i2f.springboot.spring.web.security;

import i2f.web.filter.SecurityFilter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @author Ice2Faith
 * @date 2024/10/25 8:27
 */
@ConditionalOnExpression("${i2f.security.filter.enable:true}")
@Data
@Slf4j
@NoArgsConstructor
@ConfigurationProperties("i2f.security.filter")
public class SecurityFilterAutoConfiguration {

    private String urlPatten = "/*";
    private int order = -1;

    @Bean
    public FilterRegistrationBean<SecurityFilter> securityFilterFilterRegistrationBean() {
        FilterRegistrationBean<SecurityFilter> ret = new FilterRegistrationBean<>();
        ret.setFilter(new SecurityFilter());
        ret.addUrlPatterns(urlPatten);
        ret.setOrder(order);
        return ret;
    }
}
