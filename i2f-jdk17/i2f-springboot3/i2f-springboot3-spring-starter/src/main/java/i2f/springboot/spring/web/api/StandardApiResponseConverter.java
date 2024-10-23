package i2f.springboot.spring.web.api;

/**
 * @author Ice2Faith
 * @date 2024/10/23 19:02
 * @desc
 */
public interface StandardApiResponseConverter<R> {
    R convert(Object obj);
}
