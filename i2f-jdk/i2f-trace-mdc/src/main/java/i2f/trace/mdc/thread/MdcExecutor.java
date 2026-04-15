package i2f.trace.mdc.thread;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * @author Ice2Faith
 * @date 2026/4/15 9:30
 * @desc
 */
public class MdcExecutor implements Executor {
    protected final Executor delegate;

    public MdcExecutor(Executor delegate) {
        Objects.requireNonNull(delegate, "delegate cannot be null");
        this.delegate = delegate;
    }

    public static MdcExecutor of(Executor pool) {
        if (pool instanceof MdcExecutor) {
            return (MdcExecutor) pool;
        }
        return new MdcExecutor(pool);
    }

    @Override
    public void execute(Runnable command) {
        this.delegate.execute(MdcRunnable.of(command));
    }

}
