package i2f.test.gateway.swl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

/**
 * @author Ice2Faith
 * @date 2024/7/12 20:41
 * @desc
 */
@SpringBootApplication
public class TestGatewaySwlApplication {
    public static void main(String[] args){

        SpringApplication.run(TestGatewaySwlApplication.class,args);
    }
}
