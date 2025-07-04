package i2f.resources;

import i2f.io.stream.StreamUtil;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2022/1/18 13:54
 * @desc
 */
public class ResourceUtil {

    public static void main(String[] args) throws IOException {
        Map<URL, String> res = matchResources("classpath:langs", (str) -> {
            return str.matches("lang*\\.properties");
        });
        for (Object item : res.entrySet()) {
            System.out.println(item);
        }
    }

    public static final String CLASSPATH_PREFIX = "classpath:";
    public static final String CLASSPATH_MUL_PREFIX = "classpath*:";

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
        boolean isClassPath = false;
        if (lloc.startsWith(CLASSPATH_PREFIX)) {
            lloc = location.substring(CLASSPATH_PREFIX.length());
            isClassPath = true;
        } else if (lloc.startsWith(CLASSPATH_MUL_PREFIX)) {
            lloc = location.substring(CLASSPATH_MUL_PREFIX.length());
            isClassPath = true;
        }
        if (isClassPath) {
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
            try {
                URL url = new URL(location);
                if (url != null) {
                    return new URL[]{url};
                }
            } catch (Exception e) {

            }
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

    public static Set<File> getResourcesFiles(Collection<String> locations) {
        Set<File> ret = new LinkedHashSet<>();
        if (locations == null) {
            return ret;
        }
        for (String location : locations) {
            try {
                Set<File> files = getResourceFiles(location);
                ret.addAll(files);
            } catch (Exception e) {

            }
        }
        return ret;
    }

    public static Set<File> getResourceFiles(String location) {
        Set<File> ret = new LinkedHashSet<>();
        if (location == null) {
            return ret;
        }

        Set<String> rootClassPathsDirs = new LinkedHashSet<>();
        try {
            rootClassPathsDirs = ResourceUtil.getAllRootClassPaths().stream().filter(e -> e.isDirectory()).map(e -> e.getAbsolutePath()).collect(Collectors.toSet());
        } catch (Exception e) {

        }


        boolean isClassPath = false;
        String path = location;
        if (location.startsWith(CLASSPATH_PREFIX)) {
            path = location.substring(CLASSPATH_PREFIX.length());
            isClassPath = true;
        } else if (location.startsWith(CLASSPATH_MUL_PREFIX)) {
            path = location.substring(CLASSPATH_MUL_PREFIX.length());
            isClassPath = true;
        } else if (location.startsWith("file:")) {
            try {
                URL url = new URL(location);
                String protocol = url.getProtocol();
                if ("file".equalsIgnoreCase(protocol)) {
                    path = url.getFile();
                }
            } catch (Exception e) {
                path = location.substring("file:".length());
            }
        } else {
            path = location;
        }
        if (isClassPath) {
            for (String dir : rootClassPathsDirs) {
                try {
                    File file = new File(dir, path);
                    ret.add(file);
                } catch (Exception e) {

                }
            }
        } else {
            try {
                File file = new File(path);
                ret.add(new File(file.getAbsolutePath()));
            } catch (Exception e) {

            }
        }
        return ret;
    }

    public static Set<File> getAllRootClassPaths() throws IOException {
        Set<File> ret = new LinkedHashSet<>();
        String[] classPaths = ManagementFactory.getRuntimeMXBean().getClassPath().split(";");
        URL[] resources = ResourceUtil.getClasspathResources("");
        for (String path : classPaths) {
            try {
                File file = new File(path);
                if (file.exists()) {
                    ret.add(file);
                }
            } catch (Exception e) {

            }
        }
        for (URL resource : resources) {
            try {
                String protocol = resource.getProtocol();
                if ("file".equalsIgnoreCase(protocol)) {
                    File file = new File(resource.getFile());
                    if (file.exists()) {
                        ret.add(file);
                    }
                }
            } catch (Exception e) {

            }
        }
        return ret;
    }
}
