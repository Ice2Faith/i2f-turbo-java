package i2f.springboot.swl.spring;

import i2f.cache.std.expire.IExpireCache;
import i2f.reflect.ReflectResolver;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.spring.web.mapping.MappingUtil;
import i2f.swl.consts.SwlCode;
import i2f.swl.core.SwlTransfer;
import i2f.swl.exception.SwlException;
import i2f.swl.std.ISwlMessageDigester;
import i2f.swl.std.ISwlObfuscator;
import i2f.swl.std.supplier.ISwlAsymmetricEncryptorSupplier;
import i2f.swl.std.supplier.ISwlSymmetricEncryptorSupplier;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
@ConditionalOnExpression("${i2f.swl.enable:true}")
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

    @Autowired
    private BeanFactory beanFactory;


    public <T> T getBeanByTypeOrNewInstance(Class<T> clazz) {
        try {
            T ret = beanFactory.getBean(clazz);
            if (ret != null) {
                return ret;
            }
        } catch (Exception e) {
        }
        try {
            T ret = ReflectResolver.getInstance(clazz);
            return ret;
        } catch (Exception e) {

        }
        throw new IllegalStateException("get bean or new instance exception for class: " + clazz);
    }

    @ConditionalOnMissingBean(SwlTransfer.class)
    @Bean
    public SwlTransfer swlTransfer() {
        SwlTransfer ret = new SwlTransfer();
        try {

        } catch (Exception e) {
        }
        try {
            Class<? extends ISwlAsymmetricEncryptorSupplier> clazz = webProperties.getAsymAlgoClass();
            ISwlAsymmetricEncryptorSupplier supplier = getBeanByTypeOrNewInstance(clazz);
            ret.setAsymmetricEncryptorSupplier(supplier);
        } catch (Exception e) {
            throw new SwlException(SwlCode.SYMMETRIC_EXCEPTION.code(), e.getMessage(), e);
        }
        try {
            Class<? extends ISwlSymmetricEncryptorSupplier> clazz = webProperties.getSymmAlgoClass();
            ISwlSymmetricEncryptorSupplier supplier = getBeanByTypeOrNewInstance(clazz);
            ret.setSymmetricEncryptorSupplier(supplier);
        } catch (Exception e) {
            throw new SwlException(SwlCode.SYMMETRIC_EXCEPTION.code(), e.getMessage(), e);
        }

        try {
            Class<? extends ISwlMessageDigester> clazz = webProperties.getDigestAlgoClass();
            ISwlMessageDigester digester = getBeanByTypeOrNewInstance(clazz);
            ret.setMessageDigester(digester);
        } catch (Exception e) {
            throw new SwlException(SwlCode.SYMMETRIC_EXCEPTION.code(), e.getMessage(), e);
        }

        try {
            Class<? extends ISwlObfuscator> clazz = webProperties.getObfuscateAlgoClass();
            ISwlObfuscator obfuscator = getBeanByTypeOrNewInstance(clazz);
            ret.setObfuscator(obfuscator);
        } catch (Exception e) {
            throw new SwlException(SwlCode.SYMMETRIC_EXCEPTION.code(), e.getMessage(), e);
        }

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
    public FilterRegistrationBean<SwlSpringWebFilter> swlSpringFilter(SwlTransfer transfer) {
        FilterRegistrationBean<SwlSpringWebFilter> ret = new FilterRegistrationBean<>();
        SwlSpringWebFilter filter = new SwlSpringWebFilter();
        filter.setTransfer(transfer);
        filter.setConfig(webProperties);
        filter.setMappingUtil(mappingUtil);
        filter.setJsonSerializer(jsonSerializer);

        ret.setFilter(filter);
        ret.addUrlPatterns("/*");
        ret.setOrder(-1);
        ret.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
        return ret;
    }
}
