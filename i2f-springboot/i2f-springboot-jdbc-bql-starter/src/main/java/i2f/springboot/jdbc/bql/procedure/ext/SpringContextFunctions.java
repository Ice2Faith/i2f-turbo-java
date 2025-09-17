package i2f.springboot.jdbc.bql.procedure.ext;

import i2f.reflect.ReflectResolver;
import org.springframework.context.ApplicationContext;

/**
 * @author Ice2Faith
 * @date 2025/9/17 9:49
 */
public class SpringContextFunctions {
    public static volatile ApplicationContext applicationContext;

    public static Object spring_bean(String beanName) {
        try {
            return applicationContext.getBean(beanName);
        } catch (Exception e) {

        }
        return null;
    }


    public static Class<?> class_of(Object type) {
        if (type == null) {
            type = Object.class;
        }
        Class<?> objClass = Object.class;
        if (type instanceof Class) {
            objClass = (Class<?>) type;
        } else {
            String className = String.valueOf(type);
            objClass = ReflectResolver.loadClass(className);
        }
        if (objClass == null) {
            objClass = Object.class;
        }
        return objClass;
    }

    public static Object spring_type_bean(Object type) {
        try {
            return applicationContext.getBean(class_of(type));
        } catch (Exception e) {

        }
        return null;
    }
}
