package i2f.springboot.jdbc.bql.autoconfiguration;

import i2f.springboot.jdbc.bql.components.BqlTemplate;
import i2f.springboot.jdbc.bql.properties.JdbcBqlProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

/**
 * @author Ice2Faith
 * @date 2024/4/24 16:19
 * @desc
 */
@ConditionalOnExpression("${i2f.jdbc.bql.enable:true}")
@Import({

})
@EnableConfigurationProperties({
        JdbcBqlProperties.class
})
public class JdbcBqlAutoConfiguration {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean
    @ConditionalOnMissingBean(BqlTemplate.class)
    public BqlTemplate bqlTemplate(DataSource dataSource) {
        log.info(BqlTemplate.class.getSimpleName() + " config.");
        return new BqlTemplate(dataSource);
    }
}
