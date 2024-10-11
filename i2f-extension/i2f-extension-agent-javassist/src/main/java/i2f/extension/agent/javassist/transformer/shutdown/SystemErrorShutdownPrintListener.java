package i2f.extension.agent.javassist.transformer.shutdown;

import java.util.function.BiPredicate;

/**
 * @author Ice2Faith
 * @date 2024/10/11 9:26
 */
public class SystemErrorShutdownPrintListener implements BiPredicate<Integer, StackTraceElement[]> {
    public static final SystemErrorShutdownPrintListener INSTANCE = new SystemErrorShutdownPrintListener();

    @Override
    public boolean test(Integer exitCode, StackTraceElement[] stack) {
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement item : stack) {
            builder.append("   at ").append(item).append("\n");
        }
        System.err.println("=============================\nshutdown==== exit code:" + exitCode + "\n" + builder);
        return true;
    }
}
