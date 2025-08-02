package i2f.springboot.mybatis;

import i2f.extension.mybatis.interceptor.MybatisRecordSqlInterceptor;
import i2f.extension.mybatis.interceptor.MybatisResultSetMetaInterceptor;
import i2f.extension.mybatis.proxy.handler.MybatisRecordSqlProxyHandler;
import i2f.extension.mybatis.proxy.handler.MybatisResultSetMetaProxyHandler;
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
    protected boolean enablePrintRecordSql = false;

    @ConditionalOnExpression("${i2f.mybatis.interceptor.record-sql.enable:true}")
    @Bean
    public MybatisRecordSqlInterceptor mybatisRecordSqlInterceptor() {
        MybatisRecordSqlProxyHandler handler = new MybatisRecordSqlProxyHandler();
        Logger logger = LoggerFactory.getLogger(handler.getClass());
        handler.setInfoLogger(e -> logger.info(String.valueOf(e)));
        handler.setErrorLogger(e -> logger.error(String.valueOf(e)));
        handler.setEnablePrintSql(enablePrintRecordSql);

        MybatisRecordSqlInterceptor ret = new MybatisRecordSqlInterceptor(handler);

        return ret;
    }

    @ConditionalOnExpression("${i2f.mybatis.interceptor.result-set-meta.enable:true}")
    @Bean
    public MybatisResultSetMetaInterceptor mybatisResultSetMetaInterceptor() {
        MybatisResultSetMetaProxyHandler handler = new MybatisResultSetMetaProxyHandler();
        Logger logger = LoggerFactory.getLogger(handler.getClass());
        handler.setInfoLogger(e -> logger.info(String.valueOf(e)));
        handler.setErrorLogger(e -> logger.error(String.valueOf(e)));

        MybatisResultSetMetaInterceptor ret = new MybatisResultSetMetaInterceptor();

        return ret;
    }
}
