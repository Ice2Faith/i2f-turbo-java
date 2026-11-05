package i2f.bindsql;

import i2f.lambda.core.Lambda;
import i2f.lambda.core.func.IGetter;
import i2f.text.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2024/4/7 16:52
 * @desc
 */
public class BindSql {

    public static void main(String[] args) {
        /*language=sql*/
        BindSql add = BindSql.of()
                .select(s -> s
                        .add("a.*")
                        .add("/* ? */ ,' ? '' ?'  -- ? \n  as text")
                )
                .from().add("sys_user ").add("a")
                .where(q ->
                        q.and(s -> s.add("a.user_id").eq(1001))
                                .and(s -> s.add("a.status ").in(Arrays.asList(1, 2, 3)))
                                .choose(e -> e
                                        .when(
                                                () -> false,
                                                s -> s.and(v -> v.add("a.nick_name").like("123"))
                                        )
                                        .otherwise(
                                                s -> s.and(v -> v.add("a.user_name").eq("admin"))
                                        )
                                )
                );


        /*language=sql*/
        BindSql bql = BindSql.of("select a.* from sys_user a")
                .where(q -> q
                        .andEq("a.user_id", 1)
                        .andEq(c -> c.get(BindSql::getSql), 2)
                        .andEq(c -> c.get(BindSql::getType), 3)
                        .andEq(c -> c.get("a.age"), 4)
                        .andEq(c -> c.get(BindSql::getType), 5)
                        .andEq(c -> c.get("a", BindSql::getArgs), 6)
                );
        System.out.println(bql.pretty().toOracleMergeSql());
        System.out.println("++++++++++++++++++++");
        System.out.println(add);
        System.out.println("======================");

        String sql = add.toMergeSql();
        System.out.println(sql);
        System.out.println("======================");

        sql = trimCommentSql(sql);
        System.out.println(sql);
        System.out.println("======================");

        sql = prettySql(sql);
        System.out.println(sql);
        System.out.println("======================");

    }

    public static enum Type {
        UNSET(0),
        QUERY(1),
        UPDATE(2),
        CALL(3);

        private int code;

        private Type(int code) {
            this.code = code;
        }

        public int code() {
            return code;
        }
    }

    protected Type type = Type.UNSET;
    protected String sql = "";
    protected List<Object> args = new ArrayList<>();

    public BindSql() {
    }

    public BindSql(String sql) {
        this.sql = sql;
    }

    public BindSql(String sql, List<Object> args) {
        this.sql = sql;
        this.args = args;
    }

    public BindSql(Type type, String sql, List<Object> args) {
        this.type = type;
        this.sql = sql;
        this.args = args;
    }


    public Type getType() {
        return type;
    }

    public BindSql setType(Type type) {
        this.type = type;
        return this;
    }

    public String getSql() {
        return sql;
    }

    public BindSql setSql(String sql) {
        this.sql = sql;
        return this;
    }

    public List<Object> getArgs() {
        return args;
    }

    public BindSql setArgs(List<Object> args) {
        this.args = args;
        return this;
    }

    public BindSql setArgsArray(Object[] args) {
        if (args == null) {
            return this;
        }
        this.args = new ArrayList<>(Arrays.asList(args));
        return this;
    }

    public Object[] getArgsArray() {
        if (args == null) {
            return new Object[0];
        }
        return args.toArray();
    }

    public boolean isEmpty() {
        return sql == null || sql.isEmpty();
    }

    public boolean isBlank() {
        if (isEmpty()) {
            return true;
        }
        for (int i = 0; i < sql.length(); i++) {
            char ch = sql.charAt(i);
            if (!Character.isWhitespace(ch)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BindSql bindSql = (BindSql) o;
        return type == bindSql.type &&
                Objects.equals(sql, bindSql.sql) &&
                Objects.equals(args, bindSql.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, sql, args);
    }

    public String toArgsString() {
        if (args == null) {
            return "null";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        boolean isFirst = true;
        for (Object arg : args) {
            if (!isFirst) {
                builder.append(", ");
            }
            builder.append("(").append((arg == null ? "null" : arg.getClass().getSimpleName())).append(")").append(arg);
            isFirst = false;
        }
        builder.append("]");
        return builder.toString();
    }

    @Override
    public String toString() {
        return "BindSql{\n" +
                "type  = " + type + "\n" +
                ", sql   = " + sql + "\n" +
                ", args  = " + toArgsString() + "\n" +
                "}";
    }

    // 基础功能

    public static BindSql of() {
        return new BindSql("", new ArrayList<>());
    }

    public static BindSql of(Type type, String sql, Object... args) {
        return of(sql, args).setType(type);
    }

    public static BindSql of(String sql, Object... args) {
        List<Object> list = new ArrayList<>();
        for (Object arg : args) {
            list.add(arg);
        }
        return new BindSql(sql, list);
    }

    public BindSql concat(String sql, Object... args) {
        return concat(of(sql, args));
    }

    public BindSql concat(Function<BindSql, BindSql> consumer) {
        return concat(consumer.apply(BindSql.of()));
    }

    public BindSql concat(BindSql bindSql) {
        if (bindSql == null) {
            return this;
        }
        String sql = this.sql + bindSql.sql;
        List<Object> args = new ArrayList<>();
        args.addAll(this.args);
        args.addAll(bindSql.args);
        return new BindSql(sql, args);
    }

    public BindSql add(String sql, Object... args) {
        return add(of(sql, args));
    }

    public BindSql add(Function<BindSql, BindSql> consumer) {
        return add(consumer.apply(BindSql.of()));
    }

    public BindSql add(BindSql bindSql) {
        if (bindSql == null) {
            return this;
        }
        String sql = this.sql + " " + bindSql.sql;
        List<Object> args = new ArrayList<>();
        args.addAll(this.args);
        args.addAll(bindSql.args);
        return new BindSql(sql, args);
    }

    public BindSql val(Object val) {
        return concat(BindSql.of("?", val));
    }

    public BindSql line() {
        return new BindSql(this.sql + "\n", new ArrayList<>(this.args));
    }

    public BindSql trim() {
        return new BindSql(this.sql.trim(), new ArrayList<>(this.args));
    }

    public BindSql space() {
        return new BindSql(this.sql + " ", new ArrayList<>(this.args));
    }

    public BindSql tab() {
        return new BindSql(this.sql + "\t", new ArrayList<>(this.args));
    }

    public BindSql spacing() {
        return new BindSql(" " + this.sql + " ", new ArrayList<>(this.args));
    }

    public static String escapeSqlString(String str) {
        if (str == null) {
            return str;
        }
        return "'" + str.replace("'", "''") + "'";
    }

    public static String descapeSqlString(String str) {
        if (str == null) {
            return str;
        }
        if (str.isEmpty()) {
            return str;
        }
        if (str.startsWith("'")) {
            str = str.substring(1, str.length() - 1);
        }
        return str.replace("''", "'");
    }

    // 动态部分
    public <T> BindSql vars(T... arr) {
        return vars(Arrays.asList(arr).iterator());
    }

    public <T> BindSql vars(Iterable<T> iterable) {
        return vars(iterable.iterator());
    }

    public <T> BindSql vars(Iterator<T> iterable) {
        return foreach(iterable, (s, v) -> s.add("?", v), BindSql.of(","),
                BindSql.of("("), BindSql.of(")"));
    }

    public <T> BindSql varsJoin(T... arr) {
        return varsJoin(Arrays.asList(arr).iterator());
    }

    public <T> BindSql varsJoin(Iterable<T> iterable) {
        return varsJoin(iterable.iterator());
    }

    public <T> BindSql varsJoin(Iterator<T> iterable) {
        return foreach(iterable, (s, v) -> s.add("?", v), BindSql.of(","), null, null);
    }

    public <T> BindSql foreach(T[] iterable, BiFunction<BindSql, T, BindSql> consumer) {
        return foreach(Arrays.asList(iterable).iterator(), consumer, null, null, null);
    }

    public <T> BindSql foreach(Iterable<T> iterable, BiFunction<BindSql, T, BindSql> consumer) {
        return foreach(iterable.iterator(), consumer, null, null, null);
    }

    public <T> BindSql foreach(Iterator<T> iterable, BiFunction<BindSql, T, BindSql> consumer) {
        return foreach(iterable, consumer, null, null, null);
    }

    public <T> BindSql foreach(T[] iterable, BiFunction<BindSql, T, BindSql> consumer, BindSql separator) {
        return foreach(Arrays.asList(iterable).iterator(), consumer, separator, null, null);
    }

    public <T> BindSql foreach(Iterable<T> iterable, BiFunction<BindSql, T, BindSql> consumer, BindSql separator) {
        return foreach(iterable.iterator(), consumer, separator, null, null);
    }

    public <T> BindSql foreach(Iterator<T> iterable, BiFunction<BindSql, T, BindSql> consumer, BindSql separator) {
        return foreach(iterable, consumer, separator, null, null);
    }

    public <T> BindSql foreach(T[] iterable, BiFunction<BindSql, T, BindSql> consumer, BindSql separator, BindSql open, BindSql close) {
        return foreach(Arrays.asList(iterable).iterator(), consumer, separator, open, close);
    }

    public <T> BindSql foreach(Iterable<T> iterable, BiFunction<BindSql, T, BindSql> consumer, BindSql separator, BindSql open, BindSql close) {
        return foreach(iterable.iterator(), consumer, separator, open, close);
    }

    public <T> BindSql foreach(Iterator<T> iterable, BiFunction<BindSql, T, BindSql> consumer, BindSql separator, BindSql open, BindSql close) {
        BindSql ret = BindSql.of();
        boolean isFirst = true;
        while (iterable.hasNext()) {
            T item = iterable.next();
            if (!isFirst) {
                if (separator != null) {
                    ret = ret.concat(separator);
                }
            }
            BindSql next = consumer.apply(BindSql.of(), item);
            ret = ret.concat(next);
            isFirst = false;
        }
        if (!isFirst) {
            if (open != null) {
                ret = open.concat(ret);
            }
            if (close != null) {
                ret = ret.concat(close);
            }
        }
        return concat(ret);
    }

    public <T> BindSql when(T value, BiFunction<BindSql, T, BindSql> consumer) {
        return when(value, null, consumer);
    }

    public <T> BindSql when(T value, Predicate<T> predicate, BiFunction<BindSql, T, BindSql> consumer) {
        if (predicate == null) {
            predicate = BindSql::validateValue;
        }
        if (predicate.test(value)) {
            BindSql sql = consumer.apply(BindSql.of(), value);
            return concat(sql);
        }
        return this;
    }

    public BindSql when(Supplier<Boolean> predicate, Function<BindSql, BindSql> consumer) {
        if (predicate.get()) {
            BindSql sql = consumer.apply(BindSql.of());
            return concat(sql);
        }
        return this;
    }

    public BindSql trim(Function<BindSql, BindSql> consumer,
                        List<String> trimPrefixes, List<String> trimSuffixes,
                        BindSql appendPrefix, BindSql appendSuffix) {
        BindSql next = consumer.apply(BindSql.of());
        String sql = next.getSql().trim();
        String trimSql = sql.toLowerCase();
        if (trimPrefixes != null) {
            for (String item : trimPrefixes) {
                item = item.toLowerCase();
                if (trimSql.startsWith(item)) {
                    sql = sql.substring(item.length());
                    trimSql = trimSql.substring(item.length());
                    break;
                }
            }
        }
        if (trimSuffixes != null) {
            for (String item : trimSuffixes) {
                item = item.toLowerCase();
                if (trimSql.endsWith(item)) {
                    sql = sql.substring(0, sql.length() - item.length());
                    trimSql = trimSql.substring(0, trimSql.length() - item.length());
                    break;
                }
            }
        }
        sql = sql.trim();
        if (sql.isEmpty()) {
            next = BindSql.of();
        } else {
            next = new BindSql(sql, new ArrayList<>(next.getArgs()));
            if (appendPrefix != null) {
                next = appendPrefix.add(next);
            }
            if (appendSuffix != null) {
                next = next.add(appendSuffix);
            }
        }

        return concat(next);
    }

    public BindSql bracket(Function<BindSql, BindSql> consumer) {
        return trim(consumer, null, null,
                BindSql.of("("), BindSql.of(")"));
    }

    @Data
    @NoArgsConstructor
    public static class ChooseBranch {
        protected Supplier<Boolean> predicate;
        protected Function<BindSql, BindSql> consumer;

        public ChooseBranch(Supplier<Boolean> predicate, Function<BindSql, BindSql> consumer) {
            this.predicate = predicate;
            this.consumer = consumer;
        }

        public static ChooseBranch of(Supplier<Boolean> predicate, Function<BindSql, BindSql> consumer) {
            return new ChooseBranch(predicate, consumer);
        }

        public static ChooseBranch of(Function<BindSql, BindSql> consumer) {
            return new ChooseBranch(() -> true, consumer);
        }
    }

    public static class ChooseBranchBuilder {
        protected List<ChooseBranch> list = new ArrayList<>();

        public static ChooseBranchBuilder builder() {
            return new ChooseBranchBuilder();
        }

        public ChooseBranchBuilder when(Supplier<Boolean> predicate, Function<BindSql, BindSql> consumer) {
            list.add(ChooseBranch.of(predicate, consumer));
            return this;
        }

        public ChooseBranchBuilder otherwise(Function<BindSql, BindSql> consumer) {
            list.add(ChooseBranch.of(consumer));
            return this;
        }

        public List<ChooseBranch> build() {
            return list;
        }
    }

    public BindSql choose(Consumer<ChooseBranchBuilder> consumer) {
        ChooseBranchBuilder builder = ChooseBranchBuilder.builder();
        consumer.accept(builder);
        return choose(builder.build());
    }

    public BindSql choose(ChooseBranch... branches) {
        return choose(Arrays.asList(branches));
    }

    public BindSql choose(Collection<ChooseBranch> branches) {
        if (branches == null || branches.isEmpty()) {
            return this;
        }
        for (ChooseBranch branch : branches) {
            Supplier<Boolean> predicate = branch.getPredicate();
            if (predicate == null || predicate.get()) {
                Function<BindSql, BindSql> consumer = branch.getConsumer();
                BindSql next = consumer.apply(BindSql.of());
                return concat(next);
            }
        }
        return this;
    }

    public static boolean validateValue(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Boolean) {
            return (boolean) obj;
        }
        if (obj instanceof String) {
            String s = (String) obj;
            return !s.isEmpty();
        }
        if (obj instanceof CharSequence) {
            CharSequence s = (CharSequence) obj;
            return s.length() > 0;
        }
        if (obj instanceof Collection) {
            Collection<?> c = (Collection<?>) obj;
            return !c.isEmpty();
        }
        if (obj instanceof Map) {
            Map<?, ?> m = (Map<?, ?>) obj;
            return !m.isEmpty();
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) > 0;
        }
        if (obj instanceof Iterator) {
            Iterator<?> i = (Iterator<?>) obj;
            return i.hasNext();
        }
        if (obj instanceof Enumeration) {
            Enumeration<?> e = (Enumeration<?>) obj;
            return e.hasMoreElements();
        }
        return true;
    }

    // sql 拓展部分

    public BindSql select() {
        return add(BindSql.of("select"));
    }

    public BindSql select(Function<BindSql, BindSql> consumer) {
        return trim(consumer, Collections.singletonList(","), Collections.singletonList(","), BindSql.of().select(), null);
    }

    public BindSql where() {
        return add(BindSql.of("where"));
    }

    public BindSql where(Function<BindSql, BindSql> consumer) {
        return trim(consumer, Arrays.asList("and", "or"), Arrays.asList("and", "or"),
                BindSql.of().where(), null);
    }

    public BindSql and() {
        return add(BindSql.of("and"));
    }

    public BindSql and(Function<BindSql, BindSql> consumer) {
        return and().concat(consumer);
    }

    public BindSql or() {
        return add(BindSql.of("or"));
    }

    public BindSql or(Function<BindSql, BindSql> consumer) {
        return or().concat(consumer);
    }

    public BindSql in() {
        return add(BindSql.of("in"));
    }

    public <T> BindSql in(Iterable<T> iterable) {
        return in().space().vars(iterable);
    }

    public <T> BindSql in(T... iterable) {
        return in().space().vars(iterable);
    }

    public <T> BindSql in(Iterator<T> iterable) {
        return in().space().vars(iterable);
    }

    public BindSql eq(Object value) {
        return add(BindSql.of("=")).add("?", value);
    }

    public BindSql neq(Object value) {
        return add(BindSql.of("!=")).add("?", value);
    }

    public BindSql gt(Object value) {
        return add(BindSql.of(">")).add("?", value);
    }

    public BindSql lt(Object value) {
        return add(BindSql.of("<")).add("?", value);
    }

    public BindSql gte(Object value) {
        return add(BindSql.of(">=")).add("?", value);
    }

    public BindSql lte(Object value) {
        return add(BindSql.of("<=")).add("?", value);
    }

    public BindSql like() {
        return add(BindSql.of("like"));
    }

    public BindSql like(Object value) {
        return like().add("?", "%" + value + "%");
    }

    public BindSql startsWith(Object value) {
        return like().add("?", value + "%");
    }

    public BindSql endsWith(Object value) {
        return like().add("?", "%" + value);
    }

    public BindSql between() {
        return add(BindSql.of("between"));
    }

    public BindSql between(Object begin, Object end) {
        return between().add("?", begin).to().add("?", end);
    }

    public BindSql not() {
        return add(BindSql.of("not"));
    }

    public BindSql nil() {
        return add(BindSql.of("null"));
    }

    public BindSql is() {
        return add(BindSql.of("is"));
    }

    public BindSql isNull() {
        return add(BindSql.of("is null"));
    }

    public BindSql isNotNull() {
        return add(BindSql.of("is not null"));
    }

    public BindSql exists() {
        return add(BindSql.of("exists"));
    }

    public BindSql exists(Function<BindSql, BindSql> consumer) {
        return trim(consumer, null, null,
                BindSql.of().exists().add("("), BindSql.of(")"));
    }

    public BindSql from() {
        return add(BindSql.of("from"));
    }

    public BindSql left() {
        return add(BindSql.of("left"));
    }

    public BindSql right() {
        return add(BindSql.of("right"));
    }

    public BindSql inner() {
        return add(BindSql.of("inner"));
    }

    public BindSql outer() {
        return add(BindSql.of("outer"));
    }

    public BindSql join() {
        return add(BindSql.of("join"));
    }

    public BindSql leftJoin() {
        return left().join();
    }

    public BindSql rightJoin() {
        return right().join();
    }

    public BindSql innerJoin() {
        return inner().join();
    }

    public BindSql outerJoin() {
        return outer().join();
    }

    public BindSql on() {
        return add(BindSql.of("on"));
    }

    public BindSql on(Function<BindSql, BindSql> consumer) {
        return trim(consumer, Arrays.asList("and", "or"), Arrays.asList("and", "or"),
                BindSql.of().on(), null);
    }

    public BindSql group() {
        return add(BindSql.of("group"));
    }

    public BindSql by() {
        return add(BindSql.of("by"));
    }

    public BindSql groupBy() {
        return group().by();
    }

    public BindSql groupBy(Function<BindSql, BindSql> consumer) {
        return trim(consumer, Collections.singletonList(","), Collections.singletonList(","), BindSql.of().groupBy(), null);
    }

    public BindSql order() {
        return add(BindSql.of("order"));
    }

    public BindSql orderBy() {
        return order().by();
    }

    public BindSql orderBy(Function<BindSql, BindSql> consumer) {
        return trim(consumer, Collections.singletonList(","), Collections.singletonList(","), BindSql.of().orderBy(), null);
    }

    public BindSql asc() {
        return add(BindSql.of("asc"));
    }

    public BindSql desc() {
        return add(BindSql.of("desc"));
    }

    public BindSql having() {
        return add(BindSql.of("having"));
    }

    public BindSql having(Function<BindSql, BindSql> consumer) {
        return trim(consumer, Arrays.asList("and", "or"), Arrays.asList("and", "or"),
                BindSql.of().having(), null);
    }

    public BindSql update() {
        return add(BindSql.of("update"));
    }

    public BindSql set() {
        return add(BindSql.of("set"));
    }

    public BindSql set(Function<BindSql, BindSql> consumer) {
        return trim(consumer, Collections.singletonList(","), Collections.singletonList(","),
                BindSql.of().set(), null);
    }

    public BindSql insert() {
        return add(BindSql.of("insert"));
    }

    public BindSql into() {
        return add(BindSql.of("into"));
    }

    public BindSql values() {
        return add(BindSql.of("values"));
    }

    public BindSql delete() {
        return add(BindSql.of("delete"));
    }

    public BindSql with() {
        return add(BindSql.of("with"));
    }

    public BindSql as() {
        return add(BindSql.of("as"));
    }

    public BindSql with(String alias, Function<BindSql, BindSql> consumer) {
        return BindSql.of()
                .with().add(alias).as()
                .bracket(consumer);
    }

    public BindSql create() {
        return add(BindSql.of("create"));
    }

    public BindSql table() {
        return add(BindSql.of("table"));
    }

    public BindSql view() {
        return add(BindSql.of("view"));
    }

    public BindSql index() {
        return add(BindSql.of("index"));
    }

    public BindSql unique() {
        return add(BindSql.of("unique"));
    }

    public BindSql drop() {
        return add(BindSql.of("drop"));
    }

    public BindSql grant() {
        return add(BindSql.of("grant"));
    }

    public BindSql revoke() {
        return add(BindSql.of("revoke"));
    }

    public BindSql to() {
        return add(BindSql.of("to"));
    }

    public BindSql comment() {
        return add(BindSql.of("comment"));
    }

    public BindSql primary() {
        return add(BindSql.of("primary"));
    }

    public BindSql key() {
        return add(BindSql.of("key"));
    }

    public BindSql primaryKey() {
        return primary().key();
    }

    public BindSql foreign() {
        return add(BindSql.of("foreign"));
    }

    public BindSql foreignKey() {
        return foreign().key();
    }

    public BindSql references() {
        return add(BindSql.of("references"));
    }

    // 动态条件部分

    public BindSql andEq(String column, Object value) {
        return when(value, (q, v) -> q.and().add(column).eq(v));
    }

    public BindSql andEq(Function<ColumnNameProvider, String> column, Object value) {
        return when(value, (q, v) -> q.and().add(column.apply(ColumnNameProvider.INSTANCE)).eq(v));
    }

    public BindSql andNeq(String column, Object value) {
        return when(value, (q, v) -> q.and().add(column).neq(v));
    }

    public BindSql andNeq(Function<ColumnNameProvider, String> column, Object value) {
        return when(value, (q, v) -> q.and().add(column.apply(ColumnNameProvider.INSTANCE)).neq(v));
    }

    public BindSql andGt(String column, Object value) {
        return when(value, (q, v) -> q.and().add(column).gt(v));
    }

    public BindSql andGt(Function<ColumnNameProvider, String> column, Object value) {
        return when(value, (q, v) -> q.and().add(column.apply(ColumnNameProvider.INSTANCE)).gt(v));
    }

    public BindSql andLt(String column, Object value) {
        return when(value, (q, v) -> q.and().add(column).lt(v));
    }

    public BindSql andLt(Function<ColumnNameProvider, String> column, Object value) {
        return when(value, (q, v) -> q.and().add(column.apply(ColumnNameProvider.INSTANCE)).lt(v));
    }

    public BindSql andGte(String column, Object value) {
        return when(value, (q, v) -> q.and().add(column).gte(v));
    }

    public BindSql andGte(Function<ColumnNameProvider, String> column, Object value) {
        return when(value, (q, v) -> q.and().add(column.apply(ColumnNameProvider.INSTANCE)).gte(v));
    }

    public BindSql andLte(String column, Object value) {
        return when(value, (q, v) -> q.and().add(column).lte(v));
    }

    public BindSql andLte(Function<ColumnNameProvider, String> column, Object value) {
        return when(value, (q, v) -> q.and().add(column.apply(ColumnNameProvider.INSTANCE)).lte(v));
    }

    public BindSql andBetween(String column, Object left, Object right) {
        boolean vl = validateValue(left);
        boolean vr = validateValue(right);
        if (vl && vr) {
            return and().add(column).between(left, right);
        } else if (vl) {
            return and().add(column).gte(left);
        } else if (vr) {
            return and().add(column).lte(right);
        }
        return this;
    }

    public BindSql andBetween(Function<ColumnNameProvider, String> column, Object left, Object right) {
        boolean vl = validateValue(left);
        boolean vr = validateValue(right);
        if (vl && vr) {
            return and().add(column.apply(ColumnNameProvider.INSTANCE)).between(left, right);
        } else if (vl) {
            return and().add(column.apply(ColumnNameProvider.INSTANCE)).gte(left);
        } else if (vr) {
            return and().add(column.apply(ColumnNameProvider.INSTANCE)).lte(right);
        }
        return this;
    }

    public BindSql andIsNull(String column) {
        return and().add(column).isNull();
    }

    public BindSql andIsNull(Function<ColumnNameProvider, String> column) {
        return and().add(column.apply(ColumnNameProvider.INSTANCE)).isNull();
    }

    public BindSql andIsNotNull(String column) {
        return and().add(column).isNotNull();
    }

    public BindSql andIsNotNull(Function<ColumnNameProvider, String> column) {
        return and().add(column.apply(ColumnNameProvider.INSTANCE)).isNotNull();
    }

    public BindSql andLike(String column, Object value) {
        return when(value, (q, v) -> q.and().add(column).like(v));
    }

    public BindSql andLike(Function<ColumnNameProvider, String> column, Object value) {
        return when(value, (q, v) -> q.and().add(column.apply(ColumnNameProvider.INSTANCE)).like(v));
    }

    public BindSql andNotLike(String column, Object value) {
        return when(value, (q, v) -> q.and().add(column).not().like(v));
    }

    public BindSql andNotLike(Function<ColumnNameProvider, String> column, Object value) {
        return when(value, (q, v) -> q.and().add(column.apply(ColumnNameProvider.INSTANCE)).not().like(v));
    }

    public BindSql andStartsWith(String column, Object value) {
        return when(value, (q, v) -> q.and().add(column).startsWith(v));
    }

    public BindSql andStartsWith(Function<ColumnNameProvider, String> column, Object value) {
        return when(value, (q, v) -> q.and().add(column.apply(ColumnNameProvider.INSTANCE)).startsWith(v));
    }

    public BindSql andEndsWith(String column, Object value) {
        return when(value, (q, v) -> q.and().add(column).endsWith(v));
    }

    public BindSql andEndsWith(Function<ColumnNameProvider, String> column, Object value) {
        return when(value, (q, v) -> q.and().add(column.apply(ColumnNameProvider.INSTANCE)).endsWith(v));
    }

    public <E> BindSql andIn(String column, Iterator<E> value) {
        return when(value, (q, v) -> q.and().add(column).in(v));
    }

    public <E> BindSql andIn(Function<ColumnNameProvider, String> column, Iterator<E> value) {
        return when(value, (q, v) -> q.and().add(column.apply(ColumnNameProvider.INSTANCE)).in(v));
    }

    public <E> BindSql andIn(String column, Iterable<E> value) {
        return when(value, (q, v) -> q.and().add(column).in(v));
    }

    public <E> BindSql andIn(Function<ColumnNameProvider, String> column, Iterable<E> value) {
        return when(value, (q, v) -> q.and().add(column.apply(ColumnNameProvider.INSTANCE)).in(v));
    }

    public <E> BindSql andIn(String column, E... value) {
        return when(value, (q, v) -> q.and().add(column).in(v));
    }

    public <E> BindSql andIn(Function<ColumnNameProvider, String> column, E... value) {
        return when(value, (q, v) -> q.and().add(column.apply(ColumnNameProvider.INSTANCE)).in(v));
    }

    public <E> BindSql andNotIn(String column, Iterator<E> value) {
        return when(value, (q, v) -> q.and().add(column).not().in(v));
    }

    public <E> BindSql andNotIn(Function<ColumnNameProvider, String> column, Iterator<E> value) {
        return when(value, (q, v) -> q.and().add(column.apply(ColumnNameProvider.INSTANCE)).not().in(v));
    }

    public <E> BindSql andNotIn(String column, Iterable<E> value) {
        return when(value, (q, v) -> q.and().add(column).not().in(v));
    }

    public <E> BindSql andNotIn(Function<ColumnNameProvider, String> column, Iterable<E> value) {
        return when(value, (q, v) -> q.and().add(column.apply(ColumnNameProvider.INSTANCE)).not().in(v));
    }

    public <E> BindSql andNotIn(String column, E... value) {
        return when(value, (q, v) -> q.and().add(column).not().in(v));
    }

    public <E> BindSql andNotIn(Function<ColumnNameProvider, String> column, E... value) {
        return when(value, (q, v) -> q.and().add(column.apply(ColumnNameProvider.INSTANCE)).not().in(v));
    }


    public static class ColumnNameProvider {
        public static final ColumnNameProvider INSTANCE = new ColumnNameProvider();

        public String get(String name) {
            return name;
        }

        public <R, V> String get(IGetter<R, V> getter) {
            return column(getter);
        }

        public String get(String alias, String name) {
            if (alias != null && !alias.isEmpty()) {
                if (alias.endsWith(".")) {
                    return alias + name;
                }
                return alias + "." + name;
            }
            return name;
        }

        public <R, V> String get(String alias, IGetter<R, V> getter) {
            return get(alias, column(getter));
        }

        public String get(String alias, Field field) {
            return get(alias, column(field));
        }

        public String get(Field field) {
            return get(column(field));
        }

        public static <R, T> String column(IGetter<R, T> getter) {
            Field field = Lambda.getField(getter);
            if (field == null) {
                throw new IllegalArgumentException("cannot resolve column from getter");
            }
            return column(field);
        }

        public static String column(Field field) {
            return StringUtils.toSnake(field.getName());
        }

    }


    // 其他拓展

    public BindSql pretty() {
        return new BindSql(prettySql(this.sql), new ArrayList<>(this.args));
    }

    public BindSql trimComment() {
        return new BindSql(trimCommentSql(this.sql), new ArrayList<>(this.args));
    }

    public static final Pattern PATTERN_COMMENT = Pattern.compile("('[^']*')" + // 字符串，保持不变
                    "|(--[^\\n]*($|\\n))" + // 单行注释
                    "|(/\\*[^*]*\\*/)", // 多行注释
            Pattern.CASE_INSENSITIVE);

    public static String trimCommentSql(String sql) {
        if (sql == null || sql.isEmpty()) {
            return sql;
        }
        Matcher matcher = PATTERN_COMMENT.matcher(sql);
        StringBuilder builder = new StringBuilder();
        int lastIdx = 0;
        while (matcher.find()) {
            MatchResult result = matcher.toMatchResult();
            int start = result.start();
            int end = result.end();
            builder.append(sql.substring(lastIdx, start));
            String group = result.group();
            if (group.startsWith("'")) {
                builder.append(group);
            } else if (group.startsWith("--")
                    || group.startsWith("/*")) {

            } else {
                builder.append(group);
            }
            lastIdx = end;
        }
        if (lastIdx <= sql.length()) {
            builder.append(sql.substring(lastIdx));
        }
        return builder.toString();
    }

    public static final Pattern PATTERN_PRETTY = Pattern.compile("('[^']*')" + // 字符串，保持不变
                    "|(--[^\\n]*($|\\n))" + // 单行注释，不变
                    "|(/\\*[^*]*\\*/)" + // 多行注释，不变
                    "|((^|\\s)(select|from|join|on|where|group|having|delete|insert|values|create|and|or|update|set|drop|grant|revoke)(\\s|$))" + // 换行关键字
                    "|([()])" + // 括号，控制缩进深度
                    "|[,]", // 逗号换行
            Pattern.CASE_INSENSITIVE);

    public static String prettySql(String sql) {
        if (sql == null || sql.isEmpty()) {
            return sql;
        }
        Matcher matcher = PATTERN_PRETTY.matcher(sql);
        StringBuilder builder = new StringBuilder();
        int level = 0;
        int lastIdx = 0;
        while (matcher.find()) {
            MatchResult result = matcher.toMatchResult();
            int start = result.start();
            int end = result.end();
            builder.append(sql.substring(lastIdx, start));
            String group = result.group();
            if (group.startsWith("(")) {
                builder.append(group);
                level++;
            } else if (group.startsWith(")")) {
                builder.append(group);
                level--;
                if (level < 0) {
                    level = 0;
                }
            } else if (group.startsWith("'")
                    || group.startsWith("--")
                    || group.startsWith("/*")) {
                builder.append(group);
            } else if (group.startsWith(",")) {
                for (int i = 0; i < level; i++) {
                    builder.append("\t");
                }
                builder.append(group);
                builder.append("\n");
            } else {
                builder.append("\n");
                for (int i = 0; i < level; i++) {
                    builder.append("\t");
                }
                builder.append(group);
            }
            lastIdx = end;
        }
        if (lastIdx <= sql.length()) {
            builder.append(sql.substring(lastIdx));
        }
        return builder.toString().trim();
    }

    public String toMergeSql() {
        return toMergeSql(DefaultMergeSqlValueConvertor.DEFAULT);
    }

    public static final Pattern PATTERN_MERGE = Pattern.compile("('[^']*')" + // 字符串，保持不变
            "|(--[^\\n]*($|\\n))" + // 单行注释，不变
            "|(/\\*[^*]*\\*/)" + // 多行注释，不变
            "|(\\?)" // 占位符
    );

    public String toMergeSql(Function<Object, String> valueConvertor) {
        String sql = this.sql;
        if (sql == null || sql.isEmpty()) {
            return sql;
        }
        Matcher matcher = PATTERN_MERGE.matcher(sql);
        StringBuilder builder = new StringBuilder();
        int valueIdx = 0;
        int lastIdx = 0;
        while (matcher.find()) {
            MatchResult result = matcher.toMatchResult();
            int start = result.start();
            int end = result.end();
            builder.append(sql.substring(lastIdx, start));
            String group = result.group();
            if (group.startsWith("'")
                    || group.startsWith("--")
                    || group.startsWith("/*")) {
                builder.append(group);
            } else {
                if (valueIdx < this.args.size()) {
                    builder.append(valueConvertor.apply(this.args.get(valueIdx++)));
                } else {
                    // 参数不足替换失败
                    builder.append("?");
                }
            }
            lastIdx = end;
        }
        if (lastIdx <= sql.length()) {
            builder.append(sql.substring(lastIdx));
        }
        return builder.toString();
    }

    public static class DefaultMergeSqlValueConvertor implements Function<Object, String> {
        public static final DefaultMergeSqlValueConvertor DEFAULT = new DefaultMergeSqlValueConvertor();
        public static final DateTimeFormatter FMT_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        public static final DateTimeFormatter FMT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        public static final DateTimeFormatter FMT_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
        public static final ThreadLocal<SimpleDateFormat> FMT_S_DATE = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        @Override
        public String apply(Object value) {
            if (value == null) {
                return onNull();
            }
            if (value instanceof Number) {
                return onNumber((Number) value);
            }
            if (value instanceof CharSequence) {
                return onCharSequence((CharSequence) value);
            }
            if (value instanceof Date) {
                return onDate((Date) value);
            }
            if (value instanceof LocalDateTime) {
                return onLocalDateTime((LocalDateTime) value);
            }
            if (value instanceof LocalDate) {
                return onLocalDate((LocalDate) value);
            }
            if (value instanceof LocalTime) {
                return onLocalTime((LocalTime) value);
            }
            return onObject(value);
        }

        public String onNull() {
            return "null";
        }

        public String onNumber(Number value) {
            return String.valueOf(value);
        }

        public String onCharSequence(CharSequence value) {
            return escapeSqlString(String.valueOf(value));
        }

        public String onDate(Date value) {
            return escapeSqlString(FMT_S_DATE.get().format(value));
        }

        public String onLocalDateTime(LocalDateTime value) {
            return escapeSqlString(FMT_DATE_TIME.format(value));
        }

        public String onLocalDate(LocalDate value) {
            return escapeSqlString(FMT_DATE.format(value));
        }

        public String onLocalTime(LocalTime value) {
            return escapeSqlString(FMT_TIME.format(value));
        }

        public String onObject(Object value) {
            return escapeSqlString(String.valueOf(value));
        }
    }

    public static class OracleMergeSqlValueConvertor extends DefaultMergeSqlValueConvertor {
        public static final OracleMergeSqlValueConvertor ORACLE = new OracleMergeSqlValueConvertor();

        @Override
        public String onDate(Date value) {
            return "to_date(" + escapeSqlString(FMT_S_DATE.get().format(value)) + ",'yyyy-mm-dd hh24:mi:ss')";
        }

        @Override
        public String onLocalDateTime(LocalDateTime value) {
            return "to_date(" + escapeSqlString(FMT_DATE_TIME.format(value)) + ",'yyyy-mm-dd hh24:mi:ss')";
        }

        @Override
        public String onLocalDate(LocalDate value) {
            return "to_date(" + escapeSqlString(FMT_DATE.format(value)) + ",'yyyy-mm-dd')";
        }

        @Override
        public String onLocalTime(LocalTime value) {
            return "to_date(" + escapeSqlString(FMT_TIME.format(value)) + ",'hh24:mi:ss')";
        }
    }

    public String toOracleMergeSql() {
        return toMergeSql(OracleMergeSqlValueConvertor.ORACLE);
    }

}
