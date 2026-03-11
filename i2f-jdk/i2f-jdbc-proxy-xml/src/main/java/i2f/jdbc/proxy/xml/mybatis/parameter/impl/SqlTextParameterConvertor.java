package i2f.jdbc.proxy.xml.mybatis.parameter.impl;

import i2f.jdbc.proxy.xml.mybatis.parameter.ParameterConvertor;

/**
 * @author Ice2Faith
 * @date 2026/2/3 16:45
 * @desc 将字符串转义之后变为SQL中的字符串表示法
 * 也就是使用单引号包裹，内部的单引号进行转义
 */
public class SqlTextParameterConvertor implements ParameterConvertor {
    public static final SqlTextParameterConvertor INSTANCE = new SqlTextParameterConvertor();

    public static final String NAME = "sql-text";

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
        return "'" + str + "'";
    }

}
