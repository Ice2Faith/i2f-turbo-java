package i2f.springboot.jdbc.bql.procedure.proxy;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.proxy.JdkProxyUtil;
import i2f.springboot.jdbc.bql.procedure.proxy.handler.ProxyJdbcProcedureMapperHandler;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

/**
 * @author Ice2Faith
 * @date 2025/7/23 14:04
 */
public class SpringJdbcProcedureProxyMapperFactoryBean<T> implements FactoryBean<T> {
    private Class<T> mapperClass;
    private ApplicationContext context;

    @Override
    public T getObject() throws Exception {
        JdbcProcedureExecutor executor = context.getBean(JdbcProcedureExecutor.class);
        ProxyJdbcProcedureMapperHandler handler = new ProxyJdbcProcedureMapperHandler(mapperClass, executor);
        return JdkProxyUtil.proxy(mapperClass, handler);
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

