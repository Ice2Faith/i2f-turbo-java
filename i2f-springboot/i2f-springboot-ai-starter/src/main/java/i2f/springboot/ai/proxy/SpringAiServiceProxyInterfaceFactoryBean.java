package i2f.springboot.ai.proxy;

import i2f.ai.std.service.proxy.AiServiceDynamicProxyHandler;
import i2f.ai.std.service.proxy.AiServices;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

/**
 * @author Ice2Faith
 * @date 2025/7/23 14:04
 */
public class SpringAiServiceProxyInterfaceFactoryBean<T> implements FactoryBean<T> {
    private Class<T> serviceClass;
    private ApplicationContext context;

    @Override
    public T getObject() throws Exception {
        AiServiceDynamicProxyHandler handler = context.getBean(AiServiceDynamicProxyHandler.class);
        return AiServices.create(serviceClass, handler);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Class<?> getObjectType() {
        return serviceClass;
    }
}

