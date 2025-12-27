package i2f.springboot.limit.aop;

import i2f.springboot.limit.core.LimitManager;
import i2f.springboot.limit.core.LimitType;
import i2f.springboot.limit.data.LimitRule;
import i2f.springboot.limit.exception.LimitException;
import i2f.springboot.limit.util.LimitUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2025/11/11 17:56
 */
@ConditionalOnExpression("${i2f.springboot.limit.aop.enable:true}")
@Data
@NoArgsConstructor
@Aspect
@Component
public class LimitAop {

    @Autowired
    private LimitManager manager;

    @Pointcut("@annotation(i2f.springboot.limit.aop.Limited)")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature pjpSignature = (MethodSignature) pjp.getSignature();
        Method method = pjpSignature.getMethod();
        Limited ann = method.getAnnotation(Limited.class);
        if (ann != null) {
            LimitRule rule = new LimitRule();
            rule.setCount(ann.count());
            rule.setTtl(ann.ttl());

            String signature = LimitUtil.getMethodSignature(method);

            manager.registryIfAbsentRule(LimitType.API, signature, rule);

            boolean limited = manager.isLimited(LimitType.API, signature);
            if (limited) {
                throw new LimitException("your request has been limited by request api rule!");
            }
        }
        return pjp.proceed();
    }
}
