package i2f.turbo.idea.plugin.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Ice2Faith
 * @date 2026/5/13 16:14
 * @desc
 */
public class IdeaExceptionUtil {
    public static String getThrowableStackTraceText(Throwable ex) {
        StringBuilder ret = new StringBuilder();
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        pw.flush();
        String text = writer.toString();
        String[] lines = text.split("\n");
        for (String line : lines) {
            String str = line.trim();
            if (str.startsWith("at ")) {
                if (str.contains("com.intellij.")
                        || str.contains("java.desktop")
                        || str.contains("javax.swing.")
                        || str.contains("java.awt.")
                        || str.contains("java.base/java")) {
                    continue;
                }
            }
            ret.append(line).append("\n");
        }
        return ret.toString();
    }
}
