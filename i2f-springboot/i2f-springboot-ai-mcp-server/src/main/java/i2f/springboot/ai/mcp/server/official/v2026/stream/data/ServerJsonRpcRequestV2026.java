package i2f.springboot.ai.mcp.server.official.v2026.stream.data;

import i2f.ai.rest.mcp.official.v2026.consts.OfficialMcpConstantsV2026;
import i2f.springboot.ai.mcp.server.official.v2024.stream.data.ServerJsonRpcRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 官方 MCP 2026-07-28 无状态版本（Streamable HTTP）JSON-RPC 2.0 请求体。
 * <p>
 * 与 2024 有状态版本不同，本版本不再有 initialize 握手：协议版本、客户端身份与能力都随
 * 每个请求携带于 {@code params._meta} 中，因此本类额外提供 {@link #meta()} 便捷读取。
 * 为遵循 request.md「全部实现自包含于此包」的要求，此处独立定义而不再继承其他模块的 DTO。
 *
 * @author Ice2Faith
 * @desc 2026-07-28 无状态 MCP JSON-RPC 请求体
 */
@Data
@NoArgsConstructor
public class ServerJsonRpcRequestV2026 extends ServerJsonRpcRequest {

    /**
     * 读取 params._meta，缺失时返回空 Map（不返回 null，便于直接取值）。
     *
     * @return params._meta 内容
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> meta() {
        if (params == null) {
            return new LinkedHashMap<>();
        }
        Object meta = params.get("_meta");
        if (meta instanceof Map) {
            return (Map<String, Object>) meta;
        }
        return new LinkedHashMap<>();
    }

    /**
     * 读取 params._meta 中声明的协议版本（io.modelcontextprotocol/protocolVersion）。
     *
     * @return 协议版本，未声明时返回 null
     */
    public String metaProtocolVersion() {
        Object version = meta().get(OfficialMcpConstantsV2026.META_PROTOCOL_VERSION);
        return version == null ? null : String.valueOf(version);
    }
}
