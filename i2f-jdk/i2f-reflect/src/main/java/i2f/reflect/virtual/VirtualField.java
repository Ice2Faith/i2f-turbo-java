package i2f.reflect.virtual;

/**
 * @author Ice2Faith
 * @date 2024/9/28 9:03
 */
public interface VirtualField {
    String name();

    Class<?> type();

    boolean readable();

    boolean writeable();
}
