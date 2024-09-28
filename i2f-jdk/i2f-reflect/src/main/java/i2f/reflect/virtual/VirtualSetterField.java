package i2f.reflect.virtual;

/**
 * @author Ice2Faith
 * @date 2024/9/28 9:01
 */
public interface VirtualSetterField extends VirtualField {
    void set(Object obj, Object value);

    @Override
    default boolean readable() {
        return false;
    }

    @Override
    default boolean writeable() {
        return true;
    }
}
