package i2f.ai.std.mcp.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author Ice2Faith
 * @date 2026/9/28 19:51
 * @desc
 */
@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface McpServerExpose {
    boolean value() default true;
}
