package i2f.springboot.secure;


import i2f.springboot.spring.web.converter.SpringJacksonMessageConverter;
import i2f.springboot.spring.web.converter.SpringJacksonMvcConfigurer;
import i2f.springboot.secure.core.*;
import i2f.springboot.spring.web.SpringObjectMapperCustomizerConfiguration;
import i2f.springboot.secure.exception.ExceptionResolveHandler;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ice2Faith
 * @date 2021/9/2
 */
@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Import({
        SecureConfig.class,
        SecureTransferFilter.class,
        SecureTransferAop.class,
        SecureTransfer.class,
        SecureController.class,
        SecureEncodeForwardController.class,
        ExceptionResolveHandler.class,
        SpringJacksonMvcConfigurer.class,
        SpringJacksonMessageConverter.class,
        SpringObjectMapperCustomizerConfiguration.class
})
@EnableWebMvc
public @interface EnableSecureConfig {

}
