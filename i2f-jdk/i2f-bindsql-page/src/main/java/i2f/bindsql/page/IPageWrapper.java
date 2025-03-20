package i2f.bindsql.page;

import i2f.bindsql.BindSql;
import i2f.page.ApiOffsetSize;

import java.util.function.BiFunction;

/**
 * @author Ice2Faith
 * @date 2024/4/28 9:06
 * @desc
 */
public interface IPageWrapper extends BiFunction<BindSql, ApiOffsetSize, BindSql> {


}
