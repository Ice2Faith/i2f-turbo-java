package i2f.springboot.spring.web.converter;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
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
@ConditionalOnClass(WebMvcConfigurer.class)
@ConditionalOnExpression("${i2f.spring.jackson.enable:true}")
@Data
public class SpringJacksonMvcConfigurer {

    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Bean
    public SpringMvcJacksonConverterConfigurer springMvcJacksonConverterConfigurer(){
        return new SpringMvcJacksonConverterConfigurer(mappingJackson2HttpMessageConverter);
    }
}
