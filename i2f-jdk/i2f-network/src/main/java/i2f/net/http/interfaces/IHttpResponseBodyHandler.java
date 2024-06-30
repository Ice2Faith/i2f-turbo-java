package i2f.net.http.interfaces;


/**
 * @author Ice2Faith
 * @date 2022/3/24 14:31
 * @desc
 */
public interface IHttpResponseBodyHandler<T> {
    T readBody(Object... args);
}
