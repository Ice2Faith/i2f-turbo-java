package i2f.turbo.idea.plugin.funic;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/5/15 20:20
 * @desc
 */
public final class FunicDebugReporter {
    /**
     * Funic 调试钩子方法。
     * FunicPositionManager 在此方法入口处拦截 JVM 执行，
     * 读取 fileName 和 lineNumber 参数，映射到对应的 .fic 源码断点位置。
     *
     * 注意：方法体中保留有效语句，确保 JVM 生成完整的 LineNumberTable，
     * 使 allLineLocations() 能返回有效位置，从而正确设置 JVM 断点。
     */
    public void report(String fileName, int lineNumber, Map<String,Object> variableMap){
        if (fileName == null && lineNumber < 0) {
            throw new IllegalArgumentException("fileName=" + fileName + ", lineNumber=" + lineNumber);
        }
    }
}
