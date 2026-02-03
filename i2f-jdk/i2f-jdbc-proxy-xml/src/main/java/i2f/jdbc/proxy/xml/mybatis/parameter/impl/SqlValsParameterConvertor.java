package i2f.jdbc.proxy.xml.mybatis.parameter.impl;

import i2f.jdbc.proxy.xml.mybatis.parameter.ParameterConvertor;

import java.math.BigDecimal;

/**
 * @author Ice2Faith
 * @date 2026/2/3 16:45
 * @desc 校验参数是否是值序列
 * 形如：123,456,789 这种数值序列
 * 或者：'abc','def','ghl' 这种字符序列
 */
public class SqlValsParameterConvertor implements ParameterConvertor {
    public static final SqlValsParameterConvertor INSTANCE = new SqlValsParameterConvertor();

    public static final String NAME = "sql-vals";

    @Override
    public Object convert(Object obj, String expr, boolean isDollar) {
        if (!isDollar) {
            return obj;
        }
        if (obj == null) {
            return obj;
        }
        if (!(obj instanceof CharSequence)) {
            return obj;
        }
        String str = String.valueOf(obj);
        assertVars(str);
        return obj;
    }

    public void assertVars(String sql) {
        if (sql == null || sql.isEmpty()) {
            return;
        }
        sql = sql.trim();
        if (sql.startsWith("'")) {
            if (!sql.endsWith("'")) {
                throw new IllegalArgumentException("expect string, but isn't string! ");
            }
            sql = sql.substring(1, sql.length() - 1);

            String[] arr = sql.split("'\\s*,\\s*'");
            for (String item : arr) {
                item = item.replace("''", "");
                if (item.contains("'")) {
                    throw new IllegalArgumentException("expect string, but isn't string! ");
                }
            }
        } else {
            String[] arr = sql.split("\\s*,\\s*");
            for (String item : arr) {
                item = item.trim();
                if (!item.isEmpty()) {
                    try {
                        BigDecimal num = new BigDecimal(item);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("expect number, but isn't number! cause :" + e.getMessage(), e);
                    }
                }
            }
        }
    }
}
