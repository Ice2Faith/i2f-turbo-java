package i2f.springboot.mybatis;

import i2f.extension.mybatis.interceptor.MybaisPaginationInterceptor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Ice2Faith
 * @date 2022/3/27 14:00
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.mybatis.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@EnableScheduling
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.mybatis")
@MapperScan(basePackages = {"com.**.mapper", "com.**.dao"})
public class MybatisAutoConfiguration implements InitializingBean {


    private boolean enablePaginationInterceptor = false;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("MybatisAutoConfiguration config done.");
    }

    @ConditionalOnExpression("${i2f.springboot.config.mybatis.interceptor.enable:true}")
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                log.info("MybatisConfig interceptors config");
                if (enablePaginationInterceptor) {
                    log.info("MybatisConfig pagination interceptor config");
                    configuration.addInterceptor(new MybaisPaginationInterceptor());
                }
            }
        };
    }
}
