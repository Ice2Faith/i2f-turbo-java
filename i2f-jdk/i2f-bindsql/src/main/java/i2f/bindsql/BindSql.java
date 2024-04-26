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
    protected boolean update = false;
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

    public BindSql(boolean update, String sql, List<Object> args) {
        this.update = update;
        this.sql = sql;
        this.args = args;
    }

    public static BindSql of(boolean update, String sql, Object... args) {
        return of(sql, args).setUpdate(update);
    }

    public static BindSql of(String sql, Object... args) {
        List<Object> list = new ArrayList<>();
        for (Object arg : args) {
            list.add(arg);
        }
        return new BindSql(sql, list);
    }

    public boolean isUpdate() {
        return update;
    }

    public BindSql setUpdate(boolean update) {
        this.update = update;
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
        return update == bindSql.update &&
                Objects.equals(sql, bindSql.sql) &&
                Objects.equals(args, bindSql.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(update, sql, args);
    }

    @Override
    public String toString() {
        return "BindSql{\n" +
                "update  = " + update + "\n" +
                ", sql   = " + sql + "\n" +
                ", args  = " + args + "\n" +
                "}";
    }
}
