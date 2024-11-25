package i2f.launcher.test;

import i2f.extension.cglib.CglibProxyProvider;

/**
 * @author Ice2Faith
 * @date 2024/11/25 17:55
 */
public class TestAppExtLibLauncher {
    public static void main(String[] args) throws Exception {
        System.out.println("=============main==============");
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(contextClassLoader);
        CglibProxyProvider provider = new CglibProxyProvider();
        System.out.println(provider);
        System.out.println("main");
    }
}
