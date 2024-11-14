package i2f.springboot.trace.mdc.spring.springmvc;

import i2f.trace.mdc.TraceMdcUtil;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/11/12 16:07
 */
public class WebTraceSlf4jMdcFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String traceId = TraceMdcUtil.fetchOrGenTraceIdWithSet(request, (req) -> {
            for (String name : TraceMdcUtil.POSSIBLE_TRACE_ID_NAMES) {
                String str = (String) req.getAttribute(name);
                if (StringUtils.hasLength(str)) {
                    return str;
                }
            }
            for (String name : TraceMdcUtil.POSSIBLE_TRACE_ID_NAMES) {
                String str = req.getHeader(name);
                if (StringUtils.hasLength(str)) {
                    return str;
                }
            }
            for (String name : TraceMdcUtil.POSSIBLE_TRACE_ID_NAMES) {
                String str = req.getParameter(name);
                if (StringUtils.hasLength(str)) {
                    return str;
                }
            }
            return null;
        });

        TraceMdcUtil.TRACE_SOURCE.set(String.valueOf(request.getRequestURL()));

        MDC.put(TraceMdcUtil.MDC_TRACE_ID_NAME, traceId);
        MDC.put(TraceMdcUtil.MDC_TRACE_SOURCE_NAME, TraceMdcUtil.TRACE_SOURCE.get());

        request.setAttribute(TraceMdcUtil.MDC_TRACE_ID_NAME, traceId);
        request.setAttribute(TraceMdcUtil.MDC_TRACE_SOURCE_NAME, TraceMdcUtil.TRACE_SOURCE.get());

        request = new HttpServletRequestWrapper(request) {
            @Override
            public String getHeader(String name) {
                for (String item : TraceMdcUtil.POSSIBLE_TRACE_ID_NAMES) {
                    if (item.equalsIgnoreCase(name)) {
                        String ret = traceId;
                        if (ret == null) {
                            return "";
                        }
                        return ret;
                    }
                }
                for (String item : TraceMdcUtil.POSSIBLE_TRACE_SOURCE_NAMES) {
                    if (item.equalsIgnoreCase(name)) {
                        String ret = TraceMdcUtil.TRACE_SOURCE.get();
                        if (ret == null) {
                            return "";
                        }
                        return ret;
                    }
                }
                return super.getHeader(name);
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                for (String item : TraceMdcUtil.POSSIBLE_TRACE_ID_NAMES) {
                    if (item.equalsIgnoreCase(name)) {
                        String ret = traceId;
                        if (ret == null) {
                            ret = "";
                        }
                        return Collections.enumeration(Collections.singletonList(ret));
                    }
                }
                for (String item : TraceMdcUtil.POSSIBLE_TRACE_SOURCE_NAMES) {
                    if (item.equalsIgnoreCase(name)) {
                        String ret = TraceMdcUtil.TRACE_SOURCE.get();
                        if (ret == null) {
                            ret = "";
                        }
                        return Collections.enumeration(Collections.singletonList(ret));
                    }
                }
                return super.getHeaders(name);
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                List<String> names = new ArrayList<>();
                Enumeration<String> headers = super.getHeaderNames();
                while (headers.hasMoreElements()) {
                    names.add(headers.nextElement());
                }
                names.add(TraceMdcUtil.MDC_TRACE_ID_NAME);
                return Collections.enumeration(names);
            }
        };

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(TraceMdcUtil.MDC_TRACE_ID_NAME);
            MDC.remove(TraceMdcUtil.MDC_TRACE_SOURCE_NAME);

            TraceMdcUtil.TRACE_ID.remove();
            TraceMdcUtil.TRACE_SOURCE.remove();
        }
    }
}
