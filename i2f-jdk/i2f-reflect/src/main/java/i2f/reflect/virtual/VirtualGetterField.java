package i2f.reflect.virtual;

/**
 * @author Ice2Faith
 * @date 2024/9/28 9:00
 */
public interface VirtualGetterField extends VirtualField {
    Object get(Object obj);

    @Override
    default boolean readable() {
        return true;
    }

    @Override
    default boolean writeable() {
        return false;
    }
}
