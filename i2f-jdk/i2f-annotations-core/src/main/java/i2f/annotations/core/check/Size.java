package i2f.annotations.core.check;

import i2f.annotations.core.doc.Comment;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "大小检查",
        "检查值是否满足在指定的大小范围内，针对集合/数组/字符串",
        "值在min-max之间，这是闭区间，也就是包含最大最小值"
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
public @interface Size {
    String min();

    String max();

    String message() default "";
}
