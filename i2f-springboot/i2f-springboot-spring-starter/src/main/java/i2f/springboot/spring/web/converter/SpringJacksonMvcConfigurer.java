package i2f.springboot.spring.web.converter;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Iterator;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/6/16 20:21
 * @desc
 */
@ConditionalOnExpression("${i2f.spring.jackson.enable:true}")
@Data
public class SpringJacksonMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

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
