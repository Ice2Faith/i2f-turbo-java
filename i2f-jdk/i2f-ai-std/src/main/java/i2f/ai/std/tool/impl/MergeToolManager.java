package i2f.ai.std.tool.impl;

import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.ToolManager;
import i2f.ai.std.tool.definition.ToolDefinition;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2026/9/28 21:19
 * @desc
 */
@Data
@NoArgsConstructor
public class MergeToolManager implements ToolManager {
    protected CopyOnWriteArrayList<ToolManager> list = new CopyOnWriteArrayList<>();

    public List<ToolManager> toolManagers() {
        return list;
    }

    @Override
    public List<ToolDefinition> getTools() {
        List<ToolDefinition> ret = new ArrayList<>();
        for (ToolManager manager : toolManagers()) {
            ret.addAll(manager.getTools());
        }
        return ret;
    }

    @Override
    public boolean support(ToolBaseCallRequest request) {
        for (ToolManager manager : toolManagers()) {
            if (manager.support(request)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object callTool(ToolBaseCallRequest request) throws Throwable {
        for (ToolManager manager : toolManagers()) {
            if (manager.support(request)) {
                return manager.callTool(request);
            }
        }
        throw new IllegalArgumentException("not found tool to call");
    }
}
