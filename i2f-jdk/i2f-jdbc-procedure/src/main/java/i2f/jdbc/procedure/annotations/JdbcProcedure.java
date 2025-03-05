package i2f.jdbc.procedure.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 指定对应的过程的名称
 * @author Ice2Faith
 * @date 2025/2/11 8:47
 */
@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface JdbcProcedure {
    /**
     * 指定对应的过程的名称
     */
    String value();
    // 指定对应的参数，写法和XML中对于procedure的参数定义一致
    // 例如：{"IN_SUM_MONTH.int","O_FLAG.int.out"}
    String[] arguments() default {};
}
