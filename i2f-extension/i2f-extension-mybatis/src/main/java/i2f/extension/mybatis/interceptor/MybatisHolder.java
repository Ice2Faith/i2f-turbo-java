package i2f.extension.mybatis.interceptor;

import i2f.extension.mybatis.data.ColumnMeta;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/6/26 15:39
 */
public class MybatisHolder {
    public static final ThreadLocal<Boolean> ENABLE_RECORD_SQL = ThreadLocal.withInitial(() -> false);

    public static final ThreadLocal<List<Map.Entry<String, Map.Entry<BoundSql, MappedStatement>>>> EXEC_SQL_LIST = new ThreadLocal<>();

    public static final ThreadLocal<Boolean> ENABLE_RECORD_META = ThreadLocal.withInitial(() -> false);

    public static final ThreadLocal<List<ColumnMeta>> EXEC_COLUMNS_META = new ThreadLocal<>();


    public static boolean isRecordingSql() {
        Boolean ok = MybatisHolder.ENABLE_RECORD_SQL.get();
        return ok != null && ok;
    }

    public static void startRecordMeta() {
        ENABLE_RECORD_META.set(true);
    }


    public static boolean isRecordingMeta() {
        Boolean ok = MybatisHolder.ENABLE_RECORD_META.get();
        return ok != null && ok;
    }

    public static void startRecordSql() {
        ENABLE_RECORD_SQL.set(true);
    }

    public static String stopRecordSql(boolean clear) {
        ENABLE_RECORD_SQL.set(false);
        if (clear) {
            return getLastSqlAndClear();
        }
        return getLastSql();
    }

    public static String getLastSql() {
        List<Map.Entry<String, Map.Entry<BoundSql, MappedStatement>>> list = EXEC_SQL_LIST.get();
        if (list == null) {
            return null;
        }
        Map.Entry<String, Map.Entry<BoundSql, MappedStatement>> entry = list.get(list.size() - 1);
        return entry.getKey();
    }

    public static String getLastSqlAndClear() {
        List<Map.Entry<String, Map.Entry<BoundSql, MappedStatement>>> list = EXEC_SQL_LIST.get();
        EXEC_SQL_LIST.remove();
        if (list == null) {
            return null;
        }
        Map.Entry<String, Map.Entry<BoundSql, MappedStatement>> entry = list.get(list.size() - 1);
        return entry.getKey();
    }

    public static void addSql(String sql, BoundSql boundSql, MappedStatement ms) {
        List<Map.Entry<String, Map.Entry<BoundSql, MappedStatement>>> list = EXEC_SQL_LIST.get();
        if (list == null) {
            list = new ArrayList<>();
            EXEC_SQL_LIST.set(list);
        }
        list.add(new AbstractMap.SimpleEntry<>(sql, new AbstractMap.SimpleEntry<>(boundSql, ms)));
    }

    public static List<ColumnMeta> getColumnsMetaAndClear() {
        List<ColumnMeta> ret = EXEC_COLUMNS_META.get();
        EXEC_COLUMNS_META.remove();
        return ret;
    }

    public static void setExecColumnsMeta(List<ColumnMeta> columns) {
        EXEC_COLUMNS_META.set(columns);
    }

}
