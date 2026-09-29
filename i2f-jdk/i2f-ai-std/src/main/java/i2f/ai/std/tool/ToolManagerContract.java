package i2f.ai.std.tool;

import i2f.ai.std.tool.definition.ToolDefinition;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/7/5 14:40
 * @desc 抽象公共接口类
 * 自动配置时，不应该直接使用此类
 * 而应该使用具体的子类，此类只是为了作为公共接口约束存在
 */
public interface ToolManagerContract {
    // 防止使用getTools,这种getter,容易在lombok等环境与字段冲突，导致开发因疏忽导致的意外事件
    List<ToolDefinition> listTools();

    boolean support(ToolBaseCallRequest request);

    Object callTool(ToolBaseCallRequest request) throws Throwable;
}
