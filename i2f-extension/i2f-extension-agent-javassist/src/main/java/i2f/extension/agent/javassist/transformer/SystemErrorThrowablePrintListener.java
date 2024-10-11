package i2f.extension.agent.javassist.transformer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/10/11 9:26
 */
public class SystemErrorThrowablePrintListener implements Predicate<Throwable> {
    public static final SystemErrorThrowablePrintListener INSTANCE = new SystemErrorThrowablePrintListener();

    @Override
    public boolean test(Throwable thr) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        thr.printStackTrace(ps);
        System.err.println("=============================\n====" + thr.hashCode() + "@" + new String(bos.toByteArray()));
        return true;
    }
}
