package i2f.extension.easyexcel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ice2Faith
 * @date 2024/2/2 10:10
 * @desc
 */
@Target({
        ElementType.FIELD,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelTag {
    String[] value() default {};
}
