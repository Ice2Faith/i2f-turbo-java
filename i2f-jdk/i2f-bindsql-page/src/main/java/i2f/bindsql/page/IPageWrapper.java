package i2f.bindsql.page;

import i2f.bindsql.BindSql;
import i2f.page.ApiOffsetSize;

import java.util.function.BiFunction;

/**
 * @author Ice2Faith
 * @date 2024/4/28 9:06
 * @desc
 */
@FunctionalInterface
public interface IPageWrapper {
    static IPageWrapper ofFunction(BiFunction<BindSql, ApiOffsetSize, BindSql> function) {
        return (bql, page, embed) -> function.apply(bql, page);
    }

    default BiFunction<BindSql, ApiOffsetSize, BindSql> toFunction(boolean embed) {
        return (bql, page) -> apply(bql, page, embed);
    }

    default String apply(String sql, ApiOffsetSize page) {
        return apply(new BindSql(sql), page, true).getSql();
    }

    default BindSql apply(BindSql bql, ApiOffsetSize page) {
        return apply(bql, page, false);
    }

    /**
     * @param bql
     * @param page
     * @param embed 是否直接嵌入语句，不使用参数化
     * @return
     */
    BindSql apply(BindSql bql, ApiOffsetSize page, boolean embed);
}
