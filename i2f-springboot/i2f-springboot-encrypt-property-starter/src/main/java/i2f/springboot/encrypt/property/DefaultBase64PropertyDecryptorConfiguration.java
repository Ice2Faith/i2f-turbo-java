package i2f.springboot.encrypt.property;

import i2f.springboot.encrypt.property.core.IPropertyDecryptor;
import i2f.springboot.encrypt.property.impl.Base64PropertyDecryptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Ice2Faith
 * @date 2022/6/7 10:12
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.encrypt.property.base64.enable:true}")
@ConfigurationProperties(prefix = "i2f.springboot.encrypt.property.base64")
public class DefaultBase64PropertyDecryptorConfiguration {

    @Bean
    public IPropertyDecryptor propertyDecryptor(){
        return new Base64PropertyDecryptor();
    }
}
