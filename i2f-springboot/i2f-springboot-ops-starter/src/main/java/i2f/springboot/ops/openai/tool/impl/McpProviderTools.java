package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.mcp.McpToolProvider;
import i2f.ai.std.mcp.gateway.AbstractMcpToolGatewayManager;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.ai.std.tool.definition.ToolDefinition;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    public static ThreadLocal<List<ToolDefinition>> TOOLS_HOLDER = new InheritableThreadLocal<>();

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
    public String load_tools_from_providers(
            @ToolParam(value = "providerNames", description = "provider name(s), cloud be null means all providers, for example [\"app_context\"] or [\"file\", \"command\"]")
            List<String> providerNames) {
        TOOLS_HOLDER.remove();

        if (providerNames == null) {
            providerNames = new ArrayList<>();
        }
        List<McpToolProvider> mcpProviders = gatewayManager.getMcpProviders();

        List<ToolDefinition> tools = new ArrayList<>();
        for (McpToolProvider mcpProvider : mcpProviders) {
            String name = mcpProvider.getName();
            if (providerNames.isEmpty() || providerNames.contains(name)) {
                tools.addAll(mcpProvider.getTools());
            }
        }
        TOOLS_HOLDER.set(tools);

        return (providerNames.isEmpty() ? "all" : String.valueOf(providerNames)) + " providers loaded " + tools.size() + " tools.";
    }


}
