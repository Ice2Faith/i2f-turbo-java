package i2f.springboot.ai.mcp.server.stream.auth;

import i2f.net.http.data.HttpHeaders;
import i2f.springboot.ai.mcp.server.stream.data.JsonRpcRequest;

/**
 * @author Ice2Faith
 * @date 2026/9/26 19:58
 * @desc
 */
public interface StreamMcpServerAuthFilter {
    boolean verify(JsonRpcRequest payload, HttpHeaders headers);
}
