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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/7/5 17:27
 * @desc
 */
@Tools
public class McpProviderTools {
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
            description = "list all tools provider names"
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
            description = "load all tools in tool provider(s)"
    )
    public Map<String, Object> load_tools_from_providers(
            @ToolParam(value = "providerNames", description = "provider name(s), cloud be null means all providers, for example [\"app_context\"] or [\"file\", \"command\"]")
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

        synchronized (this) {
            OpenAiOperateDto req = AgentTools.REQUEST_HOLDER.get();
            if (req.getLoadedTools() == null) {
                req.setLoadedTools(new ArrayList<>());
            }
            req.getLoadedTools().addAll(tools);
        }

        List<McpCategoryItem> items = new ArrayList<>();
        for (ToolDefinition tool : tools) {
            McpCategoryItem item = new McpCategoryItem();
            item.setName(tool.getName());
            item.setDescription(item.getDescription());
            items.add(item);
        }

        Map<String, Object> ret = new HashMap<>();
        ret.put("summary", (providerNames.isEmpty() ? "all" : String.valueOf(providerNames)) + " providers loaded " + tools.size() + " tools.");
        ret.put("tools", items);

        return ret;
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE,
                    AiTags.READONLY_VALUE
            },
            description = "load tools by tool name(s)"
    )
    public Map<String, Object> load_tools_by_names(
            @ToolParam(value = "toolNames", description = "tool name(s)for example [\"app_context.current_time\"] or [\"file.read_text\", \"command\"]")
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
        for (ToolDefinition tool : tools) {
            McpCategoryItem item = new McpCategoryItem();
            item.setName(tool.getName());
            item.setDescription(item.getDescription());
            items.add(item);
        }

        Map<String, Object> ret = new HashMap<>();
        ret.put("summary", String.valueOf(toolNames) + " tools loaded " + tools.size() + " tools.");
        ret.put("tools", items);

        return ret;
    }


}
