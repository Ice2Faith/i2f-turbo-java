package i2f.springboot.ai.mcp.server.springweb.impl;

import i2f.ai.rest.mcp.HttpSimpleMcpConstants;
import i2f.ai.rest.mcp.data.McpCallPayloadDto;
import i2f.ai.rest.mcp.server.HttpSimpleMcpServer;
import i2f.ai.rest.mcp.server.data.HttpSimpleMcpRequest;
import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.mutator.BaseMutator;
import i2f.net.http.data.HttpHeaders;
import i2f.resp.ApiResp;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/7/13 13:49
 * @desc
 */
@Data
@NoArgsConstructor
@RestController
public class SpringHttpSimpleMcpController implements BaseMutator<SpringHttpSimpleMcpController> {

    protected HttpSimpleMcpServer server;
    protected IJsonSerializer jsonSerializer = new Json2Serializer();

    @GetMapping(HttpSimpleMcpConstants.URL_PATH_GET_TOOLS)
    public ApiResp<?> getTools(HttpServletRequest request) {
        HttpSimpleMcpRequest mcpRequest = new HttpSimpleMcpRequest();
        mcpRequest.setHeaders(new HttpHeaders());
        mcpRequest.setPayloadDto(null);

        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Enumeration<String> headers = request.getHeaders(name);
            while (headers.hasMoreElements()) {
                String value = headers.nextElement();
                mcpRequest.getHeaders().add(name, value);
            }
        }

        ApiResp<List<ToolDefinition>> ret = server.getTools(mcpRequest);
        return ret;
    }

    @GetMapping(HttpSimpleMcpConstants.URL_PATH_CALL_TOOL)
    public ApiResp<?> getTools(HttpServletRequest request,
                               @RequestBody McpCallPayloadDto payloadDto) {
        HttpSimpleMcpRequest mcpRequest = new HttpSimpleMcpRequest();
        mcpRequest.setHeaders(new HttpHeaders());
        mcpRequest.setPayloadDto(payloadDto);

        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Enumeration<String> headers = request.getHeaders(name);
            while (headers.hasMoreElements()) {
                String value = headers.nextElement();
                mcpRequest.getHeaders().add(name, value);
            }
        }

        String content = payloadDto.getContent();
        ToolBaseCallRequest callRequest = (ToolBaseCallRequest) jsonSerializer.deserialize(content, ToolBaseCallRequest.class);

        Map<String, Object> contextMap = jsonSerializer.deserializeAsMap(payloadDto.getContext());

        try {
            ToolCallContextHolder.replaceAs(contextMap);
            ApiResp<?> ret = server.callTool(callRequest, mcpRequest);
            return ret;
        } finally {
            ToolCallContextHolder.clear();
        }
    }
}
