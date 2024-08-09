package i2f.context;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/8/9 8:43
 * @desc
 */
public interface INamingContext extends IContext {
    <T> T getBean(String name);

    <T> Map<String, T> getBeansMap(Class<T> clazz);

    Map<String, Object> getAllBeansMap();
}
