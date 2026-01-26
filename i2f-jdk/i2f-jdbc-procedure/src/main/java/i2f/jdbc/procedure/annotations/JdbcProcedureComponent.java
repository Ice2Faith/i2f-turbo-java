package i2f.jdbc.procedure.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 指定为一个 JdbcProcedure 的组件，动态扫描时用于筛选 bean 的范围
 *
 * @author Ice2Faith
 * @date 2025/2/11 8:47
 */
@Target({
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface JdbcProcedureComponent {

}
