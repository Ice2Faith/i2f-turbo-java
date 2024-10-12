package i2f.extension.agent.javassist.transformer.rmi;

import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/10/11 9:26
 */
public class SystemOutRmiNamingLookupPrintListener implements Predicate<String> {
    public static final SystemOutRmiNamingLookupPrintListener INSTANCE = new SystemOutRmiNamingLookupPrintListener();

    @Override
    public boolean test(String name) {
        System.out.println("=============================\nrmi-naming-lookup====" + name);
        return true;
    }
}
