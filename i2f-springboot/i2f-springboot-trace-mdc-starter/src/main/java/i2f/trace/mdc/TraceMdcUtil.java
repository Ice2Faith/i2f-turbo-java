package i2f.trace.mdc;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/11/12 15:40
 */
public class TraceMdcUtil {
    public static final String MDC_TRACE_ID_NAME = "traceId";
    public static final String MDC_TRACE_SOURCE_NAME = "traceSource";
    public static final String[] POSSIBLE_TRACE_ID_NAMES = {MDC_TRACE_ID_NAME, "TRACE_ID", "trace-id", "trace"};
    public static final String[] POSSIBLE_TRACE_SOURCE_NAMES = {MDC_TRACE_SOURCE_NAME, "TRACE_SOURCE", "trace-source", "source"};

    public static final ThreadLocal<String> TRACE_ID = new ThreadLocal<>();
    public static final ThreadLocal<String> TRACE_SOURCE = new ThreadLocal<>();

    public static String genTraceId() {
        String ret = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        return ret;
    }

    public static String genTraceIdWithSet() {
        String traceId = genTraceId();
        TRACE_ID.set(traceId);
        return traceId;
    }

    public static <T> String fetchOrGenTraceIdWithSet(T arg, Function<T, String> supplier) {
        String traceId = fetchOrGenTraceId(arg, supplier);
        TRACE_ID.set(traceId);
        return traceId;
    }

    public static String getOrGenTraceIdWithSet(Supplier<String> supplier) {
        String traceId = getOrGenTraceId(supplier);
        TRACE_ID.set(traceId);
        return traceId;
    }

    public static <T> String fetchOrGenTraceId(T arg, Function<T, String> supplier) {
        String ret = supplier.apply(arg);
        if (ret == null || ret.isEmpty()) {
            ret = genTraceId();
        }
        return ret;
    }

    public static String getOrGenTraceId(Supplier<String> supplier) {
        String ret = supplier.get();
        if (ret == null || ret.isEmpty()) {
            ret = genTraceId();
        }
        return ret;
    }

}
