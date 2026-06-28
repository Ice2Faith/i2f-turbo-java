package i2f.springboot.ops.openai.tool.impl.sql;

/**
 * @author Ice2Faith
 * @date 2026/6/4 10:49
 * @desc
 */
public interface OpsSqlValidator {
    void validateQuery(String sql);
}
