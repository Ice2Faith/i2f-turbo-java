package i2f.springboot.trace.mdc.xxljob;

import com.xxl.job.core.handler.annotation.XxlJob;
import i2f.trace.mdc.MdcHolder;
import i2f.trace.mdc.MdcTraces;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2024/11/12 17:08
 */
@ConditionalOnExpression("${i2f.springboot.trace.mdc.xxl-job.enable:true}")
@ConditionalOnClass({
        XxlJob.class,
        Aspect.class,
})
@Component
@Aspect
@EnableAspectJAutoProxy
public class MdcXxlJobAspect {

    @Pointcut("@annotation(com.xxl.job.core.handler.annotation.XxlJob)")
    public void injectPointCut() {

    }

    @Around("injectPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        XxlJob ann = method.getDeclaredAnnotation(XxlJob.class);
        String traceId = MdcTraces.genTraceId();
        String traceSource = "@XxlJob," + ((ann == null) ? (method.getDeclaringClass().getSimpleName() + "." + method.getName()) : (ann.value()));

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
