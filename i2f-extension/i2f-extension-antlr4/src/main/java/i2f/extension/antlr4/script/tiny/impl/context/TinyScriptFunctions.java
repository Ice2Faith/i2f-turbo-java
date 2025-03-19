package i2f.extension.antlr4.script.tiny.impl.context;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 20:52
 * @desc
 */
public class TinyScriptFunctions {
    public static void println(Object... obj) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Object item : obj) {
            if (!isFirst) {
                builder.append(", ");
            }
            builder.append(item);
            isFirst = false;
        }
        System.out.println(builder.toString());
    }

    public static void exec(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
