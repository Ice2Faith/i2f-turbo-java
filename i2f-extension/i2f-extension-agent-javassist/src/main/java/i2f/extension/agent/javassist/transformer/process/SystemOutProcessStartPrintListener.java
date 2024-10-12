package i2f.extension.agent.javassist.transformer.process;

import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/10/11 9:26
 */
public class SystemOutProcessStartPrintListener implements Predicate<ProcessBuilder> {
    public static final SystemOutProcessStartPrintListener INSTANCE = new SystemOutProcessStartPrintListener();

    @Override
    public boolean test(ProcessBuilder builder) {
        System.out.println("=============================\nprocess====" + builder.command());
        return true;
    }
}
