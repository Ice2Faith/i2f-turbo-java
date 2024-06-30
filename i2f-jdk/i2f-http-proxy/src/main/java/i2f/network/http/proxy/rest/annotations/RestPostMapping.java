package i2f.network.http.proxy.rest.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ice2Faith
 * @date 2022/5/18 9:14
 * @desc
 */
@Target({
        ElementType.TYPE,
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface RestPostMapping {
    String value() default "";
}
