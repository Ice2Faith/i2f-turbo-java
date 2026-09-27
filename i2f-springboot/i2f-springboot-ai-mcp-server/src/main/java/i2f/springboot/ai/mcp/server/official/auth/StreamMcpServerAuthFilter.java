package i2f.springboot.ai.mcp.server.official.auth;

import i2f.net.http.data.HttpHeaders;
import i2f.springboot.ai.mcp.server.official.v2024.stream.data.ServerJsonRpcRequest;

/**
 * @author Ice2Faith
 * @date 2026/9/26 19:58
 * @desc
 */
public interface StreamMcpServerAuthFilter {
    boolean verify(ServerJsonRpcRequest payload, HttpHeaders headers);
}
