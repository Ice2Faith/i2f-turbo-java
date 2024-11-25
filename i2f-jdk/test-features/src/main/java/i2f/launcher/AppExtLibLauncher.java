package i2f.launcher;

import i2f.launcher.test.TestAppExtLibLauncher;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AppExtLibLauncher {
    public static void main(String[] args) throws Exception {
        // Example usage
        List<File> fileList = new ArrayList<>();
        fileList.add(new File("./resources"));
        fileList.add(new File("./config"));
        fileList.add(new File("./conf"));
        fileList.add(new File("./lib"));
        fileList.add(new File("./lib-ext"));
        fileList.add(new File("./libs"));
        fileList.add(new File("./static"));
        fileList.add(new File("./public"));
        fileList.add(new File("."));

        launch(fileList, TestAppExtLibLauncher.class, args);
    }

    public static void launch(List<File> fileList, Class<?> mainClass, String[] args) throws Exception {
        List<URL> urlList = new ArrayList<>();
        for (File file : fileList) {
            loadClassPathsByFile(file, urlList);
        }

        for (URL url : urlList) {
            System.out.println(url);
        }

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ExtLibClassLoader classLoader = new ExtLibClassLoader(urlList.toArray(new URL[0]), contextClassLoader);
        Thread.currentThread().setContextClassLoader(classLoader);
        Method mainMethod = mainClass.getMethod("main", String[].class);
        mainMethod.invoke(null, (Object) args);
    }

    public static void loadClassPathsByFile(File file, Collection<URL> urlList) throws IOException {
        if (file == null) {
            return;
        }
        String name = file.getName();
        try {
            if (file.isFile()) {
                if (name.endsWith(".jar")) {
                    scanJarForNestedJars(file, urlList);
                }
            } else if (file.isDirectory()) {
                urlList.add(file.toURI().toURL());
                scanDirectoryForJars(file, urlList);
            }
        } catch (Exception e) {
            System.err.println("bad file:" + file);
            e.printStackTrace();
        }
    }

    public static void scanJarForNestedJars(File jarFile, Collection<URL> urlList) {
        if (jarFile == null) {
            return;
        }
        try (JarFile jar = new JarFile(jarFile)) {
            urlList.add(jarFile.toURI().toURL());
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                String name = entry.getName();
                if (name == null || name.isEmpty()) {
                    continue;
                }
                if (name.endsWith(".jar")) {
                    urlList.add(new URL("jar:" + jarFile.toURI().toURL() + "!/" + entry.getName()));
                }
            }
        } catch (Exception e) {
            System.err.println("bad jar:" + jarFile);
            e.printStackTrace();
        }
    }

    public static void scanDirectoryForJars(File directory, Collection<URL> urlList) {
        if (directory == null) {
            return;
        }
        try {
            if (!directory.isDirectory()) {
                return;
            }
            File[] files = directory.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                String name = file.getName();
                if (file.isDirectory()) {
                    try {
                        scanDirectoryForJars(file, urlList);
                    } catch (Exception e) {
                        System.err.println("bad directory:" + file);
                        e.printStackTrace();
                    }
                }
                if (!file.isFile()) {
                    continue;
                }
                if (name.endsWith(".jar")) {
                    try {
                        scanJarForNestedJars(file, urlList);
                    } catch (Exception e) {
                        System.err.println("bad jar:" + file);
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("bad directory:" + directory);
            e.printStackTrace();
        }

    }
}
