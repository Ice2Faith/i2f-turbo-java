package i2f.springboot.ai.mcp.client.creator;

import i2f.ai.rest.mcp.simple.client.HttpSimpleMcpClientToolProvider;
import i2f.ai.std.mcp.McpToolProvider;
import i2f.io.stream.StreamUtil;
import i2f.match.regex.RegexUtil;
import i2f.net.http.data.HttpHeaders;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import i2f.springboot.ai.mcp.client.creator.data.McpServerItemProperties;
import i2f.springboot.ai.mcp.client.creator.data.McpServerMetaProperties;
import i2f.springboot.ai.mcp.client.creator.data.McpServerProperties;
import i2f.springboot.ai.mcp.client.official.v2024.stdio.StdioMcpClient;
import i2f.springboot.ai.mcp.client.official.v2024.stdio.provider.StdioJsonRpcMcpClientToolProvider;
import i2f.springboot.ai.mcp.client.official.v2024.stream.provider.StreamJsonRpcMcpClientToolProvider;
import i2f.springboot.ai.mcp.client.official.v2026.stdio.provider.StdioJsonRpcMcpClientV2026ToolProvider;
import i2f.springboot.ai.mcp.client.official.v2026.stream.provider.StreamJsonRpcMcpClientV2026ToolProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/10/15 16:17
 * @desc
 */
public class McpServerCreator {
    public IJsonSerializer jsonSerializer() {
        return new Json2Serializer();
    }

    public String variableReplacer(String variable) {
        String prop = System.getProperty(variable);
        if (prop != null && !prop.isEmpty()) {
            return prop;
        }
        String env = System.getenv(variable);
        return env;
    }

    public String processVariable(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        if (!str.contains("$")) {
            return str;
        }
        return RegexUtil.regexFindAndReplace(str, "\\$\\{[^\\}]*\\}", s -> {
            s = s.substring(2, s.length() - 1);
            String[] arr = s.split(":", 2);
            String prop = arr[0].trim();
            String def = arr.length > 1 ? arr[1] : "";
            String r = variableReplacer(prop);
            if (r != null) {
                return r;
            }
            return def;
        });
    }

    public List<McpToolProvider> create(File mcpJsonFile) throws IOException {
        String json = StreamUtil.readString(mcpJsonFile);
        return create(json);
    }

    public List<McpToolProvider> create(InputStream mcpJson) throws IOException {
        String json = StreamUtil.readString(mcpJson, true);
        return create(json);
    }

    public List<McpToolProvider> create(String mcpJson) {
        McpServerProperties properties = (McpServerProperties) jsonSerializer().deserialize(mcpJson, McpServerProperties.class);
        return create(properties);
    }

    public List<McpToolProvider> create(McpServerProperties properties) {
        List<McpToolProvider> ret = new ArrayList<>();
        Map<String, McpServerItemProperties> mcpServers = new LinkedHashMap<>();
        if (properties.getMcpServers() != null) {
            mcpServers.putAll(properties.getMcpServers());
        }
        if (properties.getServers() != null) {
            mcpServers.putAll(properties.getServers());
        }

        for (Map.Entry<String, McpServerItemProperties> entry : mcpServers.entrySet()) {
            McpServerItemProperties value = entry.getValue();
            if (value == null) {
                continue;
            }
            try {
                McpToolProvider provider = create(entry.getKey(), value);
                if (provider != null) {
                    ret.add(provider);
                }
            } catch (Exception e) {
                // ignore
            }
        }
        return ret;
    }

    public McpToolProvider create(String name, McpServerItemProperties properties) {
        McpServerItemProperties.Type type = properties.getType();
        if (type == null) {
            return null;
        }
        boolean disabled = properties.isDisabled();
        if (disabled) {
            return null;
        }
        if (type == McpServerItemProperties.Type.stdio) {
            String command = properties.getCommand();
            if (command == null || command.isEmpty()) {
                return null;
            }
            return createStdio(name, properties);
        } else if (type == McpServerItemProperties.Type.http) {
            String url = properties.getUrl();
            if (url == null || url.isEmpty()) {
                return null;
            }
            return createHttp(name, properties);
        } else if (type == McpServerItemProperties.Type.simple) {
            String url = properties.getUrl();
            if (url == null || url.isEmpty()) {
                return null;
            }
            return createSimple(name, properties);
        }
        return null;
    }

    public StdioMcpClient createStdioClient(McpServerItemProperties properties) throws IOException {
        List<String> cmdArray = new ArrayList<>();
        cmdArray.add(processVariable(properties.getCommand()));
        if (properties.getArgs() != null) {
            for (String arg : properties.getArgs()) {
                cmdArray.add(processVariable(arg));
            }
        }
        Map<String, Object> env = properties.getEnv();
        List<String> envArray = new ArrayList<>();
        if (env != null) {
            for (Map.Entry<String, Object> entry : env.entrySet()) {
                String key = entry.getKey();
                if (key == null || key.isEmpty()) {
                    continue;
                }
                Object value = entry.getValue();
                if (value == null) {
                    value = "";
                }

                envArray.add(processVariable(key) + "=" + processVariable(String.valueOf(value)));
            }
        }
        File dir = null;
        String cwd = properties.getCwd();
        if (cwd != null && !cwd.isEmpty()) {
            dir = new File(processVariable(cwd));
        }
        return new StdioMcpClient(
                cmdArray.toArray(new String[0]),
                envArray.isEmpty() ? null : envArray.toArray(new String[0]),
                dir
        );
    }

    public McpToolProvider createStdioV2026(String name, McpServerItemProperties properties) {
        StdioMcpClient stdioClient = null;
        StdioJsonRpcMcpClientV2026ToolProvider provider = null;
        try {
            stdioClient = createStdioClient(properties);
            provider = new StdioJsonRpcMcpClientV2026ToolProvider();
            provider.setName(name);
            if (properties.getName() != null && !properties.getName().isEmpty()) {
                provider.setName(processVariable(properties.getName()));
            }
            provider.setDescription(processVariable(properties.getDescription()));
            provider.setJsonSerializer(jsonSerializer());
            McpServerMetaProperties meta = properties.getMeta();
            if (meta != null) {
                provider.setTagRules(meta.getTagRules());
            }
            provider.setStdioClient(stdioClient);
            // verify
            provider.listTools();
            return provider;
        } catch (Exception e) {
            // ignore
        }
        if (stdioClient != null) {
            stdioClient.close();
        }
        return null;
    }

    public McpToolProvider createStdioV2024(String name, McpServerItemProperties properties) {
        StdioMcpClient stdioClient = null;
        try {
            stdioClient = createStdioClient(properties);
            StdioJsonRpcMcpClientToolProvider provider = new StdioJsonRpcMcpClientToolProvider();
            provider.setName(name);
            if (properties.getName() != null && !properties.getName().isEmpty()) {
                provider.setName(processVariable(properties.getName()));
            }
            provider.setDescription(processVariable(properties.getDescription()));
            provider.setJsonSerializer(jsonSerializer());
            McpServerMetaProperties meta = properties.getMeta();
            if (meta != null) {
                provider.setTagRules(meta.getTagRules());
            }
            provider.setStdioClient(stdioClient);
            // verify
            provider.listTools();
            return provider;
        } catch (IOException ex) {
            // ignore
        }
        return null;
    }

    public McpToolProvider createStdio(String name, McpServerItemProperties properties) {
        McpToolProvider providerV2026 = createStdioV2026(name, properties);
        if (providerV2026 != null) {
            return providerV2026;
        }
        return createStdioV2024(name, properties);
    }

    public McpToolProvider createHttpV2026(String name, McpServerItemProperties properties) {
        try {
            StreamJsonRpcMcpClientV2026ToolProvider provider = new StreamJsonRpcMcpClientV2026ToolProvider();
            provider.setName(name);
            if (properties.getName() != null && !properties.getName().isEmpty()) {
                provider.setName(processVariable(properties.getName()));
            }
            provider.setDescription(processVariable(properties.getDescription()));
            provider.setJsonSerializer(jsonSerializer());
            McpServerMetaProperties meta = properties.getMeta();
            if (meta != null) {
                provider.setTagRules(meta.getTagRules());
            }
            provider.setBaseUrl(processVariable(properties.getUrl()));
            provider.setHeaders(HttpHeaders.create().apply(h -> {
                Map<String, Object> headers = properties.getHeaders();
                if (headers != null) {
                    for (Map.Entry<String, Object> entry : headers.entrySet()) {
                        String key = entry.getKey();
                        if (key == null || key.isEmpty()) {
                            continue;
                        }
                        Object value = entry.getValue();
                        if (value == null) {
                            value = "";
                        }
                        h.add(processVariable(key), processVariable(String.valueOf(value)));
                    }
                }
            }));
            // verify
            provider.listTools();
            return provider;
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    public McpToolProvider createHttpV2024(String name, McpServerItemProperties properties) {
        StreamJsonRpcMcpClientToolProvider provider = new StreamJsonRpcMcpClientToolProvider();
        provider.setName(name);
        if (properties.getName() != null && !properties.getName().isEmpty()) {
            provider.setName(processVariable(properties.getName()));
        }
        provider.setDescription(processVariable(properties.getDescription()));
        provider.setJsonSerializer(jsonSerializer());
        McpServerMetaProperties meta = properties.getMeta();
        if (meta != null) {
            provider.setTagRules(meta.getTagRules());
        }
        provider.setBaseUrl(processVariable(properties.getUrl()));
        provider.setHeaders(HttpHeaders.create().apply(h -> {
            Map<String, Object> headers = properties.getHeaders();
            if (headers != null) {
                for (Map.Entry<String, Object> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    if (key == null || key.isEmpty()) {
                        continue;
                    }
                    Object value = entry.getValue();
                    if (value == null) {
                        value = "";
                    }
                    h.add(processVariable(key), processVariable(String.valueOf(value)));
                }
            }
        }));
        // verify
        provider.listTools();
        return provider;
    }

    public McpToolProvider createHttp(String name, McpServerItemProperties properties) {
        McpToolProvider providerV2026 = createHttpV2026(name, properties);
        if (providerV2026 != null) {
            return providerV2026;
        }

        return createHttpV2024(name, properties);
    }

    public McpToolProvider createSimple(String name, McpServerItemProperties properties) {
        HttpSimpleMcpClientToolProvider provider = new HttpSimpleMcpClientToolProvider();
        provider.setName(name);
        if (properties.getName() != null && !properties.getName().isEmpty()) {
            provider.setName(processVariable(properties.getName()));
        }
        provider.setDescription(processVariable(properties.getDescription()));
        provider.setJsonSerializer(jsonSerializer());
        provider.setBaseUrl(processVariable(properties.getUrl()));
        provider.setAppId(processVariable(properties.getAppId()));
        provider.setAppKey(processVariable(properties.getAppKey()));
        provider.setHmacName(processVariable(properties.getHmacName()));
        // verify
        provider.listTools();
        return provider;
    }
}
