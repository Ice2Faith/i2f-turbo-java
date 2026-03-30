package i2f.ai.std.service.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author Ice2Faith
 * @date 2026/3/30 8:56
 * @desc
 */
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AiSystem {

    String[] value() default {};

    /**
     * 当在 method 上的时候，允许使用format生效，
     * 使用format之后，参数将不会自动注入，而是通过format进行定义
     */
    boolean format() default false;
}
