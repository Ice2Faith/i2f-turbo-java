package i2f.net.http.rest.data;

import i2f.mutator.BaseMutator;
import i2f.net.http.data.HttpHeaders;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/6/24 19:47
 * @desc
 */
@Data
@NoArgsConstructor
public class RestHttpResponse<T> implements BaseMutator<RestHttpResponse<T>> {
    /**
     * HTTP响应码
     */
    protected int statusCode;
    /**
     * HTTP状态描述
     */
    protected String statusMessage;
    /**
     * 响应头
     */
    protected HttpHeaders headers;
    /**
     * 响应体
     */
    protected T body;

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

}
