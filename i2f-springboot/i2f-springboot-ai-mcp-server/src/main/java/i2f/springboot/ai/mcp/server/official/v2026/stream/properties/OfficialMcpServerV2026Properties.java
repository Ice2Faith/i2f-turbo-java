package i2f.springboot.ai.mcp.server.official.v2026.stream.properties;

import i2f.ai.rest.mcp.official.v2026.consts.OfficialMcpConstantsV2026;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 官方 MCP 2026-07-28 无状态版本 Streamable HTTP 服务端配置。
 * <p>
 * 相比 2024 版本，新增 {@link ToolListOptions} 用于配置 tools/list 结果的缓存语义字段
 * （ttlMs / cacheScope），这是无状态版本 CacheableResult 的强制要求。
 *
 * @author Ice2Faith
 * @desc 2026-07-28 无状态 MCP 服务端配置
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.ai.mcp.server.official.v2026.stream")
public class OfficialMcpServerV2026Properties {
    protected String serverName = "i2f-mcp-server";
    protected String serverVersion = "1.0.0";

    /**
     * server/discover 结果中可选的自然语言使用说明（instructions），为空时不输出。
     */
    protected String instructions;

    protected BearerTokenOptions bearerToken = new BearerTokenOptions();

    protected ToolListOptions toolList = new ToolListOptions();

    @Data
    @NoArgsConstructor
    public static class BearerTokenOptions {
        protected boolean enable = true;
        protected List<String> allowTokens;
    }

    /**
     * tools/list 结果的缓存语义（对应无状态版本 CacheableResult 的 ttlMs 与 cacheScope 字段）。
     */
    @Data
    @NoArgsConstructor
    public static class ToolListOptions {
        /**
         * 工具列表结果的有效期（毫秒），为客户端提供缓存与减少轮询的新鲜度提示。
         */
        protected long ttlMs = 300000L;
        /**
         * 缓存作用域：public 允许共享中间节点缓存，private 仅允许私有缓存。
         */
        protected String cacheScope = OfficialMcpConstantsV2026.CACHE_SCOPE_PRIVATE;
    }
}
