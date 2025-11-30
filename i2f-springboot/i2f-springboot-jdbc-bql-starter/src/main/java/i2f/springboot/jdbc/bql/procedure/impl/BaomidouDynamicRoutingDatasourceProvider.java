package i2f.springboot.jdbc.bql.procedure.impl;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import i2f.context.std.INamingContext;
import i2f.jdbc.procedure.datasource.DataSourceProvider;
import i2f.spring.core.SpringContext;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/11/30 14:54
 */
public class BaomidouDynamicRoutingDatasourceProvider implements DataSourceProvider {
    public static final int ORDER=200;
    protected INamingContext namingContext;

    public BaomidouDynamicRoutingDatasourceProvider(INamingContext namingContext) {
        this.namingContext = namingContext;
    }

    public BaomidouDynamicRoutingDatasourceProvider(ApplicationContext applicationContext) {
        this.namingContext = new SpringContext(applicationContext);
    }

    @Override
    public Map<String, DataSource> getDataSources() {
        DynamicRoutingDataSource bean = namingContext.getBean(DynamicRoutingDataSource.class);
        Map<String, DataSource> ret = bean.getDataSources();
        return new LinkedHashMap<>(ret);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
