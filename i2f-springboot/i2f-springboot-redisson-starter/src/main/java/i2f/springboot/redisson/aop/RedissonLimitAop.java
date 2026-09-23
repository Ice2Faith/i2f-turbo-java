package i2f.springboot.redisson.aop;

import i2f.springboot.redisson.annotation.RedisLimiter;
import i2f.springboot.redisson.annotation.RedisLock;
import i2f.springboot.redisson.exception.RedissonLimitException;
import i2f.springboot.redisson.limit.RedissonRateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Ice2Faith
 * @date 2026/9/22 16:05
 * @desc
 */
@ConditionalOnExpression("${i2f.redission.limit.enable:true}")
@ConditionalOnClass(Aspect.class)
@Slf4j
@Aspect
@Component
public class RedissonLimitAop {

    @Pointcut("@annotation(i2f.springboot.redisson.annotation.RedisLimiter)")
    public void limitPointCut() {
    }

    @Autowired
    private RedissonClient redissonClient;

    @Around("limitPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Object _target = pjp.getTarget();
        Object _this = pjp.getThis();
        Method method = signature.getMethod();
        Object[] args = pjp.getArgs();
        Parameter[] params = method.getParameters();

        RedisLimiter lann = getAnnotation(method, RedisLimiter.class);
        if (lann != null) {
            String key="redisson:limit:method:"+method;
            RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
            if(!rateLimiter.isExists()){
                rateLimiter.setRate(RateType.OVERALL, lann.value(), lann.rateInterval(), lann.unit());
            }
            RedissonRateLimiter limiter = new RedissonRateLimiter(rateLimiter);
            if (!limiter.tryAcquire()) {
                throw new RedissonLimitException("method has limited!");
            }
        }

        return pjp.proceed();
    }

    public static <T extends Annotation> T getAnnotation(AnnotatedElement elem, Class<T> annClazz) {
        T ann = elem.getDeclaredAnnotation(annClazz);
        if (ann == null) {
            ann = elem.getAnnotation(annClazz);
        }
        return ann;
    }
}
