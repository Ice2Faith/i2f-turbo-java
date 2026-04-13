package i2f.lru;


/**
 * @author Ice2Faith
 * @date 2026/4/13 11:35
 * @desc
 */
public interface UncheckedAutoCloseable extends AutoCloseable {

    @Override
    default void close() {
        try {
            doClose();
        } catch (Throwable e) {
            throw new UncheckedWrappedException(e.getMessage(), e);
        }
    }

    void doClose() throws Throwable;
}
