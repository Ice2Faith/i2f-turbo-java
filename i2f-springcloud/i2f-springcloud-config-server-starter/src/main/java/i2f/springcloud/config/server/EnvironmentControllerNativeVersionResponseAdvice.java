package i2f.springcloud.config.server;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/1 14:30
 * @desc
 */
@ConditionalOnExpression("${i2f.springcloud.config-server.native.version.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@RestControllerAdvice
public class EnvironmentControllerNativeVersionResponseAdvice implements ResponseBodyAdvice<Environment> {
    protected ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = returnType.getMethod();
        if (method == null) {
            return false;
        }
        return Environment.class.isAssignableFrom(method.getReturnType());
    }

    @Override
    public Environment beforeBodyWrite(Environment body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return null;
        }
        String version = body.getVersion();
        if (version != null && !version.isEmpty()) {
            return body;
        }
        List<PropertySource> sourceList = body.getPropertySources();
        if (sourceList == null) {
            return body;
        }
        List<String> nameList = new ArrayList<>();
        for (PropertySource source : sourceList) {
            String name = source.getName();
            if (name != null) {
                nameList.add(name);
            }
        }
        nameList.sort(String::compareTo);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            for (String name : nameList) {
                digest.update(name.getBytes(StandardCharsets.UTF_8));
                try {
                    Resource resource = resourceLoader.getResource(name);
                    try (InputStream is = resource.getInputStream()) {
                        byte[] buff = new byte[1024];
                        int len = 0;
                        while ((len = is.read(buff)) > 0) {
                            digest.update(buff, 0, len);
                        }
                    }
                } catch (Exception e) {
                    digest.update("nop".getBytes(StandardCharsets.UTF_8));
                }
            }
            byte[] arr = digest.digest();
            StringBuilder builder = new StringBuilder();
            for (byte bt : arr) {
                builder.append(String.format("%02x", (int) (bt & 0x0ff)));
            }
            body.setVersion(builder.toString());
            return body;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return body;
    }
}
