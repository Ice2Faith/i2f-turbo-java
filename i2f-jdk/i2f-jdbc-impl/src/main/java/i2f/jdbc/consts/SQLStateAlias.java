package i2f.jdbc.consts;

/**
 * SqlState 枚举的别名
 *
 * @author Ice2Faith
 * @date 2025/4/17 16:25
 */
public enum SQLStateAlias {
    DIVIDE_ZERO(SQLState.C_22012);

    private SQLState raw;

    private SQLStateAlias(SQLState raw) {
        this.raw = raw;
    }

    public SQLState raw() {
        return this.raw;
    }
}
