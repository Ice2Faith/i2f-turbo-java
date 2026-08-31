package i2f.ai.std.tool.intent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author Ice2Faith
 * @date 2026/8/31 15:22
 * @desc 用于标注工具的意图，用于在支持意图识别的场景中进行工具过滤使用
 */
@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ToolIntent {
    ToolIntents[] value() default {};

    ToolIntentItem[] items() default {};

    Class<? extends IToolIntent>[] from() default {};
}
