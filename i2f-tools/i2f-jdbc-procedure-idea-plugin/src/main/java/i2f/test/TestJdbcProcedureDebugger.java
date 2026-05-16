package i2f.test;

import i2f.resources.ResourceUtil;
import i2f.turbo.idea.plugin.jdbc.procedure.debugger.JdbcProcedureDebugBridgeReporter;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/5/15 21:21
 * @desc
 */
public class TestJdbcProcedureDebugger {
    public static void main(String[] args) throws Exception {

        String resourceFile = "/assets/xproc4j/test.xml";
        String script = ResourceUtil.getClasspathResourceAsString(resourceFile, StandardCharsets.UTF_8.name());
        String[] lines = script.split("\n");
        for (int i = 0; i < lines.length; i++) {
            int lineNumber = i + 1;
            String code = lines[i];
            Map<String, Object> variableMap = new HashMap<>();
            variableMap.put("xLine", lineNumber);
            variableMap.put("xFile", resourceFile);
            variableMap.put("i", i);
            variableMap.put("code", code);
            System.out.println("line:" + lineNumber + ": " + code);
            JdbcProcedureDebugBridgeReporter.proxy(new File(resourceFile).getName(), lineNumber, variableMap);
            System.out.println("after");
        }
        System.out.println("ok");
    }


}
