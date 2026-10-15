package i2f.springboot.ai.mcp.client;

import i2f.ai.std.model.message.tool.ToolCallRequest;
import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.definition.ToolDefinition;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/10/14 9:55
 * @desc
 */
public class StdioJsonRpcMcpClientToolProviderTest {
    public static void main(String[] args) throws Throwable {
        StdioMcpClient client = new StdioMcpClient(new String[]{"npx",
                "-y",
                "@modelcontextprotocol/server-everything"
        });

        try (StdioJsonRpcMcpClientV2026ToolProvider provider = new StdioJsonRpcMcpClientV2026ToolProvider()) {
            provider.setStdioClient(client);

            List<ToolDefinition> tools = provider.listTools();
            System.out.println(tools);

            ToolBaseCallRequest request = new ToolCallRequest();
            request.setId("xx");
            request.setName("echo");
            request.setArguments("{}");
            Object result = provider.callTool(request);
            System.out.println(result);
        }
    }
}
