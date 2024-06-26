package i2f.springboot.jackson.sensible;

import i2f.extension.jackson.sensible.handler.ISensibleHandler;
import i2f.extension.jackson.sensible.holder.SensibleHandlersHolder;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/26 22:20
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.jackson.sensible.enable:true}")
@ConfigurationProperties(prefix = "i2f.springboot.jackson.sensible")
public class JacksonSensibleAutoConfiguration implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        refresh();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        refresh();
    }

    public static void refresh(){
        try {
            Map<String, ISensibleHandler> beans = getBeans(ISensibleHandler.class);
            SensibleHandlersHolder.GLOBAL_HANDLERS.clear();
            SensibleHandlersHolder.GLOBAL_HANDLERS.addAll(beans.values());
        } catch (Exception e) {

        }
    }

    public static <T> Map<String, T> getBeans(Class<T> clazz) {
        Map<String, T> ret = new HashMap<>();
        if(context==null){
            return ret;
        }
        String[] names = context.getBeanNamesForType(clazz);
        for (String name : names) {
            Object bean = context.getBean(name);
            ret.put(name, (T) bean);
        }
        return ret;
    }
}
