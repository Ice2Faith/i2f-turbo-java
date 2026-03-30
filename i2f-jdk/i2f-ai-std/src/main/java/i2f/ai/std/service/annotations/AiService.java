package i2f.ai.std.service.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ice2Faith
 * @date 2026/3/30 8:51
 * @desc
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AiService {
    boolean enable() default true;

    AiAgents[] agent() default {};

    AiTools[] tools() default {};

    AiSkills[] skills() default {};
}
