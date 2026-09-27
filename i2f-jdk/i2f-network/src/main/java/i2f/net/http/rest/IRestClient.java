package i2f.net.http.rest;

import i2f.net.http.rest.data.RestHttpRequest;
import i2f.net.http.rest.data.RestHttpResponse;
import i2f.typeof.token.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author Ice2Faith
 * @date 2026/6/24 19:45
 * @desc
 */
public interface IRestClient {

    <T> RestHttpResponse<T> rest(RestHttpRequest request, Class<T> responseType) throws IOException;

    <T> RestHttpResponse<T> rest(RestHttpRequest request, Type responseType) throws IOException;

    default <T> RestHttpResponse<T> rest(RestHttpRequest request, TypeToken<T> responseType) throws IOException {
        return rest(request, responseType.type());
    }

}
