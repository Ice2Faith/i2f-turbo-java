package i2f.mixin.impl;

/**
 * @author Ice2Faith
 * @date 2026/5/14 9:48
 * @desc
 */
public interface ThreadMixins {

    default long thread_id() {
        return Thread.currentThread().getId();
    }

    default String thread_name() {
        return Thread.currentThread().getName();
    }

    default void yield() {
        Thread.yield();
    }

    default void sleep(long seconds) {
        if (seconds >= 0) {
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    default void sleep_ms(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    default void sleep_sec(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
