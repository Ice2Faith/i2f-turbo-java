package i2f.bindsql.count;

import i2f.bindsql.count.impl.SqlCountWrapper;

/**
 * @author Ice2Faith
 * @date 2024/4/28 15:22
 * @desc
 */
public class CountWrappers {
    public static final ICountWrapper SQL = new SqlCountWrapper();

    public static ICountWrapper wrapper() {
        return SQL;
    }

}
