package i2f.jdbc.proxy.xml.mybatis.parameter.impl;

import i2f.bindsql.BindSql;
import i2f.jdbc.proxy.xml.mybatis.parameter.ParameterConvertor;

import java.lang.reflect.Array;

/**
 * @author Ice2Faith
 * @date 2026/2/3 21:23
 * @desc
 */
public class ValValsParameterConvertor implements ParameterConvertor {
    public static final ValValsParameterConvertor INSTANCE = new ValValsParameterConvertor();
    public static final String NAME = "v-vals";

    @Override
    public Object convert(Object obj, String expr, boolean isDollar) {
        if (obj == null) {
            throw new IllegalArgumentException("only support iterable vals.");
        }
        if (isDollar) {
            throw new IllegalArgumentException("only support #{} placeholder.");
        }
        BindSql bql = BindSql.of();
        if (obj instanceof Iterable) {
            Iterable<?> arr = (Iterable<?>) obj;
            boolean isFirst = true;
            for (Object item : arr) {
                if (!isFirst) {
                    bql = bql.add(",");
                }
                bql = bql.add("?", item);
                isFirst = false;
            }
        } else if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            boolean isFirst = true;
            for (int i = 0; i < length; i++) {
                Object item = Array.get(obj, i);
                if (!isFirst) {
                    bql = bql.add(",");
                }
                bql = bql.add("?", item);
                isFirst = false;
            }
        } else {
            throw new IllegalArgumentException("only support iterable vals.");
        }
        return bql;
    }
}
