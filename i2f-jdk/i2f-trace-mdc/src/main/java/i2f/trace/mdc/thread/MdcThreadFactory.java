package i2f.trace.mdc.thread;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;

/**
 * @author Ice2Faith
 * @date 2026/4/15 10:18
 * @desc
 */
public class MdcThreadFactory implements ThreadFactory {
    protected final ThreadFactory delegate;

    public MdcThreadFactory(ThreadFactory delegate) {
        Objects.requireNonNull(delegate, "delegate cannot be null");
        this.delegate = delegate;
    }

    public static MdcThreadFactory of(ThreadFactory pool) {
        if (pool instanceof MdcThreadFactory) {
            return (MdcThreadFactory) pool;
        }
        return new MdcThreadFactory(pool);
    }

    @Override
    public Thread newThread(Runnable r) {
        return this.delegate.newThread(MdcRunnable.of(r));
    }
}
