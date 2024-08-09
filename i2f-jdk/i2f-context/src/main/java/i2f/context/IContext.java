package i2f.context;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/8/9 8:36
 * @desc
 */
public interface IContext {
    <T> T getBean(Class<T> clazz);

    <T> List<T> getBeans(Class<T> clazz);

    List<Object> getAllBeans();
}
