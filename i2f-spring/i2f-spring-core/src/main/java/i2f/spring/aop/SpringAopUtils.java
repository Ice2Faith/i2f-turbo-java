package i2f.spring.aop;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * @author Ice2Faith
 * @date 2026/5/27 15:28
 * @desc
 */
@Data
@NoArgsConstructor
public class SpringAopUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    private static CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringAopUtils.applicationContext = applicationContext;
        latch.countDown();
    }

    public static ApplicationContext getApplicationContext() {
        try {
            latch.await();
        } catch (InterruptedException e) {

        }
        return applicationContext;
    }

    /**
     * 获取可能被代理的类的实际的类
     * 也就是通过代理动态生成的类，找到原始的类
     *
     * @param clazz 可能被代理的类
     * @return 实际的类
     */
    public static Class<?> getRealClass(Class<?> clazz) {
        return ProxyUtils.getProxyRealClass(clazz);
    }

    /**
     * 获取可能被代理的对象的实际的类
     * 也就是通过代理动态生成的对象，找到原始的类
     *
     * @param bean 可能被代理的对象
     * @return 实际的类
     */
    public static <T> Class<T> getRealClass(T bean) {
        return ProxyUtils.getProxyRealClass(bean);
    }

    /**
     * 获取这个对象在Spring容器中的实例对象
     * 也就是通过一个Bean对象，查找在Spring容器中这个类的实例对象
     * 因为是在Spring容器中查找，所以找到的类是可能被代理的
     * 也就是Aop事务注解功能这些功能是生效的
     *
     * @param <T>  对象类型
     * @param bean 实例对象
     * @return 容器对象
     */
    public static <T> T getProxyBean(T bean) {
        Class<?> clazz = getRealClass(bean);
        return (T) getApplicationContext().getBean(clazz);
    }

    /**
     * 获取这个对象的没有被代理的实际对象
     * 传入的类可能是被Spring代理的对象，返回的是没有被Spring代理的实际对象
     * 因此，返回的对象，可能Aop事务直接这些功能是失效的
     *
     * @param <T>  对象类型
     * @param bean 实例对象
     * @return 容器对象
     */
    public static <T> T getRealBean(T bean) {
        T ret = (T) AopProxyUtils.getSingletonTarget(bean);
        if (ret == null) {
            return bean;
        }
        return ret;
    }

    /**
     * 获取Spring容器中的一个指定类型的对象
     *
     * @param <T>   对象类型
     * @param clazz 类型
     * @return 找到的bean对象
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }
}
