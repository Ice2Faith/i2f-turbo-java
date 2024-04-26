package i2f.bql.lambda.impl;

import i2f.bql.lambda.IFieldColumnNameResolver;
import i2f.lru.LruMap;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/4/9 9:02
 * @desc
 */
public class CachedFieldColumnNameResolver implements IFieldColumnNameResolver {
    protected boolean upperCase = false;
    protected Function<Field, String> nameResolver;
    protected LruMap<Field, String> fastNameMap = new LruMap<>(8192);

    public CachedFieldColumnNameResolver(Function<Field, String> nameResolver) {
        this.nameResolver = nameResolver;
    }

    public CachedFieldColumnNameResolver(boolean upperCase, Function<Field, String> nameResolver) {
        this.upperCase = upperCase;
        this.nameResolver = nameResolver;
    }

    @Override
    public String getName(Field field) {
        if (field == null) {
            throw new IllegalArgumentException("field catch name error!");
        }
        String name = fastNameMap.get(field);
        if (name != null) {
            return name;
        }
        name = nameResolver.apply(field);
        if (upperCase) {
            name = name.toUpperCase();
        }
        fastNameMap.put(field, name);
        return name;
    }
}
