package i2f.web.filter;

import i2f.clock.SystemClock;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2024/10/23 20:25
 * @desc
 */
public class TraceFilter extends OncePerHttpServletFilter {
    public static final String TRACE_ID_HEADER = "trace-id";
    public static final String TRACE_ID_ATTR = "traceId";
    public static final String TRACE_SOURCE_ATTR = "traceSource";
    public static final ThreadLocal<String> TRACE_ID = new ThreadLocal<>();
    public static final ThreadLocal<String> TRACE_SOURCE = new ThreadLocal<>();

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String traceId = (String) request.getAttribute(TRACE_ID_ATTR);
        if (traceId == null) {
            traceId = request.getHeader(TRACE_ID_HEADER);
        }
        if (traceId == null) {
            traceId = request.getParameter(TRACE_ID_ATTR);
        }
        if (traceId == null) {
            traceId = findTraceId(request, response);
        }
        if (traceId == null) {
            traceId = SystemClock.currentTimeMillis() + "-" + UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        }
        TRACE_ID.set(traceId);
        TRACE_SOURCE.set(String.format("http [%5s] [%20s] %s", request.getMethod(), request.getContentType(), String.valueOf(request.getRequestURL())));
        request.setAttribute(TRACE_ID_ATTR, TRACE_ID.get());
        request.setAttribute(TRACE_SOURCE_ATTR, TRACE_SOURCE.get());
        try {
            onBefore(request, response);
            chain.doFilter(request, response);
        } finally {
            onAfter(request, response);
            TRACE_SOURCE.remove();
            TRACE_ID.remove();
        }
    }

    public String findTraceId(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    public void onBefore(HttpServletRequest request, HttpServletResponse response) {

    }

    public void onAfter(HttpServletRequest request, HttpServletResponse response) {

    }
}
