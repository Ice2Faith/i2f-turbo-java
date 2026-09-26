package i2f.ai.rest.mcp.official.consts;

/**
 * @author Ice2Faith
 * @date 2026/9/26 19:50
 * @desc
 */
public interface OfficialMcpConstants {
    String URL_PATH_MCP = "/mcp";
    String PROTOCOL_VERSION = "2024-11-05";
    String JSON_RPC_VERSION = "2.0";

    String METHOD_INITIALIZE = "initialize";
    String METHOD_TOOLS_LIST = "tools/list";
    String METHOD_TOOLS_CALL = "tools/call";

    String HEADER_MCP_SESSION_ID = "Mcp-Session-Id";

    // JSON-RPC 2.0 预定义错误码，MCP 官方协议沿用同一套编码
    // @see https://www.jsonrpc.org/specification#error_object
    int CODE_INVALID_REQUEST = -32600;
    int CODE_METHOD_NOT_FOUND = -32601;
    int CODE_INVALID_PARAMS = -32602;
    int CODE_INTERNAL_ERROR = -32603;
}
