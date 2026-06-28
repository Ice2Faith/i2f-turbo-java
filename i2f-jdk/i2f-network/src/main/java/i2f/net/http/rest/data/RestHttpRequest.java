package i2f.net.http.rest.data;

import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.data.HttpHeaders;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Ice2Faith
 * @date 2026/6/24 19:45
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class RestHttpRequest {
    /**
     * 请求URL
     */
    protected String url;
    /**
     * 请求方式
     */
    protected String method = HttpMethodConstants.GET;
    /**
     * URL参数
     */
    protected Object params;
    /**
     * 请求头
     */
    protected HttpHeaders headers;
    /**
     * 请求体
     */
    protected Object body;

}
