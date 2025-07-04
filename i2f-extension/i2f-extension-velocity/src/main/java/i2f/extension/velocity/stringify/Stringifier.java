package i2f.extension.velocity.stringify;

/**
 * @author Ice2Faith
 * @date 2025/4/2 8:51
 */
public interface Stringifier {
    boolean support(Object obj);

    String stringify(Object obj);
}
