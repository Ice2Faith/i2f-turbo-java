package i2f.extension.agent.javassist.transformer.http;

import java.net.URL;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/10/11 9:26
 */
public class SystemOutUrlOpenConnectionPrintListener implements Predicate<URL> {
    public static final SystemOutUrlOpenConnectionPrintListener INSTANCE = new SystemOutUrlOpenConnectionPrintListener();

    @Override
    public boolean test(URL url) {
        System.out.println("=============================\nurl====" + url);
        return true;
    }
}
