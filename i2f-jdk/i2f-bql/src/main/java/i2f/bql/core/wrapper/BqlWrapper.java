package i2f.bql.core.wrapper;

import i2f.bindsql.BindSql;
import i2f.bql.core.Bql;

import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/12/5 8:34
 */
public interface BqlWrapper extends Supplier<Bql<?>> {
    default BindSql bindSql() {
        return get().$$();
    }
}
