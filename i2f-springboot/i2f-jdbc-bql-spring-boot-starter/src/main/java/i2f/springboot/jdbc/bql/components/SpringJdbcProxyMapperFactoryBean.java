package i2f.springboot.jdbc.bql.components;

import i2f.jdbc.context.JdbcInvokeContextProvider;
import i2f.jdbc.proxy.ProxySqlExecuteGenerator;
import i2f.jdbc.proxy.provider.ProxyRenderSqlProvider;
import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

/**
 * @author Ice2Faith
 * @date 2024/6/7 16:39
 * @desc
 */
@Data
public class SpringJdbcProxyMapperFactoryBean<T> implements FactoryBean<T> {
    private Class<T> mapperClass;
    private ApplicationContext context;

    @Override
    public T getObject() throws Exception {
        JdbcInvokeContextProvider<?> contextProvider = context.getBean(JdbcInvokeContextProvider.class);
        ProxyRenderSqlProvider sqlProvider = context.getBean(ProxyRenderSqlProvider.class);
        T ret = ProxySqlExecuteGenerator.proxy(mapperClass, contextProvider, sqlProvider);
        return ret;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Class<?> getObjectType() {
        return mapperClass;
    }
}
