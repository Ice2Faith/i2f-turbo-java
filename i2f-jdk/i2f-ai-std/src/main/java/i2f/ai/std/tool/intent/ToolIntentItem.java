package i2f.ai.std.tool.intent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author Ice2Faith
 * @date 2026/8/31 15:51
 * @desc
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ToolIntentItem {
    String value() default "";
    String description() default "";
}
