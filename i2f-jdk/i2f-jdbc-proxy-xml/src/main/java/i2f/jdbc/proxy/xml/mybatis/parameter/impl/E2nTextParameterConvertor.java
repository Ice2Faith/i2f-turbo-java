package i2f.jdbc.proxy.xml.mybatis.parameter.impl;

import i2f.jdbc.proxy.xml.mybatis.parameter.ParameterConvertor;

/**
 * @author Ice2Faith
 * @date 2026/2/3 16:45
 * @desc 对字符串类型的参数，进行 如果空串 '' 则转换为 null
 */
public class E2nTextParameterConvertor implements ParameterConvertor {
    public static final E2nTextParameterConvertor INSTANCE = new E2nTextParameterConvertor();

    public static final String NAME = "e2n";

    @Override
    public Object convert(Object obj, String expr, boolean isDollar) {
        if (obj == null) {
            return obj;
        }
        if (!(obj instanceof CharSequence)) {
            return obj;
        }
        String str = String.valueOf(obj);
        return str.isEmpty() ? null : str;
    }

}
