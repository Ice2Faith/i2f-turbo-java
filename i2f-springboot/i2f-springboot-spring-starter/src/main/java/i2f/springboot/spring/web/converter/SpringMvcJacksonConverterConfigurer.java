package i2f.springboot.spring.web.converter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Iterator;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/7/12 21:32
 * @desc
 */
@Data
@NoArgsConstructor
public class SpringMvcJacksonConverterConfigurer implements WebMvcConfigurer {

    private JacksonJsonHttpMessageConverter mappingJackson2HttpMessageConverter;

    public SpringMvcJacksonConverterConfigurer(JacksonJsonHttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.mappingJackson2HttpMessageConverter = mappingJackson2HttpMessageConverter;
    }

    @Override
    public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {
        builder.configureMessageConvertersList(this::apply);
    }

    public void apply(List<HttpMessageConverter<?>> converters) {
        Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof JacksonJsonHttpMessageConverter) {
                iterator.remove();
            }
        }
        converters.add(mappingJackson2HttpMessageConverter);
    }
}
