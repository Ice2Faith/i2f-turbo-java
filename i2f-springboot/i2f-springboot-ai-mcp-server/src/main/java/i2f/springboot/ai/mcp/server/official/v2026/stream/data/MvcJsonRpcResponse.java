package i2f.springboot.ai.mcp.server.official.v2026.stream.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Ice2Faith
 * @date 2026/9/28 14:51
 * @desc
 */
@Data
@NoArgsConstructor
public class MvcJsonRpcResponse<T> {
    protected HttpStatus httpStatus;
    protected T body;

    public MvcJsonRpcResponse(HttpStatus httpStatus, T body) {
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public static <T> MvcJsonRpcResponse<T> success(T body) {
        return new MvcJsonRpcResponse<>(HttpStatus.OK, body);
    }

    public static <T> MvcJsonRpcResponse<T> error(HttpStatus status, T body) {
        return new MvcJsonRpcResponse<>(status, body);
    }
}
