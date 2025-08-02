package i2f.spring.ai.chat.configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Ice2Faith
 * @date 2025/4/27 16:31
 */
@Slf4j
@Data
@ConditionalOnExpression("${i2f.ai.chat.web-ui.enable:true}")
@Configuration
public class WebUiConfiguration implements WebMvcConfigurer, EnvironmentAware {
    protected Environment environment;

    @Value("${i2f.ai.chat.web-ui.allow-redirect-root-url:false}")
    protected boolean allowRedirectRootUrl = false;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/chat-ai/**")
                .addResourceLocations("classpath:/assets/chat-ai")
                .setCachePeriod(3600) // 设置缓存时间（可选）
                .resourceChain(true); // 启用资源链（可选）
        log.info("Chat-AI simple web-ui: http://localhost:" + environment.getProperty("server.port", "8080") + "/chat-ai/index.html");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/chat-ai")
                .setViewName("forward:/chat-ai/index.html");
        if (allowRedirectRootUrl) {
            registry.addViewController("/")
                    .setViewName("redirect:/chat-ai");
        }
    }
}
