package i2f.springboot.mybatis;

import i2f.extension.mybatis.interceptor.MybatisRecordSqlInterceptor;
import i2f.extension.mybatis.interceptor.MybatisResultSetMetaInterceptor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.mybatis")
public class MybatisDynamicAutoConfiguration {

    @Value("${i2f.mybatis.interceptor.record-sql.enable:true}")
    protected boolean enablePrintRecordSql=false;

    @ConditionalOnExpression("${i2f.mybatis.interceptor.record-sql.enable:true}")
    @Bean
    public MybatisRecordSqlInterceptor mybatisRecordSqlInterceptor(){
        MybatisRecordSqlInterceptor ret = new MybatisRecordSqlInterceptor();
        Logger logger= LoggerFactory.getLogger(ret.getClass());
        ret.setInfoLogger(e->logger.info(String.valueOf(e)));
        ret.setErrorLogger(e->logger.error(String.valueOf(e)));
        ret.setEnablePrintSql(enablePrintRecordSql);
        return ret;
    }

    @ConditionalOnExpression("${i2f.mybatis.interceptor.result-set-meta.enable:true}")
    @Bean
    public MybatisResultSetMetaInterceptor mybatisResultSetMetaInterceptor() {
        MybatisResultSetMetaInterceptor ret = new MybatisResultSetMetaInterceptor();
        Logger logger = LoggerFactory.getLogger(ret.getClass());
        ret.setInfoLogger(e -> logger.info(String.valueOf(e)));
        ret.setErrorLogger(e -> logger.error(String.valueOf(e)));
        return ret;
    }
}
