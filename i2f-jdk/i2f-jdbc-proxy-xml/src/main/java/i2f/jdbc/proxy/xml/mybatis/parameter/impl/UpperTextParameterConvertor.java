package i2f.jdbc.proxy.xml.mybatis.parameter.impl;

import i2f.jdbc.proxy.xml.mybatis.parameter.ParameterConvertor;

/**
 * @author Ice2Faith
 * @date 2026/2/3 16:45
 * @desc 对字符串类型的参数，进行 upper
 */
public class UpperTextParameterConvertor implements ParameterConvertor {
    public static final UpperTextParameterConvertor INSTANCE = new UpperTextParameterConvertor();

    public static final String NAME = "upper";

    @Override
    public Object convert(Object obj, String expr, boolean isDollar) {
        if (obj == null) {
            return obj;
        }
        if (!(obj instanceof CharSequence)) {
            return obj;
        }
        String str = String.valueOf(obj);
        return str.toUpperCase();
    }

}
