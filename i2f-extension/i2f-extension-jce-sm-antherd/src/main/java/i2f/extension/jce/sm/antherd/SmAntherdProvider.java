package i2f.extension.jce.sm.antherd;


import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/3/28 11:34
 * @desc
 */
public class SmAntherdProvider {
    public static final String MAVEN_DEPENDENCY = "" +
            "<dependency>\n" +
            "    <groupId>com.antherd</groupId>\n" +
            "    <artifactId>sm-crypto</artifactId>\n" +
            "    <version>0.3.2.1-RELEASE</version>\n" +
            "</dependency>";
    public static final String NPM_DEPENDENCY = "" +
            "npm install --save sm-crypto";

    public static final String SM_ANTHERD_CLASS_NAME = "com.antherd.smcrypto.sm2.Sm2";

    private static final AtomicBoolean printed = new AtomicBoolean(false);
    private static final AtomicBoolean checked = new AtomicBoolean(false);
    private static final AtomicBoolean status = new AtomicBoolean(false);

    static {
        printNonDependency();
    }

    public static void printNonDependency() {
        if (printed.getAndSet(true)) {
            return;
        }
        boolean has = checkDependency();
        if (has) {
            return;
        }
        System.err.println("none dependency found! please add this dependency into your pom.xml: \n" +
                MAVEN_DEPENDENCY + " \n" +
                "web project add this dependency: \n" +
                NPM_DEPENDENCY);
    }

    public static synchronized boolean checkDependency() {
        if (checked.get()) {
            return status.get();
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String[] arr = new String[]{SM_ANTHERD_CLASS_NAME};
        for (int i = 0; i < arr.length; i++) {
            String className = arr[i];
            try {
                Class<?> clazz = Class.forName(className);
                if (clazz != null) {
                    status.set(true);
                    break;
                }
            } catch (Throwable e) {

            }
            try {
                Class<?> clazz = loader.loadClass(className);
                if (clazz != null) {
                    status.set(true);
                    break;
                }
            } catch (Throwable e) {

            }
        }
        checked.set(true);
        return status.get();

    }
}
