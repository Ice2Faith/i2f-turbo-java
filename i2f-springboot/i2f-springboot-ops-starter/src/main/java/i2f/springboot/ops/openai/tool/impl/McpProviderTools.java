package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.mcp.McpToolProvider;
import i2f.ai.std.mcp.gateway.AbstractMcpToolGatewayManager;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.springboot.ops.openai.data.OpenAiOperateDto;
import i2f.springboot.ops.openai.tool.impl.a2a.AgentTools;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/7/5 17:27
 * @desc
 */
@Tools
public class McpProviderTools {
    private static final Set<String> exposeTools;

    static {
        Set<String> names = new HashSet<>();
        Method[] methods = McpProviderTools.class.getDeclaredMethods();
        for (Method method : methods) {
            Tool ann = method.getAnnotation(Tool.class);
            if (ann == null) {
                continue;
            }
            String name = method.getName();
            String value = ann.value();
            if (value != null && !value.isEmpty()) {
                name = value;
            }
            names.add(name);
        }
        exposeTools = Collections.unmodifiableSet(names);
    }

    public static Set<String> toolNames() {
        return new HashSet<>(exposeTools);
    }

    private AbstractMcpToolGatewayManager gatewayManager;

    public McpProviderTools(AbstractMcpToolGatewayManager gatewayManager) {
        this.gatewayManager = gatewayManager;
    }

    @Data
    @NoArgsConstructor
    public static class McpCategoryItem {
        protected String name;
        protected String description;
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE,
                    AiTags.READONLY_VALUE
            },
            description = "list all tool provider names"
    )
    public List<McpCategoryItem> tools_provider_list() {
        List<McpToolProvider> mcpProviders = gatewayManager.getMcpProviders();
        List<McpCategoryItem> ret = new ArrayList<>();
        for (McpToolProvider provider : mcpProviders) {
            McpCategoryItem item = new McpCategoryItem();
            item.setName(provider.getName());
            item.setDescription(provider.getDescription());

            ret.add(item);
        }
        return ret;
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE,
                    AiTags.READONLY_VALUE
            },
            description = "list all tools in tool provider(s)"
    )
    public List<McpCategoryItem> list_tools_from_providers(
            @ToolParam(value = "providerNames", description = "provider name(s), provider name must from `tools_provider_list` returns, cloud be null means all providers, for example [\"app_context\"] or [\"file\", \"command\"]")
            List<String> providerNames) {

        if (providerNames == null) {
            providerNames = new ArrayList<>();
        }
        List<McpToolProvider> mcpProviders = gatewayManager.getMcpProviders();

        List<ToolDefinition> tools = new ArrayList<>();
        for (McpToolProvider mcpProvider : mcpProviders) {
            String name = mcpProvider.getName();
            if (providerNames.isEmpty() || providerNames.contains(name)) {
                tools.addAll(gatewayManager.getProviderTools(mcpProvider));
            }
        }

        List<McpCategoryItem> items = new ArrayList<>();
        for (ToolDefinition tool : tools) {
            McpCategoryItem item = new McpCategoryItem();
            item.setName(tool.getName());
            item.setDescription(tool.getDescription());
            items.add(item);
        }

        return items;
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE,
                    AiTags.READONLY_VALUE
            },
            description = "load tools by tool name(s)"
    )
    public Map<String, Object> load_tools_by_names(
            @ToolParam(value = "toolNames", description = "tool name(s), tool name must from `list_tools_from_providers` returns, for example [\"app_context.current_time\"] or [\"file.read_text\", \"command\"]")
            List<String> toolNames) {

        if (toolNames == null) {
            toolNames = new ArrayList<>();
        }
        List<ToolDefinition> managerTools = gatewayManager.getTools();

        List<ToolDefinition> tools = new ArrayList<>();
        for (ToolDefinition managerTool : managerTools) {
            String name = managerTool.getName();
            if (toolNames.contains(name)) {
                tools.add(managerTool);
            }
        }

        synchronized (this) {
            OpenAiOperateDto req = AgentTools.REQUEST_HOLDER.get();
            if (req.getLoadedTools() == null) {
                req.setLoadedTools(new ArrayList<>());
            }
            req.getLoadedTools().addAll(tools);
        }

        List<McpCategoryItem> items = new ArrayList<>();
        Set<String> loaded = new HashSet<>();
        for (ToolDefinition tool : tools) {
            loaded.add(tool.getName());

            McpCategoryItem item = new McpCategoryItem();
            item.setName(tool.getName());
            item.setDescription(tool.getDescription());
            items.add(item);
        }

        Set<String> notLoad = new HashSet<>();
        for (String toolName : toolNames) {
            if (!loaded.contains(toolName)) {
                notLoad.add(toolName);
            }
        }

        if (!notLoad.isEmpty()) {
            throw new IllegalArgumentException("you should use `list_tools_from_providers` returns tool names, those tool not found: " + notLoad);
        }

        Map<String, Object> ret = new HashMap<>();
        ret.put("summary", "loaded " + items.size() + " tools.");
        ret.put("loadedTools", items);


        return ret;
    }

}
