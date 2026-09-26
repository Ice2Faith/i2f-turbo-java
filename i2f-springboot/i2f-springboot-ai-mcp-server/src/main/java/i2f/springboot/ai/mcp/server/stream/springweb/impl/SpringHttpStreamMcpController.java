package i2f.springboot.ai.mcp.server.stream.springweb.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.rest.mcp.official.consts.OfficialMcpConstants;
import i2f.ai.rest.mcp.official.data.JsonRpcResponse;
import i2f.ai.rest.mcp.official.data.result.*;
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
import i2f.springboot.ai.mcp.server.stream.auth.StreamMcpServerAuthFilter;
import i2f.springboot.ai.mcp.server.stream.data.ServerJsonRpcRequest;
import i2f.springboot.ai.mcp.server.stream.properties.OfficialMcpServerProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 官方 MCP 协议（protocolVersion: 2024-11-05）Streamable HTTP 传输的 SpringMVC 服务端实现。
 * <p>
 * 区别于 {@code simple} 私有协议（{@link i2f.ai.rest.mcp.simple.server.HttpSimpleMcpServer}，带 HMAC 签名验证），
 * 本实现直接使用官方标准的 JSON-RPC 2.0 报文格式（initialize / tools/list / tools/call），
 * 不依赖官方 SDK（官方SDK要求 JDK17），也不做 HMAC 签名校验，
 * 而是直接桥接 {@link IContext} + {@link JsonSchemaAnnotationResolver} + {@link ToolRawHelper}
 * 复用本项目的工具定义解析与调用逻辑。
 *
 * @author Ice2Faith
 * @desc
 */
@Data
@NoArgsConstructor
@Slf4j
@RestController
public class SpringHttpStreamMcpController implements BaseMutator<SpringHttpStreamMcpController> {

    protected OfficialMcpServerProperties properties;

    protected IContext context;
    protected JsonSchemaAnnotationResolver annotationResolver = JsonSchemaAnnotationResolver.INSTANCE;
    protected IProxyInvocationHandler invocationHandler;
    protected IJsonSerializer jsonSerializer = new JacksonJsonSerializer(new ObjectMapper());

    protected StreamMcpServerAuthFilter streamMcpServerAuthFilter;


    @PostMapping(OfficialMcpConstants.URL_PATH_MCP)
    public JsonRpcResponse<?> handle(@RequestBody ServerJsonRpcRequest payload,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
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
                    return JsonRpcResponse.error(payload.getId(), OfficialMcpConstants.CODE_INVALID_REQUEST, "auth not passed!");
                }
            }
            String method = payload.getMethod();
            if (method == null || method.isEmpty()) {
                return JsonRpcResponse.error(payload.getId(), OfficialMcpConstants.CODE_INVALID_REQUEST, "missing jsonrpc method!");
            }
            switch (method) {
                case OfficialMcpConstants.METHOD_INITIALIZE:
                    return initialize(payload);
                case OfficialMcpConstants.METHOD_TOOLS_LIST:
                    return listTools(payload);
                case OfficialMcpConstants.METHOD_TOOLS_CALL:
                    return callTool(payload);
                default:
                    return JsonRpcResponse.error(payload.getId(), OfficialMcpConstants.CODE_METHOD_NOT_FOUND, "method not found: " + method);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return JsonRpcResponse.error(payload.getId(), OfficialMcpConstants.CODE_INTERNAL_ERROR, e.getMessage());
        }
    }

    @DeleteMapping(OfficialMcpConstants.URL_PATH_MCP)
    public void terminateSession() {
        // TODO: 当前实现为无状态服务，未生成/校验 Mcp-Session-Id，此处仅作为会话终止的占位实现
    }

    protected JsonRpcResponse<?> initialize(ServerJsonRpcRequest request) {
        JsonRpcInitialResult result = new JsonRpcInitialResult();
        result.setProtocolVersion(OfficialMcpConstants.PROTOCOL_VERSION);

        Map<String, Object> tools = new LinkedHashMap<>();
        tools.put("listChanged", false);
        Map<String, Object> capabilities = new LinkedHashMap<>();
        capabilities.put("tools", tools);
        result.setCapabilities(capabilities);

        Map<String, Object> serverInfo = new LinkedHashMap<>();
        serverInfo.put("name", properties.getServerName());
        serverInfo.put("version", properties.getServerVersion());

        result.setServerInfo(serverInfo);

        return JsonRpcResponse.success(request.getId(), result);
    }

    protected JsonRpcResponse<?> listTools(ServerJsonRpcRequest request) {
        Map<String, ToolRawDefinition> definitionMap = ToolRawHelper.parseTools(annotationResolver, context);

        List<JsonRpcToolListItem> tools = new ArrayList<>();
        for (ToolRawDefinition definition : definitionMap.values()) {
            JsonRpcToolListItem item = new JsonRpcToolListItem();
            item.setName(definition.getName());
            item.setDescription(definition.getDescription());
            item.setInputSchema(definition.getJsonSchema().getParameters());
            tools.add(item);
        }

        JsonRpcToolListResult result = new JsonRpcToolListResult();
        result.setTools(tools);
        return JsonRpcResponse.success(request.getId(), result);
    }


    @SuppressWarnings("unchecked")
    protected JsonRpcResponse<?> callTool(ServerJsonRpcRequest request) {
        Map<String, Object> map = request.getParams();
        if (map == null) {
            return JsonRpcResponse.error(request.getId(), OfficialMcpConstants.CODE_INVALID_PARAMS, "missing tools/call params!");
        }
        JsonRpcToolCallParam params = RichConverter.convert(map, JsonRpcToolCallParam.class);

        String toolName = params.getName();
        if (toolName == null || toolName.isEmpty()) {
            return JsonRpcResponse.error(request.getId(), OfficialMcpConstants.CODE_INVALID_PARAMS, "missing tools/call params.name!");
        }

        Map<String, ToolRawDefinition> definitionMap = ToolRawHelper.parseTools(annotationResolver, context);
        ToolRawDefinition rawTool = definitionMap.get(toolName);
        if (rawTool == null) {
            return JsonRpcResponse.success(request.getId(), JsonRpcToolCallResult.error("un-support tool call request, tool not found: " + toolName));
        }

        Map<String, Object> argumentsMap = params.getArguments();

        try {
            Object ret = ToolRawHelper.invokeTool(rawTool, argumentsMap, invocationHandler);
            return JsonRpcResponse.success(request.getId(), JsonRpcToolCallResult.success(toText(ret)));
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return JsonRpcResponse.success(request.getId(), JsonRpcToolCallResult.error(e.getMessage()));
        }
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


}
