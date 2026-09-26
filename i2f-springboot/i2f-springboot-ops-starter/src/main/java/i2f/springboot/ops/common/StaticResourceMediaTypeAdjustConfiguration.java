package i2f.springboot.ops.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Ice2Faith
 * @date 2026/7/20 15:48
 * @desc
 */
@Configuration
@Data
@NoArgsConstructor
public class StaticResourceMediaTypeAdjustConfiguration implements WebMvcConfigurer {
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.mediaType("mjs", MediaType.valueOf("application/javascript"));
    }
}
