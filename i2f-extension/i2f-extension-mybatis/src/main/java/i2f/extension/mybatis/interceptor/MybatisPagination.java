package i2f.extension.mybatis.interceptor;

import i2f.database.type.DatabaseType;
import i2f.page.ApiOffsetSize;
import i2f.page.Page;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/7/5 17:43
 * @desc
 */
public class MybatisPagination {

    protected static ThreadLocal<Boolean> DISABLED_COUNT = new ThreadLocal<>();
    protected static ThreadLocal<ApiOffsetSize> PAGE_HOLDER = new ThreadLocal<>();
    protected static ThreadLocal<Long> TOTAL_HOLDER = new ThreadLocal<>();
    protected static ThreadLocal<DatabaseType> DATABASE_HOLDER = new ThreadLocal<>();

    public static void disableCount() {
        DISABLED_COUNT.set(true);
    }

    public static void enableCount() {
        DISABLED_COUNT.remove();
    }

    public static boolean isDisabledCount() {
        Boolean ok = DISABLED_COUNT.get();
        return ok != null && ok;
    }

    public static ApiOffsetSize getPage() {
        return PAGE_HOLDER.get();
    }

    public static ApiOffsetSize getPageAndClear() {
        ApiOffsetSize ret = PAGE_HOLDER.get();
        PAGE_HOLDER.remove();
        return ret;
    }

    public static void startPage(int offset, int size) {
        startPage(ApiOffsetSize.of(offset, size));
    }

    public static void startPage(ApiOffsetSize page) {
        PAGE_HOLDER.set(page);
        TOTAL_HOLDER.remove();
    }

    public static void clearPage() {
        PAGE_HOLDER.remove();
        TOTAL_HOLDER.remove();
    }

    public static Long getTotal() {
        return TOTAL_HOLDER.get();
    }

    public static Long getTotalAndClear() {
        Long ret = TOTAL_HOLDER.get();
        TOTAL_HOLDER.remove();
        return ret;
    }

    public static void setTotal(Long count) {
        TOTAL_HOLDER.set(count);
    }

    public static void clearTotal() {
        TOTAL_HOLDER.remove();
    }

    public static DatabaseType getDatabaseType() {
        return DATABASE_HOLDER.get();
    }

    public static DatabaseType getDatabaseTypeAndClear() {
        DatabaseType ret = DATABASE_HOLDER.get();
        DATABASE_HOLDER.remove();
        return ret;
    }

    public static void setDatabaseType(DatabaseType databaseType) {
        DATABASE_HOLDER.set(databaseType);
    }

    public static void clearDatabaseType() {
        DATABASE_HOLDER.remove();
    }

    public static <T> Page<T> ofPage(List<T> list) {
        return Page.of(getPageAndClear(), getTotalAndClear(), list);
    }
}
