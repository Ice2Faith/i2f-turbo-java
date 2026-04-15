package i2f.trace.mdc.thread;

import i2f.trace.mdc.MdcHolder;
import i2f.trace.mdc.MdcTraces;

import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2026/4/15 9:22
 * @desc
 */
public class MdcRunnable implements Runnable {
    protected final Runnable runnable;
    protected final boolean newTrace;
    private volatile String traceId;
    private volatile String traceSource;

    public MdcRunnable(Runnable runnable) {
        this(runnable, false);
    }

    public MdcRunnable(Runnable runnable, boolean newTrace) {
        Objects.requireNonNull(runnable, "runnable cannot be null");
        this.runnable = runnable;
        this.newTrace = newTrace;
        this.traceId = MdcHolder.get(MdcTraces.TRACE_ID);
        this.traceSource = MdcHolder.get(MdcTraces.TRACE_SOURCE);
    }

    public static MdcRunnable of(Runnable runnable) {
        if (runnable instanceof MdcRunnable) {
            return (MdcRunnable) runnable;
        }
        return new MdcRunnable(runnable, false);
    }

    public static MdcRunnable ofNewTrace(Runnable runnable) {
        return new MdcRunnable(runnable, true);
    }

    public static MdcRunnable of(Runnable runnable, boolean newTrace) {
        if (!newTrace) {
            if (runnable instanceof MdcRunnable) {
                return (MdcRunnable) runnable;
            }
        }
        return new MdcRunnable(runnable, newTrace);
    }

    @Override
    public void run() {
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

            runnable.run();
        } finally {
            // 执行完后清理，防止线程复用导致的数据污染
            MdcHolder.clear();
        }
    }
}
