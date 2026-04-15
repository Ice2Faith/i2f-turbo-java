package i2f.springboot.trace.mdc.powerjob;

import i2f.trace.mdc.MdcHolder;
import i2f.trace.mdc.MdcTraces;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.annotation.PowerJobHandler;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2024/11/12 19:20
 */
@ConditionalOnExpression("${i2f.springboot.trace.mdc.power-job.enable:true}")
@ConditionalOnClass({
        PowerJobHandler.class,
        Aspect.class,
})
@Component
@Aspect
public class MdcPowerJobAspect {

    @Pointcut("@annotation(tech.powerjob.worker.annotation.PowerJobHandler)")
    public void injectPointCut() {

    }

    @Around("injectPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        PowerJobHandler ann = method.getDeclaredAnnotation(PowerJobHandler.class);
        String traceId = MdcTraces.genTraceId();
        String traceSource = "@PowerJobHandler," + ((ann == null) ? (method.getDeclaringClass().getSimpleName() + "." + method.getName()) : (ann.name()));

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
