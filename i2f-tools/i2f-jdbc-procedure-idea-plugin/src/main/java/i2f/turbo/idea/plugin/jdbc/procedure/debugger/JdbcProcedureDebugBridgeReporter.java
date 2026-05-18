package i2f.turbo.idea.plugin.jdbc.procedure.debugger;

import i2f.jvm.JvmUtil;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2026/5/15 20:20
 * @desc
 */
public final class JdbcProcedureDebugBridgeReporter {
    protected String fileName;
    protected int lineNumber;
    protected Map<String, Object> variableMap;

    public static void proxy(String fileName, int lineNumber, Supplier<Map<String, Object>> variableMapSupplier) {
        if (!JvmUtil.isDebug()) {
            return;
        }
        proxy(fileName, lineNumber, variableMapSupplier.get());
    }

    public static void proxy(String fileName, int lineNumber, Map<String, Object> variableMap) {
        if (!JvmUtil.isDebug()) {
            return;
        }
        JdbcProcedureDebugBridgeReporter instance = new JdbcProcedureDebugBridgeReporter();
        instance.fileName = fileNameOnly(fileName);
        instance.lineNumber = lineNumber;
        instance.variableMap = variableMap;
        instance.report();
    }

    public static String fileNameOnly(String fileName) {
        if (fileName == null) {
            return null;
        }
        fileName = fileName.replace("\\", "/");
        int idx = fileName.lastIndexOf('/');
        if (idx >= 0) {
            fileName = fileName.substring(idx + 1);
        }
        return fileName;
    }


    public void report() {
        if (fileName == null && lineNumber < 0) {
            throw new IllegalArgumentException("fileName=" + fileName + ", lineNumber=" + lineNumber);
        }
    }
}
