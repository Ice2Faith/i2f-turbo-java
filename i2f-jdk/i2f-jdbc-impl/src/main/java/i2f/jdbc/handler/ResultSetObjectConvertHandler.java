package i2f.jdbc.handler;

/**
 * @author Ice2Faith
 * @date 2025/4/2 9:25
 */
public interface ResultSetObjectConvertHandler {
    boolean support(Object obj);

    Object convert(Object obj);
}
