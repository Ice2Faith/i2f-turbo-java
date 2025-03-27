package i2f.spring.core;

import i2f.context.std.IWritableNamingContext;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/8/9 10:41
 * @desc
 */
@Data
@NoArgsConstructor
public class SpringContext implements IWritableNamingContext, ApplicationContextAware, BeanFactoryAware {
    protected ApplicationContext applicationContext;
    protected BeanFactory beanFactory;

    public SpringContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.beanFactory=applicationContext;
    }

    public SpringContext(ApplicationContext applicationContext, BeanFactory beanFactory) {
        this.applicationContext = applicationContext;
        this.beanFactory = beanFactory;
    }

    @Override
    public void addBean(String name, Object bean) {
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) beanFactory;
        factory.autowireBean(bean);
        factory.registerSingleton(name, bean);
    }

    @Override
    public void removeBean(String name) {
        if (beanFactory.containsBean(name)) {
            ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) context.getBeanFactory();
            registry.removeBeanDefinition(name);
        }
    }

    @Override
    public <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    @Override
    public <T> Map<String, T> getBeansMap(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    @Override
    public <T> List<T> getBeans(Class<T> clazz) {
        Map<String, T> map = applicationContext.getBeansOfType(clazz);
        return new ArrayList<>(map.values());
    }

    @Override
    public List<Object> getAllBeans() {
        String[] names = applicationContext.getBeanDefinitionNames();
        List<Object> ret = new ArrayList<>();
        for (String name : names) {
            ret.add(applicationContext.getBean(name));
        }
        return ret;
    }

    @Override
    public Map<String, Object> getAllBeansMap() {
        String[] names = applicationContext.getBeanDefinitionNames();
        Map<String, Object> ret = new LinkedHashMap<>();
        for (String name : names) {
            ret.put(name, applicationContext.getBean(name));
        }
        return ret;
    }
}
