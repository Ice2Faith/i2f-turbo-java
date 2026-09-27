package i2f.springboot.ai.mcp.server.official.v2026.stream.consts;

import java.util.Collections;
import java.util.List;

/**
 * 官方 MCP 协议（protocolVersion: 2026-07-28 无状态版本）Streamable HTTP 传输的常量定义。
 * <p>
 * 区别于 2024-11-05（有状态、需要 initialize 握手与 Mcp-Session-Id）的版本，
 * 2026-07-28 版本的核心变化为：
 * <ul>
 *     <li>移除 initialize 握手与协议级会话，每个请求自行在 params._meta 中声明协议版本与客户端能力</li>
 *     <li>HTTP 请求必须携带 MCP-Protocol-Version / Mcp-Method / Mcp-Name 等镜像请求头，且须与请求体一致</li>
 *     <li>所有 result 新增必填的 resultType 字段（complete / input_required）</li>
 *     <li>tools/list 等列表结果新增 ttlMs 与 cacheScope 缓存字段</li>
 * </ul>
 * 常量集中定义于此包，不再分离模型层（参考 v2024 的
 * {@code i2f.ai.rest.mcp.official.v2024.consts.OfficialMcpConstants}）。
 *
 * @author Ice2Faith
 * @desc 2026-07-28 无状态 MCP 协议常量
 */
public interface OfficialMcpV2026Constants {
    String URL_BASE_PATH = "/v2026";
    String URL_PATH_MCP = "/mcp";

    String PROTOCOL_VERSION = "2026-07-28";
    /**
     * 当前服务端支持的协议版本列表，版本不匹配时通过 UnsupportedProtocolVersionError 的 data.supported 返回
     */
    List<String> SUPPORTED_PROTOCOL_VERSIONS = Collections.singletonList(PROTOCOL_VERSION);
    String JSON_RPC_VERSION = "2.0";

    /**
     * 无状态版本已移除 initialize，服务端 MUST 实现 server/discover 以宣告版本/能力/身份，
     * 核心业务端点仅保留 tools 相关方法
     */
    String METHOD_SERVER_DISCOVER = "server/discover";
    String METHOD_TOOLS_LIST = "tools/list";
    String METHOD_TOOLS_CALL = "tools/call";

    /**
     * 每个请求都必须携带的协议版本请求头，且取值须与 params._meta 中的协议版本一致
     */
    String HEADER_MCP_PROTOCOL_VERSION = "MCP-Protocol-Version";
    /**
     * 镜像 JSON-RPC method 字段的请求头
     */
    String HEADER_MCP_METHOD = "Mcp-Method";
    /**
     * 镜像 params.name（tools/call）或 params.uri（resources/read）字段的请求头
     */
    String HEADER_MCP_NAME = "Mcp-Name";
    /**
     * 无状态版本不再签发该请求头，若客户端携带则直接忽略
     */
    String HEADER_MCP_SESSION_ID = "Mcp-Session-Id";
    /**
     * 旧版本用于 SSE 断线续传的请求头，无状态版本不再支持，若客户端携带则直接忽略
     */
    String HEADER_LAST_EVENT_ID = "Last-Event-ID";

    String META_KEY_PREFIX = "io.modelcontextprotocol/";
    /**
     * 请求体 params._meta 中声明协议版本的 key
     */
    String META_PROTOCOL_VERSION = META_KEY_PREFIX + "protocolVersion";
    /**
     * 请求体 params._meta 中声明客户端能力的 key
     */
    String META_CLIENT_CAPABILITIES = META_KEY_PREFIX + "clientCapabilities";
    /**
     * 请求体 params._meta 中声明客户端身份的 key
     */
    String META_CLIENT_INFO = META_KEY_PREFIX + "clientInfo";
    /**
     * 响应体 result._meta 中声明服务端身份的 key
     */
    String META_SERVER_INFO = META_KEY_PREFIX + "serverInfo";
    /**
     * 请求体 params._meta 中声明日志级别的 key（本实现仅读取保留，不做日志级别控制）
     */
    String META_LOG_LEVEL = META_KEY_PREFIX + "logLevel";

    String RESULT_TYPE = "complete";
    String RESULT_TYPE_INPUT_REQUIRED = "input_required";

    String CACHE_SCOPE_PUBLIC = "public";
    String CACHE_SCOPE_PRIVATE = "private";

    // JSON-RPC 2.0 预定义错误码，MCP 官方协议沿用同一套编码
    // @see https://www.jsonrpc.org/specification#error_object
    int CODE_INVALID_REQUEST = -32600;
    int CODE_METHOD_NOT_FOUND = -32601;
    int CODE_INVALID_PARAMS = -32602;
    int CODE_INTERNAL_ERROR = -32603;

    // MCP 规范保留的协议级错误码（-32020 ~ -32099 由规范分配）
    // @see https://modelcontextprotocol.io/specification/2026-07-28/basic/index#error-codes
    int CODE_HEADER_MISMATCH = -32020;
    int CODE_MISSING_REQUIRED_CLIENT_CAPABILITY = -32021;
    int CODE_UNSUPPORTED_PROTOCOL_VERSION = -32022;
}
