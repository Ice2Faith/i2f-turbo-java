package i2f.bql.core.lambda.lambda.impl;


import i2f.annotations.db.Column;
import i2f.reflect.ReflectResolver;
import i2f.text.StringUtils;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/4/9 9:07
 * @desc
 */
public class DefaultFieldColumnNameResolver extends CachedFieldColumnNameResolver {

    public static final Function<Field, String> DEFAULT_NAME_RESOLVER = (field) -> {
        String name = field.getName();
        Column ann = ReflectResolver.getAnnotation(field, Column.class);
        if (ann == null) {
            return StringUtils.toUnderScore(name);
        }
        String value = ann.value();
        if (value != null && !value.isEmpty()) {
            return value;
        }
        return StringUtils.toUnderScore(name);
    };

    public static final Function<Field, String> ANNOTATION_NAME_RESOLVER = (field) -> {
        String name = field.getName();
        Column ann = ReflectResolver.getAnnotation(field, Column.class);
        if (ann == null) {
            return name;
        }
        String value = ann.value();
        if (value != null && !value.isEmpty()) {
            return value;
        }
        return name;
    };

    public static final Function<Field, String> UNDERSCORE_NAME_RESOLVER = (field) -> {
        String name = field.getName();
        return StringUtils.toUnderScore(name);
    };

    public static final DefaultFieldColumnNameResolver DEFAULT = new DefaultFieldColumnNameResolver(DEFAULT_NAME_RESOLVER);
    public static final DefaultFieldColumnNameResolver ANNOTATION = new DefaultFieldColumnNameResolver(ANNOTATION_NAME_RESOLVER);
    public static final DefaultFieldColumnNameResolver UNDERSCORE = new DefaultFieldColumnNameResolver(UNDERSCORE_NAME_RESOLVER);

    public DefaultFieldColumnNameResolver(Function<Field, String> nameResolver) {
        super(nameResolver);
    }

    public DefaultFieldColumnNameResolver(boolean upperCase, Function<Field, String> nameResolver) {
        super(upperCase, nameResolver);
    }

}
