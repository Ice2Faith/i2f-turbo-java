package i2f.bindsql.merge.impl;

import i2f.bindsql.merge.BindSqlMergeSqlValueConvertor;

/**
 * @author Ice2Faith
 * @date 2026/11/25 10:41
 * @desc
 */
public class DefaultMergeSqlValueConvertor extends BasicMergeSqlValueConvertor implements BindSqlMergeSqlValueConvertor {
    public static final DefaultMergeSqlValueConvertor INSTANCE = new DefaultMergeSqlValueConvertor();

    @Override
    public boolean support(String dialectType) {
        return true;
    }

}
