package i2f.launcher;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

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

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
            String path = name.replaceAll("\\.", "/") + ".class";
            byte[] bytes = getBytesByResourcePath(path);
            if (bytes != null) {
                return defineClass(name, bytes, 0, bytes.length);
            }
        }
        throw new ClassNotFoundException(name);
    }

    public byte[] getBytesByResourcePath(String path) {
        for (URL url : getURLs()) {
            String str = url.toString();
            if (str.startsWith("file://")) {
                File file = new File(str.substring("file://".length()));
                if (file.isFile()) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".jar")) {
                        byte[] bytes = getBytesInJarFile(file, path);
                        return bytes;
                    }
                } else if (file.isDirectory()) {
                    File classFile = new File(file, path);
                    if (classFile.isFile()) {
                        try {
                            byte[] bytes = getBytesInFile(classFile);
                            return bytes;
                        } catch (IOException ex) {

                        }
                    }
                }
            } else if (str.startsWith("jar:file://")) {
                String filePath = str.substring("jar:file://".length());
                int idx = filePath.indexOf("!");
                if (idx >= 0) {
                    filePath = filePath.substring(0, idx);
                }
                File jarFile = new File(filePath);
                byte[] bytes = getBytesInJarFile(jarFile, path);
                return bytes;
            }
        }
        return null;
    }

    public byte[] getBytesInStream(InputStream is, boolean close) throws IOException {
        if (is == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buff = new byte[2048];
        int len = 0;
        while ((len = is.read(buff)) > 0) {
            bos.write(buff, 0, len);
        }
        if (close) {
            is.close();
        }
        return bos.toByteArray();
    }

    public byte[] getBytesInFile(File file) throws IOException {
        if (file == null) {
            return null;
        }
        if (!file.isFile()) {
            return null;
        }
        InputStream is = new FileInputStream(file);
        return getBytesInStream(is, true);
    }

    public byte[] getBytesInJarFile(File jarFile, String searchName) {
        if (jarFile == null) {
            return null;
        }
        if (!jarFile.isFile()) {
            return null;
        }

        try (JarFile jar = new JarFile(jarFile)) {
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
                if (name.equals(searchName)) {
                    InputStream is = jar.getInputStream(entry);
                    return getBytesInStream(is, true);
                }

                if (name.endsWith(".jar")) {
                    JarInputStream jis = new JarInputStream(jar.getInputStream(entry));
                    byte[] bytes = getBytesInJarInputStream(jis, searchName);
                    jis.close();
                    return bytes;
                }
            }
        } catch (Exception e) {

        }
        return null;
    }

    public byte[] getBytesInJarInputStream(JarInputStream jis, String searchName) throws IOException {
        if (jis == null) {
            return null;
        }
        JarEntry nextJarEntry = null;
        while ((nextJarEntry = jis.getNextJarEntry()) != null) {
            if (nextJarEntry.isDirectory()) {
                continue;
            }
            String name = nextJarEntry.getName();
            if (name == null || name.isEmpty()) {
                continue;
            }
            try {
                if (name.equals(searchName)) {
                    return getBytesInStream(jis, false);
                }
                if (name.endsWith(".jar")) {
                    byte[] bytes = getBytesInJarInputStream(jis, searchName);
                    return bytes;
                }
            } finally {
                jis.closeEntry();
            }
        }
        return null;
    }

    public Set<URL> findResourcesInJarInputStream(String name, String parent, JarInputStream jis, boolean matchOne) {

    }

    public Set<URL> findResourcesInJarFile(String name, String parent, File file, boolean matchOne) {
        Set<URL> ret = new LinkedHashSet<>();
        if (file == null) {
            return ret;
        }
        if (!file.isFile()) {
            return ret;
        }
        try (JarFile jar = new JarFile(file)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName == null) {
                    continue;
                }
                if (entryName.equals(name)) {
                    ret.add(new URL(parent + "!" + entryName));
                    if (matchOne) {
                        return ret;
                    }
                }
                if (entryName.endsWith(".jar")) {
                    JarInputStream jis = new JarInputStream(jar.getInputStream(entry));
                    Set<URL> next = findResourcesInJarInputStream(name, parent + "!" + entryName, jis, matchOne);
                    jis.close();
                    ret.addAll(next);
                    if (matchOne && !ret.isEmpty()) {
                        return ret;
                    }
                }
            }
        } catch (Exception e) {

        }
        return ret;
    }

    public Set<URL> findResources(String name, boolean matchOne) throws IOException {
        Set<URL> ret = new LinkedHashSet<>();
        URL[] urls = getURLs();
        for (URL url : urls) {
            String str = url.toString();
            if (str.startsWith("file://")) {
                File file = new File(str.substring("file://".length()), name);
                if (file.exists()) {
                    ret.add(file.toURI().toURL());
                    if (matchOne) {
                        return ret;
                    }
                }
            } else if (str.startsWith("jar:file://")) {
                String filePath = str.substring("jar:file://".length());
                int idx = filePath.indexOf("!");
                if (idx >= 0) {
                    filePath = filePath.substring(0, idx);
                }
                File jarFile = new File(filePath);
                Set<URL> next = findResourcesInJarFile(name, "jar:file://" + filePath, jarFile, matchOne);
                ret.addAll(next);
                if (matchOne && !ret.isEmpty()) {
                    return ret;
                }
            }
        }
        return ret;
    }

    @Override
    public URL findResource(String name) {
        URL url = super.findResource(name);
        if (url != null) {
            return url;
        }
        try {
            Set<URL> set = findResources(name, true);
            if (!set.isEmpty()) {
                return set.iterator().next();
            }
        } catch (IOException e) {

        }
        return null;
    }

    @Override
    public Enumeration<URL> findResources(String name) throws IOException {
        Enumeration<URL> ret = super.findResources(name);
        Set<URL> set = new LinkedHashSet<>();
        while (ret.hasMoreElements()) {
            set.add(ret.nextElement());
        }
        Set<URL> next = findResources(name, false);
        set.addAll(next);
        return Collections.enumeration(set);
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        InputStream is = super.getResourceAsStream(name);
        if (is != null) {
            return is;
        }
        byte[] bytes = getBytesByResourcePath(name);
        if (bytes != null) {
            return new ByteArrayInputStream(bytes);
        }
        return null;
    }
}
