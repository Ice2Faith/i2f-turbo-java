package i2f.springboot.ai.mcp.server.stream.auth.impl;

import i2f.net.http.data.HttpHeaders;
import i2f.springboot.ai.mcp.server.stream.auth.StreamMcpServerAuthFilter;
import i2f.springboot.ai.mcp.server.stream.data.JsonRpcRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/9/26 20:00
 * @desc
 */
@Data
@NoArgsConstructor
public class StaticStreamMcpServerAuthFilter implements StreamMcpServerAuthFilter {
    protected boolean enable = true;
    protected Set<String> allowBearerTokens = new HashSet<>();

    @Override
    public boolean verify(JsonRpcRequest payload, HttpHeaders headers) {
        if (!enable) {
            return true;
        }
        String header = headers.getFirstHeader("Authorization");
        if (header == null || !header.startsWith("Bearer")) {
            return false;
        }
        String[] arr = header.split("\\s+", 2);
        if (arr.length != 2) {
            return false;
        }
        String token = arr[1];
        if (allowBearerTokens == null || allowBearerTokens.isEmpty()) {
            return false;
        }
        if (!allowBearerTokens.contains(token)) {
            return false;
        }
        return true;
    }
}
