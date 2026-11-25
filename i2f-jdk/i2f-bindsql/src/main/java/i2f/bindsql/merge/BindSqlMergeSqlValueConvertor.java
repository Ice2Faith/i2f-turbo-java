package i2f.bindsql.merge;

import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2026/11/25 10:35
 * @desc
 */
public interface BindSqlMergeSqlValueConvertor extends Function<Object, String> {
    boolean support(String dialectType);

}
