package i2f.ai.rest.rest.data;

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
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    /**
     * 请求URL
     */
    protected String url;
    /**
     * 请求方式
     */
    protected String method;
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
