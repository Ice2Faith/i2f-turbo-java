package i2f.bindsql.merge.impl;

import i2f.bindsql.merge.BindSqlMergeSqlValueConvertor;

/**
 * @author Ice2Faith
 * @date 2026/11/25 10:41
 * @desc
 */
public class MySqlMergeSqlValueConvertor extends BasicMergeSqlValueConvertor implements BindSqlMergeSqlValueConvertor {
    public static final MySqlMergeSqlValueConvertor INSTANCE = new MySqlMergeSqlValueConvertor();

    @Override
    public boolean support(String dialectType) {
        if (dialectType == null) {
            return false;
        }
        dialectType = dialectType.trim().toLowerCase();
        if (dialectType.isEmpty()) {
            return false;
        }
        // mysql / maria / gbase
        return dialectType.contains("mysql")
                || dialectType.contains("maria")
                || dialectType.contains("gbase");
    }

}
