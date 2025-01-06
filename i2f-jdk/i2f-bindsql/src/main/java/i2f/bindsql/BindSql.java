package i2f.bindsql;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/4/7 16:52
 * @desc
 */
public class BindSql {
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

    @Override
    public String toString() {
        return "BindSql{\n" +
                "type  = " + type + "\n" +
                ", sql   = " + sql + "\n" +
                ", args  = " + args + "\n" +
                "}";
    }
}
