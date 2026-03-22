package i2f.jdbc.proxy.xml.mybatis.parameter.impl;

import i2f.jdbc.proxy.xml.mybatis.parameter.ParameterConvertor;

/**
 * @author Ice2Faith
 * @date 2026/2/3 16:45
 * @desc 对字符串类型的参数，进行字符串的转义
 * 也就是将 ' 单个单引号转义为 '' 转义之后的字符串
 */
public class EscapeTextParameterConvertor implements ParameterConvertor {
    public static final EscapeTextParameterConvertor INSTANCE = new EscapeTextParameterConvertor();

    public static final String NAME = "escape";

    @Override
    public Object convert(Object obj, String expr, boolean isDollar) {
        if (obj == null) {
            return obj;
        }
        if (!(obj instanceof CharSequence)) {
            return obj;
        }
        String str = String.valueOf(obj);
        str = str.replace("'", "''");
        return str;
    }

}
