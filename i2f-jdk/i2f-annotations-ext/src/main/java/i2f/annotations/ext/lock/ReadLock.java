package i2f.annotations.ext.lock;

import i2f.annotations.core.doc.Comment;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "读锁",
        "指定为读锁，表示过程调用为共享读",
        "在使用分布式锁或者托管锁时，可以使用锁键",
        "使用value指定锁的键前缀",
        "clazz和method表示前缀是否需要携带类名和方法名",
        "锁键=clazz+method+value"
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
public @interface ReadLock {
    String value() default "";

    boolean clazz() default true;

    boolean method() default true;
}
