package i2f.springcloud.gateway.swl;

import i2f.cache.std.expire.IExpireCache;
import i2f.reflect.ReflectResolver;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.swl.consts.SwlCode;
import i2f.swl.core.SwlTransfer;
import i2f.swl.exception.SwlException;
import i2f.swl.std.ISwlMessageDigester;
import i2f.swl.std.ISwlObfuscator;
import i2f.swl.std.supplier.ISwlAsymmetricEncryptorSupplier;
import i2f.swl.std.supplier.ISwlSymmetricEncryptorSupplier;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2024/7/12 15:08
 * @desc
 */
@Data
@Slf4j
@ConditionalOnExpression("${i2f.gateway.swl.enable:true}")
@EnableConfigurationProperties({
        SwlWebConfigProperties.class,
        SwlTransferConfigProperties.class
})
@Import({

})
@ConfigurationProperties(prefix = "i2f.gateway.swl")
public class SwlGatewayAutoConfiguration {

    @Autowired
    private IJsonSerializer jsonSerializer;

    @Autowired
    private SwlWebConfigProperties webProperties;

    @Autowired
    private SwlTransferConfigProperties transferProperties;

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

    @ConditionalOnExpression("${i2f.gateway.swl.api-route.enable:true}")
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("swl_api_route", r -> r.path(SwlGatewayApiFilter.KEY_PATH,SwlGatewayApiFilter.KEY_SWAP_PATH)
                        .uri("http://localhost:80"))
                .build();
    }
}
