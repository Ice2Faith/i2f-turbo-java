package i2f.annotations.core.check;

import i2f.annotations.core.doc.Comment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ice2Faith
 * @date 2025/7/23 16:52
 */
@Comment({
        "进行验证操作",
        "检查函数的入参，返回值要符合要求",
        "tags表示了对于那些字段/参数开启校验，不指定则对所有都开启"
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
public @interface Validate {
    String[] tags() default {};
}
