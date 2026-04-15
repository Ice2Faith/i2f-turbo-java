package i2f.springboot.trace.mdc.aspect.aop;

import i2f.trace.mdc.MdcHolder;
import i2f.trace.mdc.MdcTraces;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
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
@ConditionalOnExpression("${i2f.springboot.trace.mdc.aspect.enable:true}")
@Data
@NoArgsConstructor
@Slf4j
@Aspect
@EnableAspectJAutoProxy
public class MdcAnnotationAspect {

    @Pointcut("@annotation(i2f.springboot.trace.mdc.aspect.annotation.MdcTrace)")
    public void annPointCut() {
    }

    @Around("annPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        String traceId = MdcTraces.getOrGenTraceId(() -> {
            return MdcHolder.get(MdcTraces.TRACE_ID);
        });
        String traceSource = "@Aop," + (method.getDeclaringClass().getSimpleName() + "." + method.getName());

        MdcHolder.put(MdcTraces.TRACE_ID, traceId);
        MdcHolder.put(MdcTraces.TRACE_SOURCE, traceSource);

        try {
            return pjp.proceed();
        } finally {
            MdcHolder.remove(MdcTraces.TRACE_ID);
            MdcHolder.remove(MdcTraces.TRACE_SOURCE);
        }
    }
}
