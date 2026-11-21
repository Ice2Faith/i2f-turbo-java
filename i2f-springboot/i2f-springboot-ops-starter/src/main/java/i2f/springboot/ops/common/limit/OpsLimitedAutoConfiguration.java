package i2f.springboot.ops.common.limit;

import i2f.springboot.ops.common.OpsConsts;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author Ice2Faith
 * @date 2026/11/21 17:37
 * @desc
 */
@ConditionalOnClass(FilterRegistrationBean.class)
@ConditionalOnExpression("${i2f.springboot.ops.limited.enable:true}")
@Data
@NoArgsConstructor
@Slf4j
@Configuration
@EnableConfigurationProperties(OpsLimitProperties.class)
public class OpsLimitedAutoConfiguration {
    @Autowired
    private Environment environment;
    @Autowired
    protected OpsLimitProperties properties;

    @Bean
    public FilterRegistrationBean<OpsLimitedRequestFilter> opsLimitedRequestFilterFilterRegistrationBean() {
        FilterRegistrationBean<OpsLimitedRequestFilter> registry = new FilterRegistrationBean<>();

        String urlPattern = properties.getFilterUrlPattern();
        if (urlPattern == null || urlPattern.isEmpty()) {
            String baseUrl = environment.getProperty(OpsConsts.BASE_URL_PROPERTY, OpsConsts.DEFAULT_BASE_URL);
            urlPattern = baseUrl + "/*";
        }
        properties.setFilterUrlPattern(urlPattern);

        OpsLimitedRequestFilter filter = new OpsLimitedRequestFilter();
        filter.setProperties(properties);

        OpsLimitProperties.EveryIpLimitRule ipRule = properties.getEveryIpRule();
        if (ipRule != null) {
            if (ipRule.getLruMaxSize() > 0) {
                filter.getIpLruLimiters().setMaxSize(ipRule.getLruMaxSize());
            }
        }

        registry.setFilter(filter);
        registry.setOrder(1);
        registry.addUrlPatterns(urlPattern);
        log.info("ops limited request filter registered on url-pattern: " + urlPattern);
        return registry;
    }
}
