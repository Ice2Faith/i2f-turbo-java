package i2f.springboot.ops.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/8/26 10:10
 * @desc
 */
@Configuration
@Data
@NoArgsConstructor
public class OpsStaticResourceConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 依赖库7天缓存
        registry.addResourceHandler("/ops/lib/**")
                .addResourceLocations("classpath:/assets/ops/lib/")
                .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS)
                        .cachePublic()
                        .mustRevalidate());

        // 其他文件1天缓存
        registry.addResourceHandler("/ops/**")
                .addResourceLocations("classpath:/assets/ops/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS)
                        .cachePublic()
                        .mustRevalidate());

        // 恢复 Spring Boot 默认的静态资源映射，防止其他资源 404
        registry.addResourceHandler("/**")
                .addResourceLocations(
                        "classpath:/static/",
                        "classpath:/public/"
                )
                .setCacheControl(CacheControl.noCache()); // 默认资源建议不做强缓存
    }
}
