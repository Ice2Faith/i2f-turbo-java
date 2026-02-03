package i2f.jdbc.proxy.xml.mybatis.parameter.impl;

import i2f.jdbc.proxy.xml.mybatis.parameter.ParameterConvertor;

/**
 * @author Ice2Faith
 * @date 2026/2/3 16:45
 * @desc 校验参数是否是数据库标识符
 * 形如：test_db."t_test".`c_column`
 * 或者：t_test.c_column
 * 也就是需要满足是模式名、表名、列名
 * 或者是全限定名
 */
public class SqlIdentifierParameterConvertor implements ParameterConvertor {
    public static final SqlIdentifierParameterConvertor INSTANCE = new SqlIdentifierParameterConvertor();

    public static final String NAME = "sql-identifiers";

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
        assertIdentifiers(str);
        return obj;
    }

    public void assertIdentifiers(String sql) {
        if (sql == null || sql.isEmpty()) {
            return;
        }
        sql = sql.trim();
        String[] arr = sql.split("\\.");
        for (String item : arr) {
            item = item.trim();
            if (item.isEmpty()) {
                throw new IllegalArgumentException("expect identifier, but isn't! ");
            }
            if (item.startsWith("\"")) {
                if (!item.endsWith("\"")) {
                    throw new IllegalArgumentException("expect identifier, but isn't! ");
                }
                item = item.substring(1, item.length() - 1);
            }
            if (item.startsWith("`")) {
                if (!item.endsWith("`")) {
                    throw new IllegalArgumentException("expect identifier, but isn't! ");
                }
                item = item.substring(1, item.length() - 1);
            }
            if (!item.matches("[a-zA-Z0-9_$]+")) {
                throw new IllegalArgumentException("expect identifier, but isn't! ");
            }
        }
    }
}
