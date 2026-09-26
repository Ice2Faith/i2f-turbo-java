package i2f.ai.rest.mcp.server;

import i2f.ai.rest.mcp.server.data.HttpSimpleMcpRequest;
import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.resp.ApiResp;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/7/13 11:08
 * @desc
 */
public interface HttpSimpleMcpServer {
    ApiResp<List<ToolDefinition>> getTools(HttpSimpleMcpRequest mcpRequest);

    ApiResp<?> callTool(ToolBaseCallRequest request, HttpSimpleMcpRequest mcpRequest);
}
