package i2f.springboot.ops.common;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/3/12 9:43
 * @desc
 */
public class OpsTimeCounter {
    protected static InheritableThreadLocal<Long> beginTs = new InheritableThreadLocal<>();
    protected static InheritableThreadLocal<Long> endTs = new InheritableThreadLocal<>();

    public static void begin() {
        long ts = System.currentTimeMillis();
        beginTs.set(ts);
        endTs.set(ts);
    }

    public static long end() {
        long ts = System.currentTimeMillis();
        endTs.set(ts);
        return endTs.get() - beginTs.get();
    }

    public static long end(TimeUnit unit) {
        long ms = end();
        return unit.convert(ms, TimeUnit.MILLISECONDS);
    }

}
