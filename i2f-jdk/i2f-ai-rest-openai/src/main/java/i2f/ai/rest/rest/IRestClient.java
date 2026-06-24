package i2f.ai.rest.rest;

import i2f.ai.rest.rest.data.RestHttpRequest;
import i2f.ai.rest.rest.data.RestHttpResponse;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2026/6/24 19:45
 * @desc
 */
public interface IRestClient {
    <T> RestHttpResponse<T> rest(RestHttpRequest request, Class<T> responseType) throws IOException;
}
