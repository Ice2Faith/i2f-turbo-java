package i2f.database.dialect;


import i2f.database.type.DatabaseType;

/**
 * 将Object对象转换为SQL字符串
 * 使用场景：
 * 在不适用占位符的情况下进行拼接SQL时使用
 * 例如，拼接Date,Number,String等值
 * 这样就能够进行转换为正确的SQL值
 * 不至于出现SQL错误或者出现SQL注入的情况
 * --------------------------
 * 也或者使用于针对参数化语句的情况
 * 在日志中打印出完整的语句
 * 而不是占位符和变量分开的情况
 * --------------------------
 *
 * @author Ice2Faith
 * @date 2025/6/26 14:01
 */
public interface DatabaseObject2SqlStringifier {
    boolean support(DatabaseType databaseType);

    String stringify(Object value);
}
