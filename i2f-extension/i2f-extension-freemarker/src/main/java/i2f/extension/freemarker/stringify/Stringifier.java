package i2f.extension.freemarker.stringify;

/**
 * @author Ice2Faith
 * @date 2025/4/2 8:51
 */
public interface Stringifier {
    boolean support(Object obj);

    String stringify(Object obj);
}
