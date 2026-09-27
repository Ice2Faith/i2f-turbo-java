package i2f.springboot.ai.mcp.server.official.v2026.stream.springweb.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import i2f.context.std.IContext;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.mutator.BaseMutator;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.reflect.RichConverter;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.springboot.ai.mcp.server.official.v2026.stream.consts.OfficialMcpV2026Constants;
import i2f.springboot.ai.mcp.server.official.v2026.stream.data.V2026JsonRpcResponse;
import i2f.springboot.ai.mcp.server.official.v2026.stream.data.V2026ServerJsonRpcRequest;
import i2f.springboot.ai.mcp.server.official.v2026.stream.properties.OfficialMcpServerV2026Properties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 官方 MCP 协议（protocolVersion: 2026-07-28 无状态版本）Streamable HTTP 传输的 SpringMVC 服务端实现。
 * <p>
 * 与 {@code v2024} 的有状态实现（{@code SpringHttpStreamMcpController}：需 initialize 握手、
 * Mcp-Session-Id 会话）相对，本实现严格遵循 2026-07-28 无状态规范：
 * <ul>
 *     <li>不再有 initialize 握手，仅暴露 {@code tools/list} 与 {@code tools/call} 两个方法</li>
 *     <li>协议版本随每个请求在 {@code MCP-Protocol-Version} 头与 {@code params._meta} 中声明，二者须一致</li>
 *     <li>镜像请求头 {@code Mcp-Method} / {@code Mcp-Name} 须与请求体一致，否则以 HeaderMismatch(-32020) 拒绝</li>
 *     <li>不支持的协议版本以 UnsupportedProtocolVersion(-32022) 拒绝并回带 supported 列表</li>
 *     <li>所有 result 携带必填 resultType 与 _meta.serverInfo；tools/list 额外携带 ttlMs / cacheScope</li>
 * </ul>
 * 依然不依赖官方 SDK（要求 JDK17），直接桥接 {@link IContext} + {@link JsonSchemaAnnotationResolver}
 * + {@link ToolRawHelper} 复用本项目的工具定义解析与调用逻辑。工具执行失败/工具不存在按规范以
 * result.isError=true 承载，而非 JSON-RPC error 对象。
 *
 * @author Ice2Faith
 * @desc 2026-07-28 无状态 MCP Streamable HTTP 服务端
 */
@Data
@NoArgsConstructor
@Slf4j
@RestController
@RequestMapping(OfficialMcpV2026Constants.URL_BASE_PATH)
public class SpringHttpStreamMcpV2026Controller implements BaseMutator<SpringHttpStreamMcpV2026Controller> {

    /**
     * 不可安全以纯 ASCII 头部值表示时，客户端使用的 Base64 哨兵编码前后缀
     */
    private static final Pattern BASE64_SENTINEL = Pattern.compile("^=\\?base64\\?(.*)?=$", Pattern.DOTALL);

    protected OfficialMcpServerV2026Properties properties;

    protected IContext context;
    protected JsonSchemaAnnotationResolver annotationResolver = JsonSchemaAnnotationResolver.INSTANCE;
    protected IProxyInvocationHandler invocationHandler;
    protected IJsonSerializer jsonSerializer = new JacksonJsonSerializer(new ObjectMapper());

    @PostMapping(OfficialMcpV2026Constants.URL_PATH_MCP)
    public ResponseEntity<Map<String, Object>> handle(@RequestBody V2026ServerJsonRpcRequest payload,
                                                      HttpServletRequest request) {
        return mcp(payload, request);
    }

    public ResponseEntity<Map<String, Object>> mcp(V2026ServerJsonRpcRequest payload,
                                                   HttpServletRequest request) {
        try {
            // 1. 身份验证（Bearer Token，失败以 401 + JSON-RPC 错误信封返回）
            ResponseEntity<Map<String, Object>> authError = verifyAuth(payload, request);
            if (authError != null) {
                return authError;
            }

            String method = payload.getMethod();

            // 2. 请求头与请求体一致性校验（规范强制：处理请求体的服务端 MUST 校验）
            ResponseEntity<Map<String, Object>> headerError = validateHeaders(payload, request, method);
            if (headerError != null) {
                return headerError;
            }

            // 3. 信封校验
            if (method == null || method.isEmpty()) {
                return jsonError(payload.getId(), OfficialMcpV2026Constants.CODE_INVALID_REQUEST,
                        "missing jsonrpc method!", 200);
            }

            // 4. 方法路由：无状态版本保留 server/discover 与 tools/list、tools/call
            switch (method) {
                case OfficialMcpV2026Constants.METHOD_SERVER_DISCOVER:
                    return ok(discover(payload));
                case OfficialMcpV2026Constants.METHOD_TOOLS_LIST:
                    return ok(listTools(payload));
                case OfficialMcpV2026Constants.METHOD_TOOLS_CALL:
                    return ok(callTool(payload));
                default:
                    return jsonError(payload.getId(), OfficialMcpV2026Constants.CODE_METHOD_NOT_FOUND,
                            "method not found: " + method, 404);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return jsonError(payload.getId(), OfficialMcpV2026Constants.CODE_INTERNAL_ERROR,
                    e.getMessage(), 200);
        }
    }

    /**
     * Bearer Token 身份验证：仅在配置开启时执行。返回 null 表示通过。
     */
    protected ResponseEntity<Map<String, Object>> verifyAuth(V2026ServerJsonRpcRequest payload,
                                                             HttpServletRequest request) {
        OfficialMcpServerV2026Properties.BearerTokenOptions token = properties.getBearerToken();
        if (token == null || !token.isEnable()) {
            return null;
        }
        List<String> allowTokens = token.getAllowTokens();
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer")) {
            return jsonError(payload.getId(), OfficialMcpV2026Constants.CODE_INVALID_REQUEST,
                    "auth not passed!", 401);
        }
        String[] arr = header.split("\\s+", 2);
        if (arr.length != 2 || allowTokens == null || allowTokens.isEmpty() || !allowTokens.contains(arr[1])) {
            return jsonError(payload.getId(), OfficialMcpV2026Constants.CODE_INVALID_REQUEST,
                    "auth not passed!", 401);
        }
        return null;
    }

    /**
     * 校验 2026 无状态版本强制的镜像请求头与请求体一致性。返回 null 表示通过。
     * <p>
     * 校验项：
     * <ol>
     *     <li>MCP-Protocol-Version 必须存在，且与 params._meta 中声明的版本一致</li>
     *     <li>请求声明的协议版本必须被服务端支持，否则 UnsupportedProtocolVersion(-32022)</li>
     *     <li>Mcp-Method 必须存在且等于请求体 method</li>
     *     <li>tools/call 时 Mcp-Name 必须存在且等于 params.name（区分大小写，头部为镜像值）</li>
     * </ol>
     */
    protected ResponseEntity<Map<String, Object>> validateHeaders(V2026ServerJsonRpcRequest payload,
                                                                  HttpServletRequest request,
                                                                  String method) {
        String id = payload.getId();

        // MCP-Protocol-Version
        String versionHeader = trimToNull(request.getHeader(OfficialMcpV2026Constants.HEADER_MCP_PROTOCOL_VERSION));
        String metaVersion = trimToNull(payload.metaProtocolVersion());
        if (versionHeader == null) {
            return headerMismatch(id, "missing header: " + OfficialMcpV2026Constants.HEADER_MCP_PROTOCOL_VERSION);
        }
        if (metaVersion != null && !metaVersion.equals(versionHeader)) {
            return headerMismatch(id, OfficialMcpV2026Constants.HEADER_MCP_PROTOCOL_VERSION
                    + " header value '" + versionHeader + "' does not match _meta value '" + metaVersion + "'");
        }
        if (!OfficialMcpV2026Constants.SUPPORTED_PROTOCOL_VERSIONS.contains(versionHeader)) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("supported", OfficialMcpV2026Constants.SUPPORTED_PROTOCOL_VERSIONS);
            data.put("requested", versionHeader);
            return jsonError(id, OfficialMcpV2026Constants.CODE_UNSUPPORTED_PROTOCOL_VERSION,
                    "Unsupported protocol version", 400, data);
        }

        // Mcp-Method
        String methodHeader = trimToNull(request.getHeader(OfficialMcpV2026Constants.HEADER_MCP_METHOD));
        if (methodHeader == null) {
            return headerMismatch(id, "missing header: " + OfficialMcpV2026Constants.HEADER_MCP_METHOD);
        }
        if (method != null && !method.isEmpty() && !methodHeader.equals(method)) {
            return headerMismatch(id, OfficialMcpV2026Constants.HEADER_MCP_METHOD
                    + " header value '" + methodHeader + "' does not match body method '" + method + "'");
        }

        // Mcp-Name（仅 tools/call 需要）
        if (OfficialMcpV2026Constants.METHOD_TOOLS_CALL.equals(method)) {
            String nameHeader = decodeHeaderValue(trimToNull(request.getHeader(OfficialMcpV2026Constants.HEADER_MCP_NAME)));
            String bodyName = trimToNull(stringOf(param(payload, "name")));
            if (nameHeader == null) {
                return headerMismatch(id, "missing header: " + OfficialMcpV2026Constants.HEADER_MCP_NAME);
            }
            if (bodyName != null && !nameHeader.equals(bodyName)) {
                return headerMismatch(id, OfficialMcpV2026Constants.HEADER_MCP_NAME
                        + " header value '" + nameHeader + "' does not match body name '" + bodyName + "'");
            }
        }
        return null;
    }

    /**
     * server/discover：无状态版本服务端 MUST 实现的发现 RPC，宣告支持的协议版本、能力与身份。
     * <p>
     * 返回 DiscoverResult：resultType + supportedVersions + capabilities（本服务端仅 tools）
     * + _meta.serverInfo；instructions、ttlMs、cacheScope 为可选字段，仅在配置提供时输出。
     */
    protected V2026JsonRpcResponse discover(V2026ServerJsonRpcRequest request) {
        Map<String, Object> tools = new LinkedHashMap<>();
        tools.put("listChanged", false);
        Map<String, Object> capabilities = new LinkedHashMap<>();
        capabilities.put("tools", tools);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("resultType", OfficialMcpV2026Constants.RESULT_TYPE);
        result.put("supportedVersions", OfficialMcpV2026Constants.SUPPORTED_PROTOCOL_VERSIONS);
        result.put("capabilities", capabilities);
        result.put("_meta", serverInfoMeta());

        String instructions = properties.getInstructions();
        if (instructions != null && !instructions.isEmpty()) {
            result.put("instructions", instructions);
        }
        OfficialMcpServerV2026Properties.ToolListOptions listOptions = properties.getToolList();
        result.put("ttlMs", listOptions.getTtlMs());
        result.put("cacheScope", listOptions.getCacheScope());

        return V2026JsonRpcResponse.success(request.getId(), result);
    }

    protected V2026JsonRpcResponse listTools(V2026ServerJsonRpcRequest request) {
        Map<String, ToolRawDefinition> definitionMap = ToolRawHelper.parseTools(annotationResolver, context);

        List<Map<String, Object>> tools = new ArrayList<>();
        for (ToolRawDefinition definition : definitionMap.values()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", definition.getName());
            item.put("description", definition.getDescription());
            item.put("inputSchema", definition.getJsonSchema().getParameters());
            tools.add(item);
        }

        OfficialMcpServerV2026Properties.ToolListOptions listOptions = properties.getToolList();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("resultType", OfficialMcpV2026Constants.RESULT_TYPE);
        result.put("tools", tools);
        result.put("ttlMs", listOptions.getTtlMs());
        result.put("cacheScope", listOptions.getCacheScope());
        result.put("_meta", serverInfoMeta());

        return V2026JsonRpcResponse.success(request.getId(), result);
    }

    protected V2026JsonRpcResponse callTool(V2026ServerJsonRpcRequest request) {
        Map<String, Object> params = request.getParams();
        if (params == null) {
            return V2026JsonRpcResponse.error(request.getId(),
                    OfficialMcpV2026Constants.CODE_INVALID_PARAMS, "missing tools/call params!");
        }
        String toolName = trimToNull(stringOf(params.get("name")));
        if (toolName == null) {
            return V2026JsonRpcResponse.error(request.getId(),
                    OfficialMcpV2026Constants.CODE_INVALID_PARAMS, "missing tools/call params.name!");
        }

        Map<String, Object> arguments = RichConverter.convert(params.get("arguments"), LinkedHashMap.class);

        Map<String, ToolRawDefinition> definitionMap = ToolRawHelper.parseTools(annotationResolver, context);
        ToolRawDefinition rawTool = definitionMap.get(toolName);
        if (rawTool == null) {
            // 工具不存在属于业务层结果，按规范以 isError=true 承载而非 JSON-RPC error
            return V2026JsonRpcResponse.success(request.getId(),
                    toolCallResult("un-support tool call request, tool not found: " + toolName, true));
        }

        try {
            Object ret = ToolRawHelper.invokeTool(rawTool, arguments, invocationHandler);
            return V2026JsonRpcResponse.success(request.getId(), toolCallResult(toText(ret), false));
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return V2026JsonRpcResponse.success(request.getId(), toolCallResult(e.getMessage(), true));
        }
    }

    protected Map<String, Object> toolCallResult(String text, boolean isError) {
        Map<String, Object> textContent = new LinkedHashMap<>();
        textContent.put("type", "text");
        textContent.put("text", text);

        List<Map<String, Object>> content = new ArrayList<>();
        content.add(textContent);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("resultType", OfficialMcpV2026Constants.RESULT_TYPE);
        result.put("content", content);
        result.put("isError", isError);
        result.put("_meta", serverInfoMeta());
        return result;
    }

    /**
     * 构造 result._meta，回带服务端身份（io.modelcontextprotocol/serverInfo）。
     */
    protected Map<String, Object> serverInfoMeta() {
        Map<String, Object> serverInfo = new LinkedHashMap<>();
        serverInfo.put("name", properties.getServerName());
        serverInfo.put("version", properties.getServerVersion());

        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put(OfficialMcpV2026Constants.META_SERVER_INFO, serverInfo);
        return meta;
    }

    protected String toText(Object ret) {
        if (ret == null) {
            return "";
        }
        if (ret instanceof String) {
            return (String) ret;
        }
        try {
            return jsonSerializer.serialize(ret);
        } catch (Exception e) {
            return String.valueOf(ret);
        }
    }

    // ------------------------------------------------------------------
    // 响应封装与工具方法
    // ------------------------------------------------------------------

    protected ResponseEntity<Map<String, Object>> ok(V2026JsonRpcResponse response) {
        return ResponseEntity.ok(response.toMap());
    }

    protected ResponseEntity<Map<String, Object>> headerMismatch(String id, String message) {
        return jsonError(id, OfficialMcpV2026Constants.CODE_HEADER_MISMATCH,
                "Header mismatch: " + message, 400);
    }

    protected ResponseEntity<Map<String, Object>> jsonError(String id, int code, String message, int status) {
        return jsonError(id, code, message, status, null);
    }

    protected ResponseEntity<Map<String, Object>> jsonError(String id, int code, String message, int status,
                                                            Map<String, Object> data) {
        return ResponseEntity.status(status)
                .body(V2026JsonRpcResponse.error(id, code, message, data).toMap());
    }

    protected Object param(V2026ServerJsonRpcRequest payload, String key) {
        Map<String, Object> params = payload.getParams();
        return params == null ? null : params.get(key);
    }

    protected String decodeHeaderValue(String value) {
        if (value == null) {
            return null;
        }
        java.util.regex.Matcher matcher = BASE64_SENTINEL.matcher(value);
        if (matcher.matches()) {
            try {
                byte[] decoded = Base64.getDecoder().decode(matcher.group(1));
                return new String(decoded, StandardCharsets.UTF_8);
            } catch (Exception e) {
                // 非法 Base64，原样返回交由后续比较判定
                return value;
            }
        }
        return value;
    }

    protected String stringOf(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    protected String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
