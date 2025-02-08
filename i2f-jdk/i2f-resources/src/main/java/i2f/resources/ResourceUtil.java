package i2f.resources;

import i2f.io.stream.StreamUtil;
import i2f.match.IMatcher;
import i2f.match.impl.AntMatcher;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @author Ice2Faith
 * @date 2022/1/18 13:54
 * @desc
 */
public class ResourceUtil {

    public static void main(String[] args) throws IOException {
        Map<URL, String> res = resources("classpath:langs", "lang*.properties", new AntMatcher());
        for (Object item : res.entrySet()) {
            System.out.println(item);
        }
    }

    public static final String CLASSPATH_PREFIX = "classpath:";

    public static ClassLoader getLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ResourceUtil.class.getClassLoader();
        }
        return loader;
    }

    public static URL getClasspathResource(String location) throws IOException {
        URL[] urls = getClasspathResources(location);
        if (urls.length > 0) {
            return urls[0];
        }
        return null;
    }

    public static URL[] getClasspathResources(String location) throws IOException {
        return getResources(CLASSPATH_PREFIX + location);
    }

    public static URL getResource(String location) throws IOException {
        URL[] urls = getResources(location);
        if (urls.length > 0) {
            return urls[0];
        }
        return null;
    }

    public static URL[] getResources(String location) throws IOException {
        if (location == null) {
            return new URL[0];
        }
        String lloc = location.toLowerCase();
        if (lloc.startsWith(CLASSPATH_PREFIX)) {
            lloc = location.substring(CLASSPATH_PREFIX.length());
            if (lloc.startsWith("/")) {
                lloc = lloc.substring(1);
            }
            Enumeration<URL> enums = getLoader().getResources(lloc);
            HashSet<URL> set = new HashSet<>();
            while (enums.hasMoreElements()) {
                set.add(enums.nextElement());
            }
            return set.toArray(new URL[0]);
        } else {
            File file = new File(location);
            URL url = file.toURI().toURL();
            return new URL[]{url};
        }
    }

    public static InputStream getClasspathResourceAsStream(String location) throws IOException {
        return getResourceAsStream(CLASSPATH_PREFIX + location);
    }

    public static InputStream getResourceAsStream(String location) throws IOException {
        URL[] urls = getResources(location);
        if (urls.length == 0) {
            throw new FileNotFoundException("could not found resource as:" + location);
        }
        return urls[0].openStream();
    }

    public static byte[] getResourceAsBytes(String location) throws IOException {
        return StreamUtil.readBytes(getResourceAsStream(location), true);
    }

    public static String getResourceAsString(String location, String charset) throws IOException {
        return StreamUtil.readString(getResourceAsStream(location), charset, true);
    }

    public static byte[] getClasspathResourceAsBytes(String location) throws IOException {
        return StreamUtil.readBytes(getClasspathResourceAsStream(location), true);
    }

    public static String getClasspathResourceAsString(String location, String charset) throws IOException {
        return StreamUtil.readString(getClasspathResourceAsStream(location), charset, true);
    }

    /**
     * 获取所有在指定path下的用matcher匹配上patten的资源
     * 返回值：key就是资源URL，value就是匹配上的项的名称
     *
     * @param path    路径，支持classpath前缀写法
     * @param patten  匹配表达式
     * @param matcher 匹配器
     * @return
     * @throws IOException
     */
    public static Map<URL, String> resources(String path, String patten, IMatcher matcher) throws IOException {
        return matchResources(path, matcher == null ? null : (fname) -> {
            return matcher.matched(matcher.match(fname, patten));
        });
    }

    public static Map<URL, String> matchResources(String path, Predicate<String> itemFilter) throws IOException {
        Map<URL, String> ret = new HashMap<>();
        URL[] urls = getResources(path);
        for (URL url : urls) {
            String protocol = url.getProtocol().toLowerCase();
            if ("file".equals(protocol)) {
                File file = new File(url.getFile());
                File[] files = file.listFiles();
                for (File item : files) {
                    String fname = item.getName();
                    if (itemFilter != null) {
                        if (itemFilter.test(fname)) {
                            URL url1 = item.toURI().toURL();
                            ret.put(url1, fname);
                        }
                    } else {
                        URL url1 = item.toURI().toURL();
                        ret.put(url1, fname);
                    }
                }
            } else if ("jar".equals(protocol)) {
                String ufile = url.getFile();
                String fileName = ufile.substring("file:/".length());
                int idx = fileName.indexOf("!");
                if (idx < 0) {
                    idx = fileName.length();
                }
                String jarFile = fileName.substring(0, idx);
                try {
                    JarInputStream jis = new JarInputStream(new BufferedInputStream(new FileInputStream(jarFile)));

                    JarEntry entry = jis.getNextJarEntry();
                    while (entry != null) {
                        String name = entry.getName();
                        int pidx = name.lastIndexOf("/");
                        String fname = name;
                        if (pidx >= 0) {
                            fname = name.substring(pidx + 1);
                        }
                        if (itemFilter != null) {
                            if (itemFilter.test(fname)) {
                                URL[] all = getClasspathResources(name);
                                for (URL pitem : all) {
                                    ret.put(pitem, fname);
                                }
                            }
                        } else {
                            URL[] all = getClasspathResources(name);
                            for (URL pitem : all) {
                                ret.put(pitem, fname);
                            }
                        }
                        entry = jis.getNextJarEntry();
                    }
                    jis.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }
}
