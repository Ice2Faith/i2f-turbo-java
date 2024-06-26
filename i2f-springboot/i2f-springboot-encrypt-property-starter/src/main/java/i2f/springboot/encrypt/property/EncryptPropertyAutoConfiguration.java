package i2f.springboot.encrypt.property;

import i2f.springboot.encrypt.property.core.IPropertyDecryptor;
import i2f.springboot.encrypt.property.core.PropertiesDecryptAdapter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Ice2Faith
 * @date 2022/6/7 10:12
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.encrypt.property.enable:true}")
@Slf4j
@Data
@ConfigurationProperties("i2f.springboot.encrypt.property")
public class EncryptPropertyAutoConfiguration {

    @Bean
    public PropertiesDecryptAdapter propertiesDecryptAdapter(IPropertyDecryptor decryptor) {
        return new PropertiesDecryptAdapter(decryptor);
    }

}
