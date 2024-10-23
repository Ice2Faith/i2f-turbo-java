package i2f.springboot.spring.web.api;

/**
 * @author Ice2Faith
 * @date 2024/10/23 19:03
 * @desc
 */
public interface StandardApiExceptionConverter<R> {
    R convert(Throwable throwable);
}
