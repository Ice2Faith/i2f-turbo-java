package i2f.jdbc.proxy.annotations;


import i2f.bindsql.BindSql;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Target({
        ElementType.METHOD,
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlScript {
    String value();

    BindSql.Type type() default BindSql.Type.UNSET;
}
