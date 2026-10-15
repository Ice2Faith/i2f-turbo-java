package i2f.springboot.ai.mcp.client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ice2Faith
 * @date 2026/10/14 8:58
 * @desc
 */
public class StdioMcpClientTest {
    private static final AtomicLong idSeq = new AtomicLong(1);

    public static Object wrapRequest(String id, String method, Object params) {

        if (params == null) {
            params = new HashMap<>();
        }
        Map<String, Object> request = new HashMap<>();
        request.put("jsonrpc", "2.0");
        request.put("id", id);
        request.put("method", method);
        request.put("params", params);

        return request;
    }

    public static void main(String[] args) throws Exception {
        // 例如：node /path/to/server.js，或 java -jar server.jar
        try (StdioMcpClient client = new StdioMcpClient(new String[]{"npx",
                "-y",
                "@modelcontextprotocol/server-everything"
        })) {

            // 1. initialize
            Map<String, Object> initParams = new HashMap<>();
            initParams.put("protocolVersion", "2024-11-05");
            initParams.put("capabilities", new HashMap<>());

            Map<String, Object> initClientInfo = new HashMap<>();
            initClientInfo.put("name", "demo-client");
            initClientInfo.put("version", "1.0.0");
            initParams.put("clientInfo", initClientInfo);

            String initId = String.valueOf(idSeq.getAndIncrement());
            Object initResp = client.send(initId, wrapRequest(initId, "initialize", initParams)).get(60, TimeUnit.SECONDS);
            System.out.println("initialize: " + initResp);

            // 2. notifications/initialized
            client.send("notifications/initialized", null);

            // 3. tools/list
            String listId = String.valueOf(idSeq.getAndIncrement());
            Object toolsResp = client.send(listId, wrapRequest(listId, "tools/list", new HashMap<>()))
                    .get(10, TimeUnit.SECONDS);
            System.out.println("tools: " + toolsResp);

            // 4. tools/call
            Map<String, Object> callParams = new HashMap<>();
            callParams.put("name", "echo");

            Map<String, Object> callArgs = new HashMap<>();
            callArgs.put("message", "hello");
            callParams.put("arguments", callArgs);

            String callId = String.valueOf(idSeq.getAndIncrement());
            Object callResp = client.send(callId, wrapRequest(callId, "tools/call", callParams)).get(10, TimeUnit.SECONDS);
            System.out.println("call result: " + callResp);

        }
    }
}
