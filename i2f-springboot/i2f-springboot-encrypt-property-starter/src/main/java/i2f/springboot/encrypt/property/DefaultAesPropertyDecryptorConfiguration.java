package i2f.springboot.encrypt.property;

import i2f.springboot.encrypt.property.core.IPropertyDecryptor;
import i2f.springboot.encrypt.property.impl.AesPropertyDecryptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Ice2Faith
 * @date 2022/6/7 10:12
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.encrypt.property.aes.enable:true}")
@ConfigurationProperties(prefix = "i2f.springboot.encrypt.property.aes")
public class DefaultAesPropertyDecryptorConfiguration {

    private String key;

    @Bean
    public IPropertyDecryptor propertyDecryptor() {
        return new AesPropertyDecryptor(key);
    }
}
