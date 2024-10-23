package i2f.springboot.spring.web.response;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Target({
        ElementType.METHOD,

        ElementType.TYPE,
        ElementType.ANNOTATION_TYPE,
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StandardResp {
    boolean value() default true;
}
