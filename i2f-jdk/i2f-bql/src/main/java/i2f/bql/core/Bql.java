package i2f.bql.core;


import i2f.bindsql.BindSql;
import i2f.bql.core.wrapper.DeleteWrapper;
import i2f.bql.core.wrapper.InsertWrapper;
import i2f.bql.core.wrapper.QueryWrapper;
import i2f.bql.core.wrapper.UpdateWrapper;
import i2f.container.builder.Builders;
import i2f.container.builder.collection.ListBuilder;
import i2f.container.builder.map.MapBuilder;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/4/7 16:44
 * @desc H: Inheritable
 */
public class Bql<H extends Bql<H>> {
    public static <H extends i2f.bql.core.map.Bql<H>> i2f.bql.core.map.Bql<H> $map() {
        return new i2f.bql.core.map.Bql();
    }

    public static <H extends i2f.bql.core.lambda.Bql<H>> i2f.bql.core.lambda.Bql<H> $lambda() {
        return new i2f.bql.core.lambda.Bql();
    }

    public static <H extends i2f.bql.core.bean.Bql<H>> i2f.bql.core.bean.Bql<H> $bean() {
        return new i2f.bql.core.bean.Bql();
    }

    public static InsertWrapper<?> $insertWrapper() {
        return new InsertWrapper<>();
    }

    public static DeleteWrapper<?> $deleteWrapper() {
        return new DeleteWrapper<>();
    }

    public static UpdateWrapper<?> $updateWrapper() {
        return new UpdateWrapper<>();
    }

    public static QueryWrapper<?> $queryWrapper() {
        return new QueryWrapper<>();
    }

    protected static List<String> TRIM_COMMA_LIST = Collections.unmodifiableList($one2list(","));
    protected static List<String> TRIM_UNION_LIST = Collections.unmodifiableList($ls("union all", "union", "UNION ALL", "UNION"));
    protected static List<String> TRIM_AND_OR_LIST = Collections.unmodifiableList($ls("and", "or", "AND", "OR"));
    protected static List<String> TRIM_COMMA_SM_LIST = Collections.unmodifiableList($ls(",", ";"));


    public static final Function<String, String> NONE_COLUMN_NAME_DECORATOR = (v) -> v;
    public static final Function<String, String> NONE_TABLE_NAME_DECORATOR = (v) -> v;
    public static final Function<String, String> NONE_COLUMN_AS_DECORATOR = (v) -> v;

    public static final Function<String, String> QUOTA_COLUMN_AS_DECORATOR = (v) -> {
        if (v.startsWith("\"")) {
            return v;
        } else {
            return "\"" + v + "\"";
        }
    };

    public static final Function<String, String> MYSQL_COLUMN_NAME_DECORATOR = (v) -> {
        if ("*".equalsIgnoreCase(v)) {
            return v;
        }
        if (v.startsWith("`")) {
            return v;
        } else {
            return "`" + v + "`";
        }
    };
    public static final Function<String, String> MYSQL_TABLE_NAME_DECORATOR = (v) -> {
        if ("dual".equalsIgnoreCase(v)) {
            return v;
        }
        if (v.startsWith("`")) {
            return v;
        } else {
            return "`" + v + "`";
        }
    };

    public static final Function<String, String> ORACLE_COLUMN_NAME_DECORATOR = (v) -> {
        if ("*".equalsIgnoreCase(v)) {
            return v;
        }
        if (v.startsWith("\"")) {
            return v;
        } else {
            return "\"" + v + "\"";
        }
    };
    public static final Function<String, String> ORACLE_TABLE_NAME_DECORATOR = (v) -> {
        if ("dual".equalsIgnoreCase(v)) {
            return v;
        }
        if (v.startsWith("\"")) {
            return v;
        } else {
            return "\"" + v + "\"";
        }
    };

    public static final Predicate<?> DEFAULT_FILTER = (val) -> {
        if (val == null) {
            return false;
        }
        if (val instanceof String) {
            return !((String) val).isEmpty();
        }
        if (val instanceof Collection) {
            return !((Collection) val).isEmpty();
        }
        if (val instanceof Map) {
            return !((Map) val).isEmpty();
        }
        Class<?> clazz = val.getClass();
        if (clazz.isArray()) {
            return Array.getLength(val) > 0;
        }
        return val != null;
    };


    public static Boolean GLOBAL_UPPER_KEYWORDS = false;
    public static Function<String, String> GLOBAL_COLUMN_NAME_DECORATOR = NONE_COLUMN_NAME_DECORATOR;
    public static Function<String, String> GLOBAL_TABLE_NAME_DECORATOR = NONE_TABLE_NAME_DECORATOR;
    public static Function<String, String> GLOBAL_COLUMN_AS_DECORATOR = NONE_COLUMN_AS_DECORATOR;

    public static ThreadLocal<Boolean> UPPER_KEYWORDS_HOLDER = ThreadLocal.withInitial(() -> false);
    public static ThreadLocal<Function<String, String>> COLUMN_NAME_DECORATOR_HOLDER = ThreadLocal.withInitial(() -> NONE_COLUMN_NAME_DECORATOR);
    public static ThreadLocal<Function<String, String>> TABLE_NAME_DECORATOR_HOLDER = ThreadLocal.withInitial(() -> NONE_TABLE_NAME_DECORATOR);
    public static ThreadLocal<Function<String, String>> COLUMN_AS_DECORATOR_HOLDER = ThreadLocal.withInitial(() -> NONE_COLUMN_AS_DECORATOR);


    public static <T> Predicate<T> defaultFilter() {
        return (Predicate<T>) DEFAULT_FILTER;
    }

    public static void globalMysqlMode() {
        GLOBAL_UPPER_KEYWORDS = false;
        GLOBAL_COLUMN_NAME_DECORATOR = MYSQL_COLUMN_NAME_DECORATOR;
        GLOBAL_TABLE_NAME_DECORATOR = MYSQL_TABLE_NAME_DECORATOR;
    }

    public static void globalOracleMode() {
        GLOBAL_UPPER_KEYWORDS = true;
        GLOBAL_COLUMN_NAME_DECORATOR = ORACLE_COLUMN_NAME_DECORATOR;
        GLOBAL_TABLE_NAME_DECORATOR = ORACLE_TABLE_NAME_DECORATOR;
    }

    public static void globalQuotaColumnAs() {
        GLOBAL_COLUMN_AS_DECORATOR = QUOTA_COLUMN_AS_DECORATOR;
    }

    public static void globalNoneColumnAs() {
        GLOBAL_COLUMN_AS_DECORATOR = NONE_COLUMN_AS_DECORATOR;
    }

    public static void globalReset() {
        GLOBAL_UPPER_KEYWORDS = false;
        GLOBAL_COLUMN_NAME_DECORATOR = NONE_COLUMN_NAME_DECORATOR;
        GLOBAL_TABLE_NAME_DECORATOR = NONE_TABLE_NAME_DECORATOR;
        GLOBAL_COLUMN_AS_DECORATOR = NONE_COLUMN_AS_DECORATOR;
    }

    public static void threadMysqlMode() {
        UPPER_KEYWORDS_HOLDER.set(false);
        COLUMN_NAME_DECORATOR_HOLDER.set(MYSQL_COLUMN_NAME_DECORATOR);
        TABLE_NAME_DECORATOR_HOLDER.set(MYSQL_TABLE_NAME_DECORATOR);
    }

    public static void threadOracleMode() {
        UPPER_KEYWORDS_HOLDER.set(true);
        COLUMN_NAME_DECORATOR_HOLDER.set(ORACLE_COLUMN_NAME_DECORATOR);
        TABLE_NAME_DECORATOR_HOLDER.set(ORACLE_TABLE_NAME_DECORATOR);
    }

    public static void threadQuotaColumnAs() {
        COLUMN_AS_DECORATOR_HOLDER.set(QUOTA_COLUMN_AS_DECORATOR);
    }

    public static void threadNoneColumnAs() {
        COLUMN_AS_DECORATOR_HOLDER.set(NONE_COLUMN_AS_DECORATOR);
    }

    public static void threadReset() {
        UPPER_KEYWORDS_HOLDER.set(false);
        COLUMN_NAME_DECORATOR_HOLDER.set(NONE_COLUMN_NAME_DECORATOR);
        TABLE_NAME_DECORATOR_HOLDER.set(NONE_TABLE_NAME_DECORATOR);
        COLUMN_AS_DECORATOR_HOLDER.set(NONE_COLUMN_AS_DECORATOR);
    }

    public H dialectGlobalMysql() {
        globalMysqlMode();
        refresh();
        return (H) this;
    }

    public H dialectGlobalOracle() {
        globalOracleMode();
        refresh();
        return (H) this;
    }

    public H dialectGlobalReset() {
        globalReset();
        refresh();
        return (H) this;
    }

    public H dialectThreadMysql() {
        threadMysqlMode();
        refresh();
        return (H) this;
    }

    public H dialectThreadOracle() {
        threadOracleMode();
        refresh();
        return (H) this;
    }

    public H dialectThreadReset() {
        threadReset();
        refresh();
        return (H) this;
    }

    public H dialectGlobalQuotaColumnAs() {
        globalQuotaColumnAs();
        refresh();
        return (H) this;
    }

    public H dialectGlobalNoneColumnAs() {
        globalNoneColumnAs();
        refresh();
        return (H) this;
    }

    public H dialectThreadQuotaColumnAs() {
        threadQuotaColumnAs();
        refresh();
        return (H) this;
    }

    public H dialectThreadNoneColumnAs() {
        threadNoneColumnAs();
        refresh();
        return (H) this;
    }

    protected LinkedList<BindSql> builder = new LinkedList<>();
    protected String link = "and";
    protected String separator = " ";
    protected String alias = null;
    protected String placeholder = "?";

    protected boolean upperKeywords = false;
    protected Function<String, String> columnNameDecorator = NONE_COLUMN_NAME_DECORATOR;
    protected Function<String, String> tableNameDecorator = NONE_TABLE_NAME_DECORATOR;
    protected Function<String, String> columnAsDecorator = NONE_COLUMN_AS_DECORATOR;

    public static final InheritableThreadLocal<String> localLink = new InheritableThreadLocal<>();
    public static final InheritableThreadLocal<String> localSeparator = new InheritableThreadLocal<>();
    public static final InheritableThreadLocal<String> localAlias = new InheritableThreadLocal<>();
    public static final InheritableThreadLocal<String> localPlaceHolder = new InheritableThreadLocal<>();
    public static final InheritableThreadLocal<Boolean> localUpperKeyWords = new InheritableThreadLocal<>();
    public static final InheritableThreadLocal<Function<String, String>> localColumnNameDecorator = new InheritableThreadLocal<>();
    public static final InheritableThreadLocal<Function<String, String>> localTableNameDecorator = new InheritableThreadLocal<>();
    public static final InheritableThreadLocal<Function<String, String>> localColumnAsDecorator = new InheritableThreadLocal<>();

    public Bql() {
        refresh();
    }

    public H refresh(){
        global();
        inherit();
        return (H) this;
    }

    public H global(){
        if (GLOBAL_UPPER_KEYWORDS != null) {
            this.upperKeywords = GLOBAL_UPPER_KEYWORDS;
        }
        if (UPPER_KEYWORDS_HOLDER.get() != null) {
            this.upperKeywords = UPPER_KEYWORDS_HOLDER.get();
        }

        if (GLOBAL_COLUMN_NAME_DECORATOR != null) {
            this.columnNameDecorator = GLOBAL_COLUMN_NAME_DECORATOR;
        }
        if (COLUMN_NAME_DECORATOR_HOLDER.get() != null) {
            this.columnNameDecorator = COLUMN_NAME_DECORATOR_HOLDER.get();
        }

        if (GLOBAL_TABLE_NAME_DECORATOR != null) {
            this.tableNameDecorator = GLOBAL_TABLE_NAME_DECORATOR;
        }
        if (TABLE_NAME_DECORATOR_HOLDER.get() != null) {
            this.tableNameDecorator = TABLE_NAME_DECORATOR_HOLDER.get();
        }

        if (GLOBAL_COLUMN_AS_DECORATOR != null) {
            this.columnAsDecorator = GLOBAL_COLUMN_AS_DECORATOR;
        }
        if (COLUMN_AS_DECORATOR_HOLDER.get() != null) {
            this.columnAsDecorator = COLUMN_AS_DECORATOR_HOLDER.get();
        }

        return (H)this;
    }

    public H inherit() {
        Optional.ofNullable(localLink.get()).ifPresent((v) -> link = v);
        Optional.ofNullable(localSeparator.get()).ifPresent((v) -> separator = v);
        Optional.ofNullable(localPlaceHolder.get()).ifPresent((v) -> placeholder = v);
        Optional.ofNullable(localUpperKeyWords.get()).ifPresent((v) -> upperKeywords = v);
        Optional.ofNullable(localColumnNameDecorator.get()).ifPresent((v) -> columnNameDecorator = v);
        Optional.ofNullable(localTableNameDecorator.get()).ifPresent((v) -> tableNameDecorator = v);
        Optional.ofNullable(localColumnAsDecorator.get()).ifPresent((v) -> columnAsDecorator = v);
        store();
        return (H) this;
    }

    public H store() {
        localLink.set(link);
        localSeparator.set(separator);
        localAlias.set(alias);
        localPlaceHolder.set(placeholder);
        localUpperKeyWords.set(upperKeywords);
        localColumnNameDecorator.set(columnNameDecorator);
        localTableNameDecorator.set(tableNameDecorator);
        localColumnAsDecorator.set(columnAsDecorator);
        return (H) this;
    }

    public H unset() {
        localLink.set(null);
        localSeparator.set(null);
        localAlias.set(null);
        localPlaceHolder.set(null);
        localUpperKeyWords.set(null);
        localColumnNameDecorator.set(null);
        localTableNameDecorator.set(null);
        localColumnAsDecorator.set(null);
        store();
        return (H) this;
    }

    public Function<String, String> columnNameDecorator() {
        return columnNameDecorator;
    }

    public H columnNameDecorator(Function<String, String> columnNameDecorator) {
        this.columnNameDecorator = columnNameDecorator;
        store();
        return (H) this;
    }

    public Function<String, String> tableNameDecorator() {
        return tableNameDecorator;
    }

    public H tableNameDecorator(Function<String, String> tableNameDecorator) {
        this.tableNameDecorator = tableNameDecorator;
        store();
        return (H) this;
    }

    public Function<String, String> columnAsDecorator() {
        return columnAsDecorator;
    }

    public H columnAsDecorator(Function<String, String> columnAsDecorator) {
        this.columnAsDecorator = columnAsDecorator;
        store();
        return (H) this;
    }

    public static <H extends Bql<H>> Bql<H> $_() {
        return (Bql<H>) new Bql();
    }

    public static <H extends Bql<H>> Bql<H> $_(BindSql bql) {
        return (Bql<H>) new Bql().$(bql);
    }

    public static <H extends Bql<H>> Bql<H> $_(Bql<?> val) {
        return (Bql<H>) new Bql().$(val);
    }

    public static <H extends Bql<H>> Bql<H> $_(Supplier<Bql<?>> caller) {
        return (Bql<H>) new Bql().$(caller);
    }

    public static <H extends Bql<H>> Bql<H> $_(String sql, Object args) {
        return (Bql<H>) new Bql().$(sql, args);
    }

    public BindSql $$() {
        if (builder.isEmpty()) {
            return new BindSql("");
        }
        if (builder.size() == 1) {
            return builder.get(0);
        }
        StringBuilder sql = new StringBuilder();
        List<Object> args = new ArrayList<>();
        for (BindSql bql : builder) {
            if (bql == null) {
                continue;
            }
            if (bql.getSql() != null) {
                sql.append(bql.getSql());
            }
            if (bql.getArgs() != null) {
                args.addAll(bql.getArgs());
            }
        }
        return new BindSql(sql.toString(), args);
    }

    public H addSeparator() {
        if (separator == null) {
            return (H) this;
        }
        if (builder.isEmpty()) {
            return (H) this;
        }
        BindSql last = builder.getLast();
        if (last.getArgs().isEmpty()) {
            if (separator.equals(last.getSql())) {
                return (H) this;
            }
            if (last.getSql().endsWith(separator)) {
                return (H) this;
            }
            if (last.getSql().trim().endsWith(separator)) {
                return (H) this;
            }
        }
        builder.add(new BindSql(separator));
        return (H) this;
    }

    public H $(Bql<?> val) {
        if (val != null) {
            addSeparator();
            builder.addAll(val.builder);
        }
        return (H) this;
    }

    public H $(Supplier<Bql<?>> caller) {
        if (caller != null) {
            return $(caller.get());
        }
        return (H) this;
    }

    public H $(BindSql bql) {
        if (bql == null) {
            return (H) this;
        }
        addSeparator();
        builder.add(bql);
        return (H) this;
    }

    public H $(String sql, Object... args) {
        return $(new BindSql(sql, $arr2list(args)));
    }

    public H $(Appendable appendable) {
        return $(appendable.toString());
    }

    public H $(StringBuilder builder) {
        return $(builder.toString());
    }

    public H $(StringBuffer buffer) {
        return $(buffer.toString());
    }


    public H $clear() {
        this.builder.clear();
        return (H) this;
    }

    public BindSql $getAndClear() {
        BindSql bql = this.$$();
        this.$clear();
        return bql;
    }

    public H $ensureBefore(String str) {
        if (this.builder.isEmpty()) {
            return $(str);
        }
        if (!this.builder.isEmpty()) {
            BindSql last = this.builder.getLast();
            String sql = last.getSql();
            if (sql.endsWith(str)) {
                return (H) this;
            }
            sql = sql.trim();
            if (sql.endsWith(str)) {
                return (H) this;
            }
        }

        BindSql bql = this.$getAndClear();
        String sql = bql.getSql();
        if (sql.endsWith(str)) {
            return (H) this;
        }

        sql = sql.trim();
        if (sql.endsWith(str)) {
            return (H) this;
        }

        bql.setSql(sql);
        $(bql);
        $(str);
        return (H) this;
    }

    public H $trimBefore(Collection<String> strs) {
        if (this.builder.isEmpty()) {
            return (H) this;
        }
        if (!this.builder.isEmpty()) {
            BindSql last = this.builder.getLast();
            String sql = last.getSql();
            for (String str : strs) {
                if (str == null) {
                    continue;
                }
                if (sql.endsWith(str)) {
                    sql = sql.substring(0, sql.length() - str.length());
                    last.setSql(sql);
                    return (H) this;
                }
            }
            sql = sql.trim();
            for (String str : strs) {
                if (str == null) {
                    continue;
                }
                if (sql.endsWith(str)) {
                    sql = sql.substring(0, sql.length() - str.length());
                    last.setSql(sql);
                    return (H) this;
                }
            }
        }

        BindSql bql = this.$getAndClear();
        String sql = bql.getSql();
        for (String str : strs) {
            if (str == null) {
                continue;
            }
            if (sql.endsWith(str)) {
                sql = sql.substring(0, sql.length() - str.length());
                bql.setSql(sql);
                return $(bql);
            }
        }
        sql = sql.trim();
        for (String str : strs) {
            if (str == null) {
                continue;
            }
            if (sql.endsWith(str)) {
                sql = sql.substring(0, sql.length() - str.length());
                bql.setSql(sql);
                return $(bql);
            }
        }
        return $(bql);
    }

    public H $trimBefore(String... strs) {
        return $trimBefore($arr2list(strs));
    }

    public <T> H $if(T val, Predicate<T> filter, Function<T, Bql<?>> caller) {
        if (filter != null && !filter.test(val)) {
            return (H) this;
        }
        return $(caller.apply(val));
    }

    public <T> H $if(T val, Function<T, Bql<?>> caller) {
        return $if(val, defaultFilter(), caller);
    }

    public <T> H $trim(T val, Predicate<T> filter,
                       String trimPrefixes, String trimSuffixes,
                       String appendPrefix, String appendSuffix,
                       Function<T, Bql<?>> caller) {
        return $trim(val, filter,
                $one2list(trimPrefixes), $one2list(trimSuffixes),
                appendPrefix, appendSuffix,
                caller);
    }

    public <T> H $trim(T val, Predicate<T> filter,
                       String[] trimPrefixes, String[] trimSuffixes,
                       String appendPrefix, String appendSuffix,
                       Function<T, Bql<?>> caller) {
        return $trim(val, filter,
                $arr2list(trimPrefixes), $arr2list(trimSuffixes),
                appendPrefix, appendSuffix,
                caller);
    }

    public <T> H $trim(T val, Predicate<T> filter,
                       Collection<String> trimPrefixes, Collection<String> trimSuffixes,
                       String appendPrefix, String appendSuffix,
                       Function<T, Bql<?>> caller) {
        if (filter != null && !filter.test(val)) {
            return (H) this;
        }
        BindSql bql = caller.apply(val).$$();
        String sql = bql.getSql();
        if (trimPrefixes != null) {
            for (String item : trimPrefixes) {
                if (item == null) {
                    continue;
                }
                sql = sql.trim();
                if (sql.startsWith(item)) {
                    sql = sql.substring(item.length());
                }
            }
        }
        if (trimSuffixes != null) {
            for (String item : trimSuffixes) {
                if (item == null) {
                    continue;
                }
                sql = sql.trim();
                if (sql.startsWith(item)) {
                    sql = sql.substring(0, sql.length() - item.length());
                }
            }
        }
        if (appendPrefix != null) {
            sql = sql.trim();
            if (!sql.isEmpty()) {
                sql = appendPrefix + " " + sql;
            }
        }
        if (appendSuffix != null) {
            sql = sql.trim();
            if (!sql.isEmpty()) {
                sql = sql + " " + appendSuffix;
            }
        }
        bql.setSql(sql);
        return $(bql);
    }

    public <T> H $trim(T val,
                       String trimPrefixes, String trimSuffixes,
                       String appendPrefix, String appendSuffix,
                       Function<T, Bql<?>> caller) {
        return $trim(val,
                $one2list(trimPrefixes), $one2list(trimSuffixes),
                appendPrefix, appendSuffix,
                caller);
    }

    public <T> H $trim(T val,
                       String[] trimPrefixes, String[] trimSuffixes,
                       String appendPrefix, String appendSuffix,
                       Function<T, Bql<?>> caller) {
        return $trim(val,
                $arr2list(trimPrefixes), $arr2list(trimSuffixes),
                appendPrefix, appendSuffix,
                caller);
    }

    public <T> H $trim(T val,
                       Collection<String> trimPrefixes, Collection<String> trimSuffixes,
                       String appendPrefix, String appendSuffix,
                       Function<T, Bql<?>> caller) {
        return $trim(val, defaultFilter(),
                trimPrefixes, trimSuffixes,
                appendPrefix, appendSuffix,
                caller);
    }

    public H $trim(String trimPrefixes, String trimSuffixes,
                   String appendPrefix, String appendSuffix,
                   Supplier<Bql<?>> caller) {
        return $trim($one2list(trimPrefixes), $one2list(trimSuffixes),
                appendPrefix, appendSuffix,
                caller);
    }

    public H $trim(String[] trimPrefixes, String[] trimSuffixes,
                   String appendPrefix, String appendSuffix,
                   Supplier<Bql<?>> caller) {
        return $trim($arr2list(trimPrefixes), $arr2list(trimSuffixes),
                appendPrefix, appendSuffix,
                caller);
    }

    public H $trim(Collection<String> trimPrefixes, Collection<String> trimSuffixes,
                   String appendPrefix, String appendSuffix,
                   Supplier<Bql<?>> caller) {
        return $trim(null, null,
                trimPrefixes, trimSuffixes,
                appendPrefix, appendSuffix,
                v -> caller.get());
    }

    public <T, C extends Collection<T>> H $for(C val, Predicate<C> filter,
                                               String separator,
                                               Predicate<T> itemFilter,
                                               BiFunction<Integer, T, Bql<?>> itemCaller) {
        if (filter != null && !filter.test(val)) {
            return (H) this;
        }
        Bql<?> next = Bql.$_().$sep(separator);
        int idx = 0;
        for (T item : val) {
            if (itemFilter != null && !itemFilter.test(item)) {
                idx++;
                continue;
            }
            Bql<?> one = itemCaller.apply(idx, item);
            next.$(one);
            idx++;
        }
        return $(next);
    }

    public <T, C extends Collection<T>> H $for(C val,
                                               String separator,
                                               Predicate<T> itemFilter,
                                               BiFunction<Integer, T, Bql<?>> itemCaller) {
        return $for(val, defaultFilter(), separator, itemFilter, itemCaller);
    }

    public <T, C extends Collection<T>> H $forUnion(C val,
                                                    Predicate<T> itemFilter,
                                                    BiFunction<Integer, T, Bql<?>> itemCaller) {
        return $trim(val, TRIM_UNION_LIST,
                TRIM_UNION_LIST,
                null, null,
                (list) -> Bql.$_()
                        .$for(list, $keywords(" union "), itemFilter,
                                itemCaller
                        )
        );
    }

    public <T, C extends Collection<T>> H $forUnionAll(C val,
                                                       Predicate<T> itemFilter,
                                                       BiFunction<Integer, T, Bql<?>> itemCaller) {
        return $trim(val, TRIM_UNION_LIST,
                TRIM_UNION_LIST,
                null, null,
                (list) -> Bql.$_()
                        .$for(list, $keywords(" union all "), itemFilter,
                                itemCaller
                        )
        );
    }


    public H $each(Supplier<Bql>... suppliers) {
        return $each($arr2list(suppliers));
    }

    public H $each(Collection<Supplier<Bql>> suppliers) {
        for (Supplier<Bql> supplier : suppliers) {
            Bql<?> next = supplier.get();
            $(next);
        }
        return (H) this;
    }

    public H $format(String format, Object... args) {
        return $(String.format(format, args));
    }

    public H $concat(Object... args) {
        StringBuilder builder = new StringBuilder();
        for (Object item : args) {
            builder.append(item);
        }
        return $(builder.toString());
    }

    public H $var(Object val) {
        return $(placeholder, val);
    }

    public static <T> List<T> $ls(T... arr) {
        List<T> ret = new ArrayList<>();
        if (arr == null) {
            return ret;
        }
        for (T item : arr) {
            ret.add(item);
        }
        return ret;
    }

    public static <T> List<T> $arr2list(T[] arr) {
        List<T> ret = new ArrayList<>();
        if (arr == null) {
            return ret;
        }
        for (T item : arr) {
            ret.add(item);
        }
        return ret;
    }

    public static <T> List<T> $one2list(T obj) {
        List<T> ret = new ArrayList<>();
        if (obj == null) {
            return ret;
        }
        ret.add(obj);
        return ret;
    }

    public static MapBuilder<String, String, LinkedHashMap<String, String>> $colMap() {
        return Builders.newMap(LinkedHashMap::new, String.class, String.class);
    }

    public static MapBuilder<String, Object, LinkedHashMap<String, Object>> $valueMap() {
        return Builders.newMap(LinkedHashMap::new, String.class, Object.class);
    }

    public static ListBuilder<String, LinkedList<String>> $colList() {
        return Builders.newList(LinkedList::new, String.class);
    }

    public H $tableName(String table) {
        return $(decorateTableName(table));
    }

    public H $columnName(String column) {
        return $(decorateColumnName(column));
    }

    public H $from(String table, String tableAlias) {
        if (tableAlias == null || tableAlias.isEmpty()) {
            return $($keywords("from ") + decorateTableName(table));
        }
        return $($keywords("from ") + decorateTableName(table) + " " + tableAlias);
    }

    public H $from(String table) {
        return $from(table, null);
    }

    public H $fromDual() {
        return from().$($keywords("dual"));
    }

    public H $from(Supplier<Bql<?>> caller, String tableAlias) {
        return $trim(null, null,
                (List<String>) null, null,
                $keywords("from ("),
                ") " + tableAlias,
                v -> caller.get()
        );
    }

    public H $join(String table, String tableAlias) {
        if (tableAlias == null || tableAlias.isEmpty()) {
            return join().$(decorateTableName(table));
        }
        return join().$(decorateTableName(table)).$(tableAlias);
    }


    public H $join(String table) {
        return join().$(decorateTableName(table));
    }


    public H $join(Supplier<Bql<?>> caller, String tableAlias) {
        return $trim(null, null,
                (List<String>) null, null,
                $keywords("join ("),
                ") " + tableAlias,
                v -> caller.get()
        );
    }


    public <T> H $on(T val, Predicate<T> filter,
                     Function<T, Bql<?>> caller) {
        return $trim(val, filter,
                TRIM_AND_OR_LIST,
                TRIM_AND_OR_LIST,
                $keywords("on"), null,
                caller);
    }

    public <T> H $on(T val,
                     Function<T, Bql<?>> caller) {
        return $on(val, defaultFilter(), caller);
    }

    public <T> H $on(Supplier<Bql<?>> caller) {
        return $on(null, null, v -> caller.get());
    }


    public <T> H $where(T val, Predicate<T> filter,
                        Function<T, Bql<?>> caller) {
        return $trim(val, filter,
                TRIM_AND_OR_LIST,
                TRIM_AND_OR_LIST,
                $keywords("where"), null,
                caller);
    }

    public <T> H $where(T val,
                        Function<T, Bql<?>> caller) {
        return $where(val, defaultFilter(), caller);
    }

    public <T> H $where(Supplier<Bql<?>> caller) {
        return $where(null, null, v -> caller.get());
    }

    public <T> H $set(T val, Predicate<T> filter,
                      Function<T, Bql<?>> caller) {
        return $trim(val, filter,
                TRIM_COMMA_LIST,
                TRIM_COMMA_LIST,
                $keywords("set"), null,
                caller);
    }

    public <T> H $set(T val,
                      Function<T, Bql<?>> caller) {
        return $set(val, defaultFilter(), caller);
    }

    public <T> H $set(Supplier<Bql<?>> caller) {
        return $set(null, null, v -> caller.get());
    }

    public <T> H $select(T val, Predicate<T> filter,
                         Function<T, Bql<?>> caller) {
        return $trim(val, filter,
                TRIM_COMMA_LIST,
                TRIM_COMMA_LIST,
                $keywords("select"), null,
                caller);
    }

    public <T> H $select(T val,
                         Function<T, Bql<?>> caller) {
        return $select(val, defaultFilter(), caller);
    }

    public H $select(Supplier<Bql<?>> caller) {
        return $select(null, null, v -> caller.get());
    }

    public H $select(String... columns) {
        return $select($arr2list(columns));
    }

    public H $select(Collection<String> columns) {
        return $select(columns, (list) -> {
            Bql<?> ret = Bql.$_().$sepComma();
            for (String col : list) {
                if (col == null) {
                    continue;
                }
                ret.$col(col);
            }
            return ret;
        });
    }

    public H $selectRaw(String... columns) {
        return $selectRaw($arr2list(columns));
    }

    public H $selectRaw(Collection<String> columns) {
        return $select(columns, (list) -> {
            Bql<?> ret = Bql.$_().$sepComma();
            for (String col : list) {
                if (col == null) {
                    continue;
                }
                ret.$(col);
            }
            return ret;
        });
    }

    public <T> H $groupBy(T val, Predicate<T> filter,
                          Function<T, Bql<?>> caller) {
        return $trim(val, filter,
                TRIM_COMMA_LIST,
                TRIM_COMMA_LIST,
                $keywords("group by"), null,
                caller);
    }

    public <T> H $groupBy(T val,
                          Function<T, Bql<?>> caller) {
        return $groupBy(val, defaultFilter(), caller);
    }

    public <T> H $groupBy(Supplier<Bql<?>> caller) {
        return $groupBy(null, null, v -> caller.get());
    }

    public <T> H $orderBy(T val, Predicate<T> filter,
                          Function<T, Bql<?>> caller) {
        return $trim(val, filter,
                TRIM_COMMA_LIST,
                TRIM_COMMA_LIST,
                $keywords("order by"), null,
                caller);
    }

    public <T> H $orderBy(T val,
                          Function<T, Bql<?>> caller) {
        return $orderBy(val, defaultFilter(), caller);
    }

    public <T> H $orderBy(Supplier<Bql<?>> caller) {
        return $orderBy(null, null, v -> caller.get());
    }


    public <T> H $and(T val, Predicate<T> filter,
                      Function<T, Bql<?>> caller) {
        return $trim(val, filter,
                TRIM_AND_OR_LIST,
                TRIM_AND_OR_LIST,
                $keywords("and ("), ")",
                caller);
    }

    public <T> H $and(T val,
                      Function<T, Bql<?>> caller) {
        return $and(val, defaultFilter(), caller);
    }

    public <T> H $and(Supplier<Bql<?>> caller) {
        return $and(null, null, v -> caller.get());
    }


    public <T> H $or(T val, Predicate<T> filter,
                     Function<T, Bql<?>> caller) {
        return $trim(val, filter,
                TRIM_AND_OR_LIST,
                TRIM_AND_OR_LIST,
                $keywords("or ("), ")",
                caller);
    }

    public <T> H $or(T val,
                     Function<T, Bql<?>> caller) {
        return $or(val, defaultFilter(), caller);
    }

    public <T> H $or(Supplier<Bql<?>> caller) {
        return $or(null, null, v -> caller.get());
    }

    public H $reset() {
        this.link = "and";
        this.alias = null;
        this.separator = " ";
        this.placeholder = "?";
        this.store();
        return (H) this;
    }

    public H $and() {
        this.link = "and";
        store();
        return (H) this;
    }

    public H $or() {
        this.link = "or";
        store();
        return (H) this;
    }

    public H $link() {
        this.link = null;
        store();
        return (H) this;
    }

    public H $alias() {
        this.alias = null;
        store();
        return (H) this;
    }

    public H $alias(String alias) {
        this.alias = alias;
        store();
        return (H) this;
    }

    public H $sep() {
        this.separator = " ";
        store();
        return (H) this;
    }

    public H $sepComma() {
        this.separator = ",";
        store();
        return (H) this;
    }

    public H $sep(String separator) {
        this.separator = separator;
        store();
        return (H) this;
    }

    public H $sepNone() {
        this.separator = null;
        store();
        return (H) this;
    }

    public H placeholder() {
        this.placeholder = "?";
        store();
        return (H) this;
    }

    public H placeholder(String holder) {
        this.placeholder = holder;
        store();
        return (H) this;
    }

    public H upperKeywords(boolean enable) {
        this.upperKeywords = enable;
        store();
        return (H) this;
    }

    public String $keywords(String str) {
        if (str == null) {
            return str;
        }
        if (upperKeywords) {
            return str.toUpperCase();
        }
        return str;
    }

    public String decorateColumnName(String name) {
        if (name == null) {
            return null;
        }
        if (columnNameDecorator != null) {
            return columnNameDecorator.apply(name);
        }
        return name;
    }

    public String decorateTableName(String name) {
        if (name == null) {
            return null;
        }
        if (tableNameDecorator != null) {
            return tableNameDecorator.apply(name);
        }
        return name;
    }

    public String decorateColumnAs(String name) {
        if (name == null) {
            return null;
        }
        if (columnAsDecorator != null) {
            return columnAsDecorator.apply(name);
        }
        return name;
    }

    public String columnName(String tableAlias, String column) {
        if (tableAlias != null && !tableAlias.isEmpty()) {
            return tableAlias + "." + decorateColumnName(column);
        }
        return decorateColumnName(column);
    }

    public String condColumnName(String link, String tableAlias, String column) {
        String name = columnName(tableAlias, column);
        if (link != null && !link.isEmpty()) {
            return $keywords(link) + " " + name;
        }
        return name;
    }

    public String selectColumnName(String tableAlias, String column, String asAlias) {
        String name = columnName(tableAlias, decorateColumnName(column));
        if (asAlias != null && !asAlias.isEmpty()) {
            return name + $keywords(" as ") + decorateColumnAs(asAlias);
        }
        return name;
    }

    public String valueCondColumnName(String link, String tableAlias, String column) {
        String name = valueColumnName(tableAlias, column);
        if (link != null && !link.isEmpty()) {
            return $keywords(link) + " " + name;
        }
        return name;
    }

    public String valueColumnName(String tableAlias, String column) {
        if (tableAlias != null && !tableAlias.isEmpty()) {
            return tableAlias + "." + column;
        }
        return column;
    }

    public String valueSelectColumnName(String tableAlias, String column, String asAlias) {
        String name = valueColumnName(tableAlias, column);
        if (asAlias != null && !asAlias.isEmpty()) {
            return name + $keywords(" as ") + decorateColumnAs(asAlias);
        }
        return name;
    }


    public H $colAs(String column, String asAlias, Object... args) {
        return $(valueSelectColumnName(null, column, asAlias), args);
    }


    public H $col(String tableAlias, String column, String asAlias) {
        return $(selectColumnName(tableAlias, column, asAlias));
    }

    public H $col(String column, String asAlias) {
        return $(selectColumnName(alias, column, asAlias));
    }

    public H $col(String column) {
        return $(selectColumnName(alias, column, null));
    }

    public H $col(Supplier<Bql<?>> caller, String asAlias) {
        return $trim(null, null,
                (List<String>) null, null,
                "(",
                $keywords(") as ") + asAlias,
                v -> caller.get()
        );
    }

    public <T> H $cond(T val, Predicate<T> filter, BiFunction<T, String, Bql<?>> caller) {
        return $if(val, filter, v -> Bql.$_().$($keywords(link)).$(caller.apply(v, alias)));
    }

    public <T> H $cond(T val, BiFunction<T, String, Bql<?>> caller) {
        return $cond(val, defaultFilter(), caller);
    }

    public <T> H $cond(Supplier<Bql<?>> caller) {
        return $cond(null, null, (v, a) -> caller.get());
    }

    public <T> H $cond(String sql, Object... args) {
        return $cond(null, null, (v, a) -> Bql.$_(new BindSql(sql, $arr2list(args))));
    }


    public <T> H $eq(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> Bql.$_(condColumnName(link, alias, column) + " = " + placeholder, val));
    }


    public <T> H $eq(String column, T val) {
        return $eq(column, val, defaultFilter());
    }

    public H $eqNull(String column) {
        return $(condColumnName(link, alias, column) + $keywords(" = null"));
    }

    public <T> H $like(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> Bql.$_(condColumnName(link, alias, column) + $keywords(" like ") + placeholder, val));
    }

    public <T> H $like(String column, T val) {
        return $like(column, val, defaultFilter());
    }

    public <T> H $instr(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> Bql.$_(valueCondColumnName(link, null, "instr(" + columnName(alias, column) + "," + placeholder + ")") + " > 0", val));
    }


    public <T> H $instr(String column, T val) {
        return $instr(column, val, defaultFilter());
    }

    public <T> H $gt(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> Bql.$_(condColumnName(link, alias, column) + " > " + placeholder, val));
    }


    public <T> H $gt(String column, T val) {
        return $gt(column, val, defaultFilter());
    }

    public <T> H $lt(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> Bql.$_(condColumnName(link, alias, column) + " < " + placeholder, val));
    }


    public <T> H $lt(String column, T val) {
        return $lt(column, val, defaultFilter());
    }

    public <T> H $gte(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> Bql.$_(condColumnName(link, alias, column) + " >= " + placeholder, val));
    }


    public <T> H $gte(String column, T val) {
        return $gte(column, val, defaultFilter());
    }

    public <T> H $lte(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> Bql.$_(condColumnName(link, alias, column) + " <= " + placeholder, val));
    }


    public <T> H $lte(String column, T val) {
        return $lte(column, val, defaultFilter());
    }

    public <T> H $neq(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> Bql.$_(condColumnName(link, alias, column) + " != " + placeholder, val));
    }


    public <T> H $neq(String column, T val) {
        return $neq(column, val, defaultFilter());
    }

    public <T> H $ne(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> Bql.$_(condColumnName(link, alias, column) + " <> " + placeholder, val));
    }


    public <T> H $ne(String column, T val) {
        return $ne(column, val, defaultFilter());
    }

    public <T> H $isNull(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> Bql.$_(new BindSql(condColumnName(link, alias, column) + $keywords(" is null"))));
    }


    public <T> H $isNull(String column, T val) {
        return $isNull(column, val, defaultFilter());
    }


    public H $isNull(String column) {
        return $isNull(column, null, null);
    }

    public <T> H $isNotNull(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> Bql.$_(new BindSql(condColumnName(link, alias, column) + $keywords(" is not null"))));
    }


    public <T> H $isNotNull(String column, T val) {
        return $isNotNull(column, val, defaultFilter());
    }

    public H $isNotNull(String column) {
        return $isNotNull(column, null, null);
    }

    public <T, C extends Collection<T>> H $in(String column, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $trim(val, filter,
                TRIM_COMMA_LIST,
                TRIM_COMMA_LIST,
                condColumnName(link, alias, column) + $keywords(" in ("),
                ")",
                col -> Bql.$_().
                        $for(col, null, ",", itemFilter,
                                (i, v) -> Bql.$_(placeholder, v)
                        )
        );
    }

    public <T, C extends Collection<T>> H $in(String column, C val, Predicate<T> itemFilter) {
        return $in(column, val, defaultFilter(), itemFilter);
    }

    public <T, C extends Collection<T>> H $in(String column, C val) {
        return $in(column, val, defaultFilter(), null);
    }

    public <T, C extends Collection<T>> H $notIn(String column, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $trim(val, filter,
                TRIM_COMMA_LIST,
                TRIM_COMMA_LIST,
                condColumnName(link, alias, column) + $keywords(" not in ("),
                ")",
                col -> Bql.$_().
                        $for(col, null, ",", itemFilter,
                                (i, v) -> Bql.$_(placeholder, v)
                        )
        );
    }


    public <T, C extends Collection<T>> H $notIn(String column, C val, Predicate<T> itemFilter) {
        return $notIn(column, val, defaultFilter(), itemFilter);
    }


    public <T, C extends Collection<T>> H $notIn(String column, C val) {
        return $notIn(column, val, defaultFilter(), null);
    }


    public <T> H $between(String column, T min, T max, Predicate<T> filter) {
        boolean testMin = filter != null && filter.test(min);
        boolean testMax = filter != null && filter.test(max);
        if (testMin && testMax) {
            return $(condColumnName(link, alias, column)).between().$(placeholder, min).and().$(placeholder, max);
        } else if (testMin) {
            return $gte(column, min);
        } else if (testMax) {
            return $lt(column, max);
        }
        return (H) this;
    }


    public <T> H $between(String column, T min, T max) {
        return $between(column, min, max, defaultFilter());
    }


    public <T> H $exists(T val, Predicate<T> filter, Function<T, Bql<?>> caller) {
        return $trim(val, filter,
                (List<String>) null, null,
                valueCondColumnName(link, null, $keywords(" exists (")),
                ")", caller
        );
    }

    public <T> H $exists(T val, Function<T, Bql<?>> caller) {
        return $exists(val, defaultFilter(), caller);
    }

    public <T> H $exists(Supplier<Bql<?>> caller) {
        return $exists(null, null, v -> caller.get());
    }

    public <T> H $notExists(T val, Predicate<T> filter, Function<T, Bql<?>> caller) {
        return $trim(val, filter,
                (List<String>) null, null,
                condColumnName(link, null, $keywords(" not exists (")),
                ")", caller
        );
    }

    public <T> H $notExists(T val, Function<T, Bql<?>> caller) {
        return $notExists(val, defaultFilter(), caller);
    }

    public <T> H $notExists(Supplier<Bql<?>> caller) {
        return $notExists(null, null, v -> caller.get());
    }

    public H $bracket(Supplier<Bql<?>> caller) {
        return $trim(null, null,
                TRIM_COMMA_LIST,
                TRIM_COMMA_LIST,
                "(", ")",
                v -> caller.get());
    }

    public H $beginBlock(Supplier<Bql<?>> caller) {
        return $trim(null, null,
                TRIM_COMMA_SM_LIST,
                TRIM_COMMA_LIST,
                $keywords("begin\n"), $keywords("\nend"),
                v -> caller.get());
    }


    public H count(String inner) {
        return $($keywords("count(") + inner + ")");
    }

    public H count(Supplier<Bql<?>> caller) {
        return $($keywords("count(")).$(caller.get()).$(")");
    }

    public H fetchCursorInto(String name, Supplier<Bql<?>> caller) {
        return fetch().$(name).into()
                .$trim(TRIM_COMMA_LIST,
                        TRIM_COMMA_LIST,
                        null, null,
                        caller);
    }

    public H openCursor(String name) {
        return open().$(name);
    }

    public H declareCursor(String name, Supplier<Bql<?>> caller) {
        return declare().cursor().$(name).$for().$(caller.get());
    }

    public H forEachRow(Supplier<Bql<?>> caller) {
        return forEachRow().$beginBlock(caller);
    }


    public H $into(String table) {
        return into().$(table);
    }

    public H $into(String table, Supplier<Bql<?>> caller) {
        return into().$(decorateTableName(table)).$bracket(caller);
    }

    public H $values(Supplier<Bql<?>> caller) {
        return values().$bracket(caller);
    }

    public H $update(String table) {
        return update().$(decorateTableName(table));
    }

    public H $deleteFrom(String table) {
        return deleteFrom().$(decorateTableName(table));
    }

    public H $table(String table) {
        return table().$(decorateTableName(table));
    }

    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("'", "''");
    }

    public H $comment(String comment) {
        return comment().$("'" + escapeSql(comment) + "'");
    }

    public H delimiter(String delimiter) {
        return delimiter().$(delimiter).ln();
    }

    public H $truncateTable(String table) {
        return truncate().table().$(decorateTableName(table));
    }

    public H closeCursor(String name) {
        return close().$(name);
    }


    public H left() {
        return $($keywords("left"));
    }

    public H right() {
        return $($keywords("right"));
    }

    public H inner() {
        return $($keywords("inner"));
    }

    public H outer() {
        return $($keywords("outer"));
    }

    public H join() {
        return $($keywords("join"));
    }

    public H leftJoin() {
        return left().join();
    }

    public H rightJoin() {
        return right().join();
    }

    public H innerJoin() {
        return inner().join();
    }

    public H outerJoin() {
        return outer().join();
    }

    public H insert() {
        return $($keywords("insert"));
    }

    public H into() {
        return $($keywords("into"));
    }

    public H values() {
        return $($keywords("values"));
    }

    public H update() {
        return $($keywords("update"));
    }

    public H delete() {
        return $($keywords("delete"));
    }

    public H deleteFrom() {
        return delete().from();
    }

    public H create() {
        return $($keywords("create"));
    }

    public H table() {
        return $($keywords("table"));
    }

    public H primary() {
        return $($keywords("primary"));
    }

    public H key() {
        return $($keywords("key"));
    }

    public H primaryKey() {
        return primary().key();
    }

    public H $default() {
        return $($keywords("default"));
    }

    public H $null() {
        return $($keywords("null"));
    }

    public H not() {
        return $($keywords("not"));
    }

    public H notNull() {
        return not().$null();
    }

    public H comment() {
        return $($keywords("comment"));
    }

    public H foreign() {
        return $($keywords("foreign"));
    }

    public H references() {
        return $($keywords("references"));
    }

    public H foreignKeyReferences() {
        return foreign().key().references();
    }

    public H $foreignKeyReferences(String table, String column) {
        return foreign().key().references().$(decorateTableName(table) + "(" + decorateColumnName(column) + ")");
    }


    public H unique() {
        return $($keywords("unique"));
    }

    public H index() {
        return $($keywords("index"));
    }

    public H on() {
        return $($keywords("on"));
    }

    public H drop() {
        return $($keywords("drop"));
    }

    public H select() {
        return $($keywords("select"));
    }

    public H as() {
        return $($keywords("as"));
    }

    public H $case() {
        return $($keywords("case"));
    }

    public H when() {
        return $($keywords("when"));
    }

    public H $else() {
        return $($keywords("else"));
    }

    public H end() {
        return $($keywords("end"));
    }

    public H from() {
        return $($keywords("from"));
    }

    public H and() {
        return $($keywords("and"));
    }

    public H or() {
        return $($keywords("or"));
    }

    public H like() {
        return $($keywords("like"));
    }

    public H sp() {
        return $(" ");
    }

    public H ln() {
        return $("\n");
    }

    public H tb() {
        return $("\t");
    }

    public H cm() {
        return $(",");
    }

    public H sm() {
        return $(";");
    }

    public H qt() {
        return $("'");
    }

    public H qd() {
        return $("\"");
    }

    public H eq() {
        return $("=");
    }

    public H gt() {
        return $(">");
    }

    public H lt() {
        return $("<");
    }

    public H gte() {
        return $(">=");
    }

    public H lte() {
        return $("<=");
    }

    public H neq() {
        return $("!=");
    }

    public H ne() {
        return $("<>");
    }

    public H between() {
        return $($keywords("between"));
    }

    public H where() {
        return $($keywords("where"));
    }

    public H group() {
        return $($keywords("group"));
    }

    public H by() {
        return $($keywords("by"));
    }

    public H groupBy() {
        return group().by();
    }

    public H having() {
        return $($keywords("having"));
    }

    public H order() {
        return $($keywords("order"));
    }

    public H orderBy() {
        return order().by();
    }

    public H $if() {
        return $($keywords("if"));
    }

    public H exists() {
        return $($keywords("exists"));
    }

    public H replace() {
        return $($keywords("replace"));
    }

    public H ignore() {
        return $($keywords("ignore"));
    }

    public H truncate() {
        return $($keywords("truncate"));
    }

    public H truncateTable() {
        return truncate().table();
    }

    public H set() {
        return $($keywords("set"));
    }

    public H distinct() {
        return $($keywords("distinct"));
    }

    public H top() {
        return $($keywords("top"));
    }

    public H limit() {
        return $($keywords("limit"));
    }

    public H in() {
        return $($keywords("in"));
    }

    public H with() {
        return $($keywords("with"));
    }

    public H union() {
        return $($keywords("union"));
    }

    public H all() {
        return $($keywords("all"));
    }

    public H unionAll() {
        return union().all();
    }

    public H over() {
        return $($keywords("over"));
    }

    public H partition() {
        return $($keywords("partition"));
    }

    public H partitionBy() {
        return partition().by();
    }

    public H database() {
        return $($keywords("database"));
    }

    public H check() {
        return $($keywords("check"));
    }

    public H show() {
        return $($keywords("show"));
    }

    public H databases() {
        return $($keywords("databases"));
    }

    public H tables() {
        return $($keywords("tables"));
    }

    public H procedure() {
        return $($keywords("procedure"));
    }

    public H declare() {
        return $($keywords("declare"));
    }

    public H temporary() {
        return $($keywords("temporary"));
    }

    public H alter() {
        return $($keywords("alter"));
    }

    public H view() {
        return $($keywords("view"));
    }

    public H trigger() {
        return $($keywords("trigger"));
    }

    public H schema() {
        return $($keywords("schema"));
    }

    public H domain() {
        return $($keywords("domain"));
    }

    public H catalog() {
        return $($keywords("catalog"));
    }

    public H then() {
        return $($keywords("then"));
    }

    public H $while() {
        return $($keywords("while"));
    }

    public H $do() {
        return $($keywords("do"));
    }

    public H repeat() {
        return $($keywords("repeat"));
    }

    public H endRepeat() {
        return end().repeat();
    }

    public H until() {
        return $($keywords("until"));
    }


    public H loop() {
        return $($keywords("loop"));
    }

    public H delimiter() {
        return $($keywords("delimiter"));
    }

    public H out() {
        return $($keywords("out"));
    }

    public H begin() {
        return $($keywords("begin"));
    }

    public H commit() {
        return $($keywords("commit"));
    }

    public H rollback() {
        return $($keywords("rollback"));
    }

    public H call() {
        return $($keywords("call"));
    }

    public H before() {
        return $($keywords("before"));
    }

    public H after() {
        return $($keywords("after"));
    }

    public H $for() {
        return $($keywords("for"));
    }

    public H each() {
        return $($keywords("each"));
    }

    public H row() {
        return $($keywords("row"));
    }

    public H forEachRow() {
        return $for().each().row();
    }


    public H cursor() {
        return $($keywords("cursor"));
    }

    public H open() {
        return $($keywords("open"));
    }

    public H close() {
        return $($keywords("close"));
    }

    public H fetch() {
        return $($keywords("fetch"));
    }

    public H $int() {
        return $($keywords("int"));
    }

    public H bigint() {
        return $($keywords("bigint"));
    }

    public H varchar() {
        return $($keywords("varchar"));
    }

    public H varchar(int len) {
        return $($keywords("varchar(") + len + ")");
    }

    public H datetime() {
        return $($keywords("datetime"));
    }

    public H date() {
        return $($keywords("date"));
    }

    public H timestamp() {
        return $($keywords("timestamp"));
    }

    public H tinyint() {
        return $($keywords("tinyint"));
    }

    public H $char() {
        return $($keywords("char"));
    }

    public H $char(int len) {
        return $($keywords("char(") + len + ")");
    }

    public H decimal() {
        return $($keywords("decimal"));
    }

    public H decimal(int precision, int scale) {
        return $($keywords("decimal(") + precision + "," + scale + ")");
    }

    public H $double() {
        return $($keywords("double"));
    }

    public H $double(int precision, int scale) {
        return $($keywords("double(") + precision + "," + scale + ")");
    }

    public H count() {
        return $($keywords("count"));
    }

    public H countAll() {
        return $($keywords("count(*)"));
    }

    public H count1() {
        return $($keywords("count(1)"));
    }

}
