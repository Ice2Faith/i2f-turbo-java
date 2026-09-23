package i2f.springboot.redisson.annotation;

import org.redisson.api.RateIntervalUnit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLimiter {
    /**
     * redisson limiter 的 rate
     */
    long value();

    long rateInterval() default 1;

    RateIntervalUnit unit() default RateIntervalUnit.SECONDS;
}
