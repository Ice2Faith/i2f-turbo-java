package i2f.jdbc.procedure.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 用于标注一个方法为一个函数，允许指定一个别名
 *
 * @author Ice2Faith
 * @date 2025/2/11 8:47
 */
@Target({
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface JdbcProcedureFunction {
    String value() default "";
}
