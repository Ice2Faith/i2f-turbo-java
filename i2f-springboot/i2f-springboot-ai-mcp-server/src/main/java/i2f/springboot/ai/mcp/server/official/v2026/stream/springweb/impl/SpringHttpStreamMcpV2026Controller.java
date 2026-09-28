package i2f.springboot.ai.mcp.server.official.v2026.stream.springweb.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.rest.mcp.official.v2024.consts.OfficialMcpConstants;
import i2f.ai.rest.mcp.official.v2024.data.result.JsonRpcToolCallParam;
import i2f.ai.rest.mcp.official.v2024.data.result.JsonRpcToolListItem;
import i2f.ai.rest.mcp.official.v2026.consts.OfficialMcpConstantsV2026;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import i2f.context.std.IContext;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.mutator.BaseMutator;
import i2f.net.http.data.HttpHeaders;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.reflect.RichConverter;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.springboot.ai.mcp.server.official.auth.StreamMcpServerAuthFilter;
import i2f.ai.rest.mcp.official.v2026.data.JsonRpcResponseV2026;
import i2f.springboot.ai.mcp.server.official.v2026.stream.data.MvcJsonRpcResponse;
import i2f.springboot.ai.mcp.server.official.v2026.stream.data.ServerJsonRpcRequestV2026;
import i2f.ai.rest.mcp.official.v2026.data.result.JsonRpcServerDiscoverResult;
import i2f.ai.rest.mcp.official.v2026.data.result.JsonRpcServerInfo;
import i2f.ai.rest.mcp.official.v2026.data.result.JsonRpcToolCallResultV2026;
import i2f.ai.rest.mcp.official.v2026.data.result.JsonRpcToolListResultV2026;
import i2f.springboot.ai.mcp.server.official.v2026.stream.properties.OfficialMcpServerV2026Properties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
@RequestMapping(OfficialMcpConstantsV2026.URL_BASE_PATH)
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

    protected StreamMcpServerAuthFilter streamMcpServerAuthFilter;

    @PostMapping(OfficialMcpConstantsV2026.URL_PATH_MCP)
    public ResponseEntity<Map<String, Object>> handle(@RequestBody ServerJsonRpcRequestV2026 payload,
                                                      HttpServletRequest request) {
        // 统一进行rpc字段转换
        MvcJsonRpcResponse<JsonRpcResponseV2026<?>> resp = mcp(payload, request);
        Map<String, Object> map = new HashMap<>();
        JsonRpcResponseV2026<?> body = resp.getBody();
        if (body != null) {
            map = body.toMap();
        }
        return ResponseEntity.status(resp.getHttpStatus())
                .body(map);
    }

    public MvcJsonRpcResponse<JsonRpcResponseV2026<?>> mcp(ServerJsonRpcRequestV2026 payload,
                                                           HttpServletRequest request) {
        try {
            // 如果配置了身份验证器，则进行验证身份
            if (streamMcpServerAuthFilter != null) {
                HttpHeaders filterHeaders = HttpHeaders.create();
                Enumeration<String> names = request.getHeaderNames();
                while (names.hasMoreElements()) {
                    String name = names.nextElement();
                    Enumeration<String> headers = request.getHeaders(name);
                    while (headers.hasMoreElements()) {
                        String value = headers.nextElement();
                        filterHeaders.add(name, value);
                    }
                }
                if (!streamMcpServerAuthFilter.verify(payload, filterHeaders)) {
                    return MvcJsonRpcResponse.error(HttpStatus.UNAUTHORIZED, JsonRpcResponseV2026.error(payload.getId(), OfficialMcpConstants.CODE_INVALID_REQUEST, "auth not passed!"));
                }
            }

            String method = payload.getMethod();

            // 2. 请求头与请求体一致性校验（规范强制：处理请求体的服务端 MUST 校验）
            MvcJsonRpcResponse<JsonRpcResponseV2026<?>> headerError = validateHeaders(payload, request, method);
            if (headerError != null) {
                return headerError;
            }

            // 3. 信封校验
            if (method == null || method.isEmpty()) {
                return MvcJsonRpcResponse.success(JsonRpcResponseV2026.error(payload.getId(), OfficialMcpConstantsV2026.CODE_INVALID_REQUEST,
                        "missing jsonrpc method!"));
            }

            // 4. 方法路由：无状态版本保留 server/discover 与 tools/list、tools/call
            switch (method) {
                case OfficialMcpConstantsV2026.METHOD_SERVER_DISCOVER:
                    return MvcJsonRpcResponse.success(discover(payload));
                case OfficialMcpConstantsV2026.METHOD_TOOLS_LIST:
                    return MvcJsonRpcResponse.success(listTools(payload));
                case OfficialMcpConstantsV2026.METHOD_TOOLS_CALL:
                    return MvcJsonRpcResponse.success(callTool(payload));
                default:
                    return MvcJsonRpcResponse.error(HttpStatus.NOT_FOUND, JsonRpcResponseV2026.error(payload.getId(), OfficialMcpConstantsV2026.CODE_METHOD_NOT_FOUND,
                            "method not found: " + method));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return MvcJsonRpcResponse.success(JsonRpcResponseV2026.error(payload.getId(), OfficialMcpConstantsV2026.CODE_INTERNAL_ERROR,
                    "internal error, " + e.getMessage()));
        }
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
    protected MvcJsonRpcResponse<JsonRpcResponseV2026<?>> validateHeaders(ServerJsonRpcRequestV2026 payload,
                                                                          HttpServletRequest request,
                                                                          String method) {
        String id = payload.getId();

        // MCP-Protocol-Version
        String versionHeader = trimToNull(request.getHeader(OfficialMcpConstantsV2026.HEADER_MCP_PROTOCOL_VERSION));
        String metaVersion = trimToNull(payload.metaProtocolVersion());
        if (versionHeader == null) {
            return headerMismatch(id, "missing header: " + OfficialMcpConstantsV2026.HEADER_MCP_PROTOCOL_VERSION);
        }
        if (metaVersion != null && !metaVersion.equals(versionHeader)) {
            return headerMismatch(id, OfficialMcpConstantsV2026.HEADER_MCP_PROTOCOL_VERSION
                    + " header value '" + versionHeader + "' does not match _meta value '" + metaVersion + "'");
        }
        if (!OfficialMcpConstantsV2026.SUPPORTED_PROTOCOL_VERSIONS.contains(versionHeader)) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("supported", OfficialMcpConstantsV2026.SUPPORTED_PROTOCOL_VERSIONS);
            data.put("requested", versionHeader);
            return MvcJsonRpcResponse.error(HttpStatus.BAD_REQUEST, JsonRpcResponseV2026.error(id, OfficialMcpConstantsV2026.CODE_UNSUPPORTED_PROTOCOL_VERSION,
                    "Unsupported protocol version", data));
        }

        // Mcp-Method
        String methodHeader = trimToNull(request.getHeader(OfficialMcpConstantsV2026.HEADER_MCP_METHOD));
        if (methodHeader == null) {
            return headerMismatch(id, "missing header: " + OfficialMcpConstantsV2026.HEADER_MCP_METHOD);
        }
        if (method != null && !method.isEmpty() && !methodHeader.equals(method)) {
            return headerMismatch(id, OfficialMcpConstantsV2026.HEADER_MCP_METHOD
                    + " header value '" + methodHeader + "' does not match body method '" + method + "'");
        }

        // Mcp-Name（仅 tools/call 需要）
        if (OfficialMcpConstantsV2026.METHOD_TOOLS_CALL.equals(method)) {
            String nameHeader = decodeHeaderValue(trimToNull(request.getHeader(OfficialMcpConstantsV2026.HEADER_MCP_NAME)));
            if (nameHeader == null) {
                return headerMismatch(id, "missing header: " + OfficialMcpConstantsV2026.HEADER_MCP_NAME);
            }

            Map<String, Object> map = payload.getParams();
            JsonRpcToolCallParam params = RichConverter.convert(map, JsonRpcToolCallParam.class);

            String bodyName = params.getName();
            if (bodyName != null && !nameHeader.equals(bodyName)) {
                return headerMismatch(id, OfficialMcpConstantsV2026.HEADER_MCP_NAME
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
    protected JsonRpcResponseV2026<?> discover(ServerJsonRpcRequestV2026 request) {
        Map<String, Object> tools = new LinkedHashMap<>();
        tools.put("listChanged", false);
        Map<String, Object> capabilities = new LinkedHashMap<>();
        capabilities.put("tools", tools);

        JsonRpcServerDiscoverResult result = new JsonRpcServerDiscoverResult();
        result.setResultType(OfficialMcpConstantsV2026.RESULT_TYPE_COMPLETE);
        result.setSupportedVersions(OfficialMcpConstantsV2026.SUPPORTED_PROTOCOL_VERSIONS);
        result.setCapabilities(capabilities);
        result.set_meta(serverInfoMeta());

        String instructions = properties.getInstructions();
        if (instructions != null && !instructions.isEmpty()) {
            result.setInstructions(instructions);
        }
        OfficialMcpServerV2026Properties.ToolListOptions listOptions = properties.getToolList();
        result.setTtlMs(listOptions.getTtlMs());
        result.setCacheScope(listOptions.getCacheScope());

        return JsonRpcResponseV2026.success(request.getId(), result.toMap());
    }

    protected JsonRpcResponseV2026<?> listTools(ServerJsonRpcRequestV2026 request) {
        Map<String, ToolRawDefinition> definitionMap = ToolRawHelper.parseTools(annotationResolver, context);

        List<JsonRpcToolListItem> tools = new ArrayList<>();
        for (ToolRawDefinition definition : definitionMap.values()) {
            JsonRpcToolListItem item = new JsonRpcToolListItem();
            item.setName(definition.getName());
            item.setDescription(definition.getDescription());
            item.setInputSchema(definition.getJsonSchema().getParameters());
            tools.add(item);
        }

        OfficialMcpServerV2026Properties.ToolListOptions listOptions = properties.getToolList();
        JsonRpcToolListResultV2026 result = new JsonRpcToolListResultV2026();
        result.setResultType(OfficialMcpConstantsV2026.RESULT_TYPE_COMPLETE);
        result.setTools(tools);
        result.setTtlMs(listOptions.getTtlMs());
        result.setCacheScope(listOptions.getCacheScope());
        result.set_meta(serverInfoMeta());

        return JsonRpcResponseV2026.success(request.getId(), result.toMap());
    }

    protected JsonRpcResponseV2026<?> callTool(ServerJsonRpcRequestV2026 request) {
        Map<String, Object> map = request.getParams();
        if (map == null) {
            return JsonRpcResponseV2026.error(request.getId(), OfficialMcpConstantsV2026.CODE_INVALID_PARAMS, "missing tools/call params!");
        }
        JsonRpcToolCallParam params = RichConverter.convert(map, JsonRpcToolCallParam.class);

        String toolName = params.getName();
        if (toolName == null || toolName.isEmpty()) {
            return JsonRpcResponseV2026.error(request.getId(), OfficialMcpConstantsV2026.CODE_INVALID_PARAMS, "missing tools/call params.name!");
        }

        Map<String, Object> arguments = params.getArguments();

        Map<String, ToolRawDefinition> definitionMap = ToolRawHelper.parseTools(annotationResolver, context);
        ToolRawDefinition rawTool = definitionMap.get(toolName);
        if (rawTool == null) {
            // 工具不存在属于业务层结果，按规范以 isError=true 承载而非 JSON-RPC error
            return JsonRpcResponseV2026.success(request.getId(), JsonRpcToolCallResultV2026.error("un-support tool call request, tool not found: " + toolName).withMeta(serverInfoMeta()));
        }

        try {
            Object ret = ToolRawHelper.invokeTool(rawTool, arguments, invocationHandler);
            return JsonRpcResponseV2026.success(request.getId(), JsonRpcToolCallResultV2026.success(toText(ret)).withMeta(serverInfoMeta()));
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return JsonRpcResponseV2026.success(request.getId(), JsonRpcToolCallResultV2026.error("tool call error, " + e.getMessage()).withMeta(serverInfoMeta()));
        }
    }


    /**
     * 构造 result._meta，回带服务端身份（io.modelcontextprotocol/serverInfo）。
     */
    protected Map<String, Object> serverInfoMeta() {
        JsonRpcServerInfo serverInfo = new JsonRpcServerInfo();
        serverInfo.setName(properties.getServerName());
        serverInfo.setVersion(properties.getServerVersion());

        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put(OfficialMcpConstantsV2026.META_SERVER_INFO, serverInfo);
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


    protected MvcJsonRpcResponse<JsonRpcResponseV2026<?>> headerMismatch(String id, String message) {
        return MvcJsonRpcResponse.error(HttpStatus.BAD_REQUEST, JsonRpcResponseV2026.error(id, OfficialMcpConstantsV2026.CODE_HEADER_MISMATCH, "Header mismatch: " + message));
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

    protected String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
