package i2f.trace.mdc;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2026/4/15 9:08
 * @desc
 */
public class MdcTraces {
    public static final String TRACE_ID = "traceId";
    public static final String TRACE_SOURCE = "traceSource";
    public static final String TRACE_URL = "traceUrl";
    public static final String TRACE_IP = "traceIp";
    public static final String TRACE_APP = "traceApp";

    public static final String[] TRACE_SOURCE_HEADERS = {
            TRACE_SOURCE,
            "X-Trace-Source",
            "Trace-Source",
            "X-Request-Source",
            "Request-Source",
            "RequestSource",
    };

    public static final String[] TRACE_ID_HEADERS = {
            TRACE_ID,
            "X-Trace-Id",
            "Trace-Id",
            "X-Request-Id",
            "Request-Id",
            "RequestId",
    };

    public static String genTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getOrGenTraceId(Supplier<String> supplier) {
        String traceId = supplier.get();
        if (traceId == null || traceId.isEmpty()) {
            traceId = genTraceId();
        }
        return traceId;
    }
}
