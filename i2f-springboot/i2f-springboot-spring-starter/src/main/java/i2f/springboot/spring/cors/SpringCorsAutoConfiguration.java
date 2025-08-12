package i2f.springboot.spring.cors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2024/6/12 9:44
 * @desc
 */
@ConditionalOnClass(CorsFilter.class)
@ConditionalOnExpression("${i2f.spring.cors.enable:true}")
@Slf4j
@ConfigurationProperties(prefix = "i2f.spring.cors")
public class SpringCorsAutoConfiguration {

    private String urlPatten = "/**";

    private String allowOrigins = "*";
    private String allowMethods = "*";
    private String allowHeaders = "*";
    private String exposedHeaders = "*";

    private boolean allowCredentials = true;
    private long maxAge = 6000L;

    @ConditionalOnClass(CorsFilter.class)
    @Order(1)
    @Bean
    public CorsFilter corsFilter() {
        List<String> originList = Arrays.asList(allowOrigins.split(",|;"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        OriginPattenCorsConfiguration config = new OriginPattenCorsConfiguration();
        config.setAllowedMethods(Arrays.asList(allowMethods.split(",|;")));
        config.setAllowedOrigins(originList.stream().filter(e->!e.contains("*")).collect(Collectors.toList()));
        config.setAllowedHeaders(Arrays.asList(allowHeaders.split(",|;")));
        config.setExposedHeaders(Arrays.asList(exposedHeaders.split(",|;")));
        config.setAllowedOriginPatterns(originList);

        config.setAllowCredentials(allowCredentials);

        config.setMaxAge(maxAge);
        // 对接口配置跨域设置
        source.registerCorsConfiguration(urlPatten, config);
        log.info("CorsConfig CorsFilter config done.");
        return new CorsFilter(source);
    }
}
