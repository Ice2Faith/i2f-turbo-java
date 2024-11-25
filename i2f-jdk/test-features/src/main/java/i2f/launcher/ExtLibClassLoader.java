package i2f.launcher;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * @author Ice2Faith
 * @date 2024/11/25 16:25
 */
public class ExtLibClassLoader extends URLClassLoader {
    public ExtLibClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public ExtLibClassLoader(URL[] urls) {
        super(urls);
    }

    public ExtLibClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }
}
