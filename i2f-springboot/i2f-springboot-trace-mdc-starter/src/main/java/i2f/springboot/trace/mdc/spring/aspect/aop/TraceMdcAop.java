package i2f.springboot.trace.mdc.spring.aspect.aop;

import i2f.trace.mdc.TraceMdcUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2024/12/6 16:04
 */
@ConditionalOnClass({
        Aspect.class,
        Pointcut.class,
        ProceedingJoinPoint.class
})
@ConditionalOnExpression("${i2f.spring.trace.mdc.aspect:true}")
@Data
@NoArgsConstructor
@Slf4j
@Aspect
@EnableAspectJAutoProxy
public class TraceMdcAop {

    @Pointcut("@annotation(i2f.springboot.trace.mdc.spring.aspect.annotation.TraceMdc)")
    public void annPointCut() {
    }

    @Around("annPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        String traceId = TraceMdcUtil.getOrGenTraceIdWithSet(() -> {
            String r = TraceMdcUtil.TRACE_ID.get();
            if (r == null) {
                r = MDC.get(TraceMdcUtil.MDC_TRACE_ID_NAME);
            }
            return r;
        });
        String traceSource = "@Aop," + (method.getDeclaringClass().getSimpleName() + "." + method.getName());

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
