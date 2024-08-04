package i2f.springboot.spring.web.converter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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

    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    public SpringMvcJacksonConverterConfigurer(MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.mappingJackson2HttpMessageConverter = mappingJackson2HttpMessageConverter;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                iterator.remove();
            }
        }
        converters.add(mappingJackson2HttpMessageConverter);
    }
}
