package i2f.spring.core;

import i2f.spring.enviroment.EnvironmentUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Ice2Faith
 * @date 2022/3/26 15:29
 * @desc
 */
public class SpringUtil implements
        ApplicationContextAware,
        BeanFactoryAware,
        EnvironmentAware,
        ResourceLoaderAware {

    private volatile ApplicationContext applicationContext;
    private CountDownLatch latchApplicationContext = new CountDownLatch(1);

    private volatile BeanFactory beanFactory;
    private CountDownLatch latchBeanFactory = new CountDownLatch(1);

    private volatile Environment environment;
    private CountDownLatch latchEnvironment = new CountDownLatch(1);

    private volatile ResourceLoader resourceLoader;
    private CountDownLatch latchResourceLoader = new CountDownLatch(1);

    private volatile BeanDefinitionRegistry beanDefinitionRegistry;
    private CountDownLatch latchBeanDefinitionRegistry = new CountDownLatch(1);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        if (this.applicationContext == null) {
            this.applicationContext = applicationContext;
            latchApplicationContext.countDown();
            beanDefinitionRegistry = (BeanDefinitionRegistry) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
            latchBeanDefinitionRegistry.countDown();
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.beanFactory == null) {
            this.beanFactory = beanFactory;
            latchBeanFactory.countDown();
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        if (this.environment == null) {
            this.environment = environment;
            latchEnvironment.countDown();
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        if (this.resourceLoader == null) {
            this.resourceLoader = resourceLoader;
            latchResourceLoader.countDown();
        }
    }

    public ApplicationContext getApplicationContext() {
        try {
            latchApplicationContext.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return applicationContext;
    }

    public BeanFactory getBeanFactory() {
        try {
            latchBeanFactory.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanFactory;
    }

    public Environment getEnvironment() {
        try {
            latchEnvironment.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return environment;
    }

    public ResourceLoader getResourceLoader() {
        try {
            latchResourceLoader.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceLoader;
    }

    public BeanDefinitionRegistry getBeanDefinitionRegistry() {
        try {
            latchBeanDefinitionRegistry.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanDefinitionRegistry;
    }


    public String getValue(String name) {
        return getEnvironment().getProperty(name);
    }


    public Map<String, Map<String, Object>> getEnvironmentMap() {
        return EnvironmentUtil.of(getEnvironment()).getEnvironmentProperties();
    }

    public Map<String, Object> getEnvironmentPropertiesWithPrefix(boolean keepPrefix, String prefix) {
        return EnvironmentUtil.of(getEnvironment()).getPropertiesWithPrefix(getEnvironmentMap(), keepPrefix, prefix);
    }

    public Resource getResource(String match) {
        return resourceLoader.getResource(match);
    }

    public Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    public <T> Map<String, T> getBeans(Class<T> clazz) {
        String[] beanNames = getApplicationContext().getBeanNamesForType(clazz);
        Map<String, T> ret = new HashMap<>(Math.max(beanNames.length, 10));
        for (String name : beanNames) {
            ret.put(name, getBean(name, clazz));
        }
        return ret;
    }

    public AutowireCapableBeanFactory getAutowireCapableBeanFactory() {
        return getApplicationContext().getAutowireCapableBeanFactory();
    }

    public void makeAutowireSupport(Object existBean, int autowireMode, boolean dependencyCheck) {
        getAutowireCapableBeanFactory().autowireBeanProperties(existBean, autowireMode, dependencyCheck);
    }

    public void makeAutowireSupport(Object existBean) {
        getAutowireCapableBeanFactory().autowireBeanProperties(existBean, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
    }

    public <T> T registerBean(String name, Class<T> clazz, Object... args) {
        return registerBean((ConfigurableApplicationContext) applicationContext, name, clazz, args);
    }

    public void registerSingletonBean(String name, Object bean) {
        registerSingletonBean((DefaultListableBeanFactory) beanFactory, name, bean);
    }

    public <T> T removeBean(String name) {
        return removeBean((ConfigurableApplicationContext) applicationContext, name);
    }

    public <T> T removeBean(ConfigurableApplicationContext applicationContext, String name) {
        T ret = null;
        if (applicationContext.containsBean(name)) {
            ret = (T) applicationContext.getBean(name);
            BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
            beanFactory.removeBeanDefinition(name);
        }
        return ret;
    }

    public void registerSingletonBean(DefaultListableBeanFactory beanFactory, String name, Object bean) {
        beanFactory.registerSingleton(name, bean);
    }

    public <T> T registerBean(ConfigurableApplicationContext applicationContext, String name, Class<T> clazz,
                              Object... args) {
        if (applicationContext.containsBean(name)) {
            Object bean = applicationContext.getBean(name);
            if (bean.getClass().isAssignableFrom(clazz)) {
                return (T) bean;
            } else {
                throw new RuntimeException("repeat bean register which name is :" + name);
            }
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        for (Object arg : args) {
            beanDefinitionBuilder.addConstructorArgValue(arg);
        }
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
        beanFactory.registerBeanDefinition(name, beanDefinition);
        return applicationContext.getBean(name, clazz);
    }

}

