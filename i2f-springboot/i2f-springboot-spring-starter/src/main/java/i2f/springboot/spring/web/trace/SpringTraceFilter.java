package i2f.springboot.spring.web.trace;

import i2f.web.filter.TraceFilter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ice2Faith
 * @date 2024/10/23 20:25
 * @desc
 */
@Slf4j
public class SpringTraceFilter extends TraceFilter {
    @Override
    public String findTraceId(HttpServletRequest request, HttpServletResponse response) {
        return MDC.get(TRACE_ID_ATTR);
    }

    @Override
    public void onBefore(HttpServletRequest request, HttpServletResponse response) {
        MDC.put(TRACE_ID_ATTR, TRACE_ID.get());
        MDC.put(TRACE_SOURCE_ATTR, TRACE_SOURCE.get());
        log.info("traceId=" + TRACE_ID.get() + ", source=" + TRACE_SOURCE.get());
    }

    @Override
    public void onAfter(HttpServletRequest request, HttpServletResponse response) {
        MDC.remove(TRACE_ID_ATTR);
        MDC.remove(TRACE_SOURCE_ATTR);
    }
}
