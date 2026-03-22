package i2f.jdbc.proxy.xml.mybatis.parameter.impl;

import i2f.jdbc.proxy.xml.mybatis.parameter.ParameterConvertor;

/**
 * @author Ice2Faith
 * @date 2026/2/3 16:45
 * @desc 对字符串类型的参数，进行字符串的反转义
 * 也就是将 '' 转义的单引号恢复为 ' 单个单引号
 */
public class UnescapeTextParameterConvertor implements ParameterConvertor {
    public static final UnescapeTextParameterConvertor INSTANCE = new UnescapeTextParameterConvertor();

    public static final String NAME = "unescape";

    @Override
    public Object convert(Object obj, String expr, boolean isDollar) {
        if (obj == null) {
            return obj;
        }
        if (!(obj instanceof CharSequence)) {
            return obj;
        }
        String str = String.valueOf(obj);
        str = str.replace("''", "'");
        return str;
    }

}
