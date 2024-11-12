package i2f.springboot.trace.mdc.spring.scheduling;

import i2f.trace.mdc.TraceMdcUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2024/11/12 17:18
 */
@ConditionalOnClass({
        Scheduled.class,
        Aspect.class,
})
@Component
@Aspect
@EnableAspectJAutoProxy
public class SpringSchedulingTraceSlf4jMdcAspect {

    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void injectPointCut() {

    }

    @Around("injectPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        String traceId = TraceMdcUtil.genTraceIdWithSet();
        String traceSource = "@Scheduled," + (method.getDeclaringClass().getSimpleName() + "." + method.getName());

        TraceMdcUtil.TRACE_SOURCE.set(traceSource);

        MDC.put(TraceMdcUtil.MDC_TRACE_ID_NAME, traceId);
        MDC.put(TraceMdcUtil.MDC_TRACE_SOURCE_NAME, TraceMdcUtil.TRACE_SOURCE.get());

        try {
            return pjp.proceed();
        } finally {
            MDC.remove(TraceMdcUtil.MDC_TRACE_ID_NAME);
            MDC.remove(TraceMdcUtil.MDC_TRACE_SOURCE_NAME);

            TraceMdcUtil.TRACE_ID.remove();
            TraceMdcUtil.TRACE_SOURCE.remove();
        }
    }
}
