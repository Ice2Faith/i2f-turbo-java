package i2f.extension.easyexcel.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/2/2 10:07
 * @desc
 */
public class ExcelTaskUtil {

    public static <T extends Annotation> T getAnnotation(AnnotatedElement elem, Class<T> clazz) {
        T ann = elem.getDeclaredAnnotation(clazz);
        if (ann == null) {
            ann = elem.getAnnotation(clazz);
        }
        return ann;
    }

    public static <T extends Annotation> T[] getAnnotationRepeatable(AnnotatedElement elem, Class<T> clazz, T[] arr) {
        Set<T> set = new LinkedHashSet<>();
        T[] ann = elem.getDeclaredAnnotationsByType(clazz);
        if (ann != null) {
            for (T item : ann) {
                set.add(item);
            }
        }
        ann = elem.getAnnotationsByType(clazz);
        if (ann != null) {
            for (T item : ann) {
                set.add(item);
            }
        }
        T[] ret = set.toArray(arr);
        return ret;
    }

    public static Map<Field, Class<?>> getFields(Class<?> clazz, boolean withSuper, Predicate<Field> filter) {
        Map<Field, Class<?>> ret = new LinkedHashMap<>();
        getFieldsNext(ret, clazz, withSuper, filter);
        return ret;
    }

    private static void getFieldsNext(Map<Field, Class<?>> map, Class<?> clazz, boolean withSuper, Predicate<Field> filter) {
        if (map == null || clazz == null) {
            return;
        }
        if (Object.class.equals(clazz)) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null) {
            for (Field item : fields) {
                if (filter == null || filter.test(item)) {
                    map.put(item, clazz);
                }
            }
        }
        fields = clazz.getFields();
        if (fields != null) {
            for (Field item : fields) {
                if (filter == null || filter.test(item)) {
                    map.put(item, clazz);
                }
            }
        }
        if (!withSuper) {
            return;
        }
        Class<?> superclass = clazz.getSuperclass();
        getFieldsNext(map, superclass, withSuper, filter);
    }

}
