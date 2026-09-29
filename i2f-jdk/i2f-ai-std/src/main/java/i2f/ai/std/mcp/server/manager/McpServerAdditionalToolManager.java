package i2f.ai.std.mcp.server.manager;

import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.ToolManagerContract;
import i2f.ai.std.tool.ToolManager;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.context.std.IContext;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/9/29 11:19
 * @desc
 */
@Data
@NoArgsConstructor
public class McpServerAdditionalToolManager implements ToolManager {
    protected IContext context;
    protected ToolManager primaryManager;

    public List<ToolManagerContract> toolManagers() {
        List<ToolManagerContract> ret = new ArrayList<>();
        if (primaryManager != null) {
            ret.add(primaryManager);
        }
        List<McpServerAdditionalToolProvider> beans = context.getBeans(McpServerAdditionalToolProvider.class);
        if (beans != null) {
            ret.addAll(beans);
        }
        return ret;
    }

    @Override
    public List<ToolDefinition> listTools() {
        List<ToolDefinition> ret = new ArrayList<>();
        for (ToolManagerContract manager : toolManagers()) {
            ret.addAll(manager.listTools());
        }
        return ret;
    }

    @Override
    public boolean support(ToolBaseCallRequest request) {
        for (ToolManagerContract manager : toolManagers()) {
            if (manager.support(request)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object callTool(ToolBaseCallRequest request) throws Throwable {
        for (ToolManagerContract manager : toolManagers()) {
            if (manager.support(request)) {
                return manager.callTool(request);
            }
        }
        throw new IllegalArgumentException("not found tool to call");
    }

}
