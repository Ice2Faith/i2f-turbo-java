package i2f.extension.antlr4.script.funic.lang.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author Ice2Faith
 * @date 2026/4/23 19:18
 * @desc
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FunicFunction {

    String[] value() default {};
}
