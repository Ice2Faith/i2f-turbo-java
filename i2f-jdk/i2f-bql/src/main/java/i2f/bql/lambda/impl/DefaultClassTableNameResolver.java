package i2f.bql.lambda.impl;


import i2f.annotations.db.Table;
import i2f.reflect.ReflectResolver;
import i2f.text.StringUtils;

import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/4/9 9:07
 * @desc
 */
public class DefaultClassTableNameResolver extends CachedClassTableNameResolver {

    public static final Function<Class<?>, String> DEFAULT_NAME_RESOLVER = (clazz) -> {
        String name = clazz.getSimpleName();
        Table ann = ReflectResolver.getAnnotation(clazz, Table.class);
        if (ann == null) {
            return StringUtils.toUnderScore(name);
        }
        String value = ann.value();
        if (value != null && !"".equals(value)) {
            return value;
        }
        return StringUtils.toUnderScore(name);
    };

    public static final Function<Class<?>, String> ANNOTATION_NAME_RESOLVER = (clazz) -> {
        String name = clazz.getSimpleName();
        Table ann = ReflectResolver.getAnnotation(clazz, Table.class);
        if (ann == null) {
            return name;
        }
        String value = ann.value();
        if (value != null && !"".equals(value)) {
            return value;
        }
        return name;
    };

    public static final Function<Class<?>, String> UNDERSCORE_NAME_RESOLVER = (clazz) -> {
        String name = clazz.getSimpleName();
        return StringUtils.toUnderScore(name);
    };

    public static final DefaultClassTableNameResolver DEFAULT = new DefaultClassTableNameResolver(DEFAULT_NAME_RESOLVER);
    public static final DefaultClassTableNameResolver ANNOTATION = new DefaultClassTableNameResolver(ANNOTATION_NAME_RESOLVER);
    public static final DefaultClassTableNameResolver UNDERSCORE = new DefaultClassTableNameResolver(UNDERSCORE_NAME_RESOLVER);

    public DefaultClassTableNameResolver(Function<Class<?>, String> nameResolver) {
        super(nameResolver);
    }

    public DefaultClassTableNameResolver(boolean upperCase, Function<Class<?>, String> nameResolver) {
        super(upperCase, nameResolver);
    }


}
