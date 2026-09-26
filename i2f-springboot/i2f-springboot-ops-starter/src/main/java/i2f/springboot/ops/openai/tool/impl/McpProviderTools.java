package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.mcp.McpToolProvider;
import i2f.ai.std.mcp.gateway.AbstractMcpToolGatewayManager;
import i2f.ai.std.mcp.impl.ContextAppMcpToolProvider;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.springboot.ops.openai.data.OpenAiOperateDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2026/7/5 17:27
 * @desc
 */
@Tools(tags = {
        AiTags.MCP_VALUE
})
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

    public static final String NAME_LIST_PROVIDER = "tools_provider_list";
    public static final String NAME_LIST_TOOLS = "list_tools_from_providers";
    public static final String NAME_SEARCH_TOOLS = "search_tools";
    public static final String NAME_LOAD_TOOLS = "load_tools_by_names";

    public static final String SYSTEM_PROMPT = "# 动态工具查找与加载\n" +
            "\n" +
            "- 系统集成多个工具供应商\n" +
            "- 使用 `" + NAME_LIST_PROVIDER + "` 获取所有供应商列表\n" +
            "- 每个供应商提供一组工具\n" +
            "- 使用 `" + NAME_LIST_TOOLS + "` 获取某些供应商都提供了哪些工具\n" +
            "\t- 【重要】只能获取 `" + NAME_LIST_PROVIDER + "` 中支持的供应商\n" +
            "    - 其中 `" + ContextAppMcpToolProvider.DEFAULT_NAME + "` 供应商是特殊的，可能包含系统开发者单独提供的各个领域的工具\n" +
            "    - 当其他供应商没有相关工具时，`" + ContextAppMcpToolProvider.DEFAULT_NAME + "` 可能包含这些工具\n" +
            "- 可以使用 `" + NAME_SEARCH_TOOLS + "` 尝试直接查找可能提供的工具\n" +
            "\t- 【注意】直接找不到时，可以通过供应商列举查看方式逐步探索\n" +
            "- 工具需要被加载才能使用\n" +
            "- 使用 `" + NAME_LOAD_TOOLS + "` 来加载工具\n" +
            "\t- 【重要】只能加载来自 `" + NAME_LIST_TOOLS + "` 或 `" + NAME_SEARCH_TOOLS + "` 中能够提供的工具\n" +
            "- 工具数量有限制，使用LRU策略自动卸载最久未使用的工具\n" +
            "- 因此，需要动态加载工具";
    ;

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

    @Tool(value = NAME_LIST_PROVIDER,
            tags = {
                    AiTags.AUTO_VALUE,
                    AiTags.READONLY_VALUE
            },
            description = "list all tool provider names"
    )
    public Map<String, Object> tools_provider_list() {
        Map<String, Object> ret = new HashMap<>();

        List<McpToolProvider> mcpProviders = gatewayManager.getMcpProviders();
        List<McpCategoryItem> items = new ArrayList<>();
        for (McpToolProvider provider : mcpProviders) {
            McpCategoryItem item = new McpCategoryItem();
            item.setName(provider.getName());
            item.setDescription(provider.getDescription());

            items.add(item);
        }

        ret.put("providers", items);

        return ret;
    }

    @Tool(value = NAME_LIST_TOOLS,
            tags = {
                    AiTags.AUTO_VALUE,
                    AiTags.READONLY_VALUE
            },
            description = "list all tools in tool provider(s)"
    )
    public Map<String, Object> list_tools_from_providers(
            @ToolParam(value = "providerNames", description = "provider name(s), provider name must from `" + NAME_LIST_PROVIDER + "` returns, cloud be null means all providers, for example [\"app_context\"] or [\"file\", \"command\"]")
            List<String> providerNames) {
        Map<String, Object> ret = new HashMap<>();

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

        ret.put("tools", items);
        if (items.isEmpty()) {
            ret.put("hint", "not found any tools, maybe you need list `" + ContextAppMcpToolProvider.DEFAULT_NAME + "` provider");
        } else {
            ret.put("hint", items.size() + " tools found, next you maybe need use `" + NAME_LOAD_TOOLS + "` to load tools");
        }
        return ret;
    }

    @Tool(value = NAME_SEARCH_TOOLS,
            tags = {
                    AiTags.AUTO_VALUE,
                    AiTags.READONLY_VALUE
            },
            description = "search tool in all tool provider(s) which name or description partial matched regex. Note: use `^$` to full match, implements by java `matcher.find()`"
    )
    public Map<String, Object> search_tools(
            @ToolParam(value = "regex", description = "the regex for search , use java style, for example \"file|read|write\" or \"(?i)command\"")
            String regex) {
        Map<String, Object> ret = new HashMap<>();
        List<ToolDefinition> tools = gatewayManager.getTools();
        Pattern pattern = Pattern.compile(regex);

        List<McpCategoryItem> items = new ArrayList<>();
        for (ToolDefinition tool : tools) {
            Matcher matcher = pattern.matcher(tool.getName());
            if (matcher.find()) {
                McpCategoryItem item = new McpCategoryItem();
                item.setName(tool.getName());
                item.setDescription(tool.getDescription());
                items.add(item);
            }
        }

        ret.put("tools", items);
        if (items.isEmpty()) {
            ret.put("hint", "not found any tools, maybe you need list `" + ContextAppMcpToolProvider.DEFAULT_NAME + "` provider");
        } else {
            ret.put("hint", items.size() + " tools found, next you maybe need use `" + NAME_LOAD_TOOLS + "` to load tools");
        }
        return ret;
    }

    @Tool(value = NAME_LOAD_TOOLS,
            tags = {
                    AiTags.AUTO_VALUE,
                    AiTags.READONLY_VALUE
            },
            description = "load tools by tool name(s)"
    )
    public Map<String, Object> load_tools_by_names(
            @ToolParam(value = "toolNames", description = "tool name(s), tool name must from `" + NAME_LIST_TOOLS + "` returns, for example [\"app_context.current_time\"] or [\"file.read_text\", \"command\"]")
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
            OpenAiOperateDto req = ToolCallContextHolder.get("req");
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
            throw new IllegalArgumentException("you should use `" + NAME_LIST_TOOLS + "` returns tool names, those tool not found: " + notLoad);
        }

        Map<String, Object> ret = new HashMap<>();
        ret.put("summary", "loaded " + items.size() + " tools.");
        ret.put("loadedTools", items);


        return ret;
    }

}
