package i2f.annotations.core.doc;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "一组备注",
        "提供了另一种可能，提供多个备注的方式，可使用defaultIndex指定默认值"
})
@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.LOCAL_VARIABLE,
        ElementType.METHOD,

        ElementType.TYPE_PARAMETER,
        ElementType.TYPE,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PACKAGE,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Comments {
    Comment[] value() default {};

    int defaultIndex() default 0;
}
