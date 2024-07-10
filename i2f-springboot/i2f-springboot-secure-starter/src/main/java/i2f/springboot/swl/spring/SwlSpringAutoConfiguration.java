package i2f.springboot.swl.spring;

import i2f.cache.expire.IExpireCache;
import i2f.serialize.str.json.IJsonSerializer;
import i2f.spring.web.mapping.MappingUtil;
import i2f.springboot.swl.core.SwlTransfer;
import i2f.springboot.swl.impl.SwlBase64Obfuscator;
import i2f.springboot.swl.impl.SwlSm2AsymmetricEncryptor;
import i2f.springboot.swl.impl.SwlSm3MessageDigester;
import i2f.springboot.swl.impl.SwlSm4SymmetricEncryptor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.DispatcherType;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2024/7/10 17:23
 * @desc
 */
@EnableConfigurationProperties({
        SwlWebConfigProperties.class,
        SwlTransferConfigProperties.class
})
@Data
@NoArgsConstructor
public class SwlSpringAutoConfiguration {

    @Autowired
    private IJsonSerializer jsonSerializer;

    @Autowired
    private SwlWebConfigProperties webProperties;

    @Autowired
    private SwlTransferConfigProperties transferProperties;

    @Autowired
    private MappingUtil mappingUtil;

    @Autowired
    private IExpireCache<String, Object> expireCache;


    @ConditionalOnMissingBean(SwlTransfer.class)
    @Bean
    public SwlTransfer swlTransfer() {
        SwlTransfer ret = new SwlTransfer();
        ret.setAsymmetricEncryptorSupplier(() -> new SwlSm2AsymmetricEncryptor());
        ret.setSymmetricEncryptorSupplier(() -> new SwlSm4SymmetricEncryptor());
        ret.setMessageDigester(new SwlSm3MessageDigester());
        ret.setObfuscator(new SwlBase64Obfuscator());

        ret.setCache(new IExpireCache<String, String>() {
            @Override
            public void set(String key, String value, long time, TimeUnit timeUnit) {
                expireCache.set(key, value, time, timeUnit);
            }

            @Override
            public void expire(String key, long time, TimeUnit timeUnit) {
                expireCache.expire(key, time, timeUnit);
            }

            @Override
            public Long getExpire(String key, TimeUnit timeUnit) {
                return expireCache.getExpire(key, timeUnit);
            }

            @Override
            public String get(String key) {
                return (String) expireCache.get(key);
            }

            @Override
            public void set(String key, String value) {
                expireCache.set(key, value);
            }

            @Override
            public boolean exists(String key) {
                return expireCache.exists(key);
            }

            @Override
            public void remove(String key) {
                expireCache.remove(key);
            }
        });

        ret.setConfig(transferProperties);


        return ret;
    }

    @Bean
    public FilterRegistrationBean<SwlSpringFilter> swlSpringFilter(SwlTransfer transfer) {
        FilterRegistrationBean<SwlSpringFilter> ret = new FilterRegistrationBean<>();
        SwlSpringFilter filter = new SwlSpringFilter();
        filter.setTransfer(transfer);
        filter.setConfig(webProperties);
        filter.setMappingUtil(mappingUtil);
        filter.setJsonSerializer(jsonSerializer);

        ret.setFilter(filter);
        ret.addUrlPatterns("/**");
        ret.setOrder(-1);
        ret.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
        return ret;
    }
}
