package i2f.trace.mdc.thread;

import i2f.trace.mdc.MdcHolder;
import i2f.trace.mdc.MdcTraces;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author Ice2Faith
 * @date 2026/4/15 9:22
 * @desc
 */
public class MdcCallable<T> implements Callable<T> {
    protected final Callable<T> callable;
    protected final boolean newTrace;
    private volatile String traceId;
    private volatile String traceSource;

    public MdcCallable(Callable<T> callable) {
        this(callable, false);
    }

    public MdcCallable(Callable<T> callable, boolean newTrace) {
        Objects.requireNonNull(callable, "callable cannot be null");
        this.callable = callable;
        this.newTrace = newTrace;
        this.traceId = MdcHolder.get(MdcTraces.TRACE_ID);
        this.traceSource = MdcHolder.get(MdcTraces.TRACE_SOURCE);
    }

    public static <T> MdcCallable<T> of(Callable<T> callable) {
        if (callable instanceof MdcCallable) {
            return (MdcCallable<T>) callable;
        }
        return new MdcCallable<>(callable, false);
    }

    public static <T> MdcCallable<T> ofNewTrace(Callable<T> callable) {
        return new MdcCallable<>(callable, true);
    }

    public static <T> MdcCallable<T> of(Callable<T> callable, boolean newTrace) {
        if (!newTrace) {
            if (callable instanceof MdcCallable) {
                return (MdcCallable<T>) callable;
            }
        }
        return new MdcCallable<>(callable, newTrace);
    }

    @Override
    public T call() throws Exception {
        try {
            String traceId = this.traceId;
            String traceSource = this.traceSource;
            // 在子线程中设置 MDC 上下文
            if (newTrace) {
                traceId = MdcTraces.genTraceId();
                traceSource = null;
            }
            MdcHolder.put(MdcTraces.TRACE_ID, traceId);
            MdcHolder.put(MdcTraces.TRACE_SOURCE, traceSource);

            return callable.call();
        } finally {
            // 执行完后清理，防止线程复用导致的数据污染
            MdcHolder.clear();
        }
    }
}
