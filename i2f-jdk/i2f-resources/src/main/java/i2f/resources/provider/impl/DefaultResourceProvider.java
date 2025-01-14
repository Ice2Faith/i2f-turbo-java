package i2f.resources.provider.impl;


import i2f.io.stream.StreamUtil;
import i2f.resources.provider.ResourceProvider;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * classpath:/resources/assets/string.properties
 * app.name=test --> getString("app.name")/ get("app.name")
 * classpath:/resources/assets/bundle/bg.mp3 --> getBundle("bg",",mp3)
 * classpath:/resources/assets/bundle/bg.jpg --> getBundle("bg",".jpg)
 * classpath:/resources/assets/bundle/banner.txt --> getString("banner") / getBundle("banner",".txt")
 *
 * @author Ice2Faith
 * @date 2025/1/14 11:10
 */
public class DefaultResourceProvider implements ResourceProvider {
    public static final DefaultResourceProvider INSTANCE = new DefaultResourceProvider();

    public static final String ROOT_PATH = "assets";
    public static final String BUNDLE_PATH = "bundle";
    public static final String[] DEFAULT_SEARCH_PATHS = {
            "classpath:resources",
            "classpath:",
            "./resources",
            ".",
            "../resources",
            ".."
    };
    public static final String[] DEFAULT_SEARCH_FILES = {
            "assets.properties",
            "resources.properties",
            "string.properties"
    };
    public static final String[] DEFAULT_STRING_SUFFIXES = {
            ".txt",
            ".xml",
            ".json",
            ".html"
    };

    public List<String> getSearchFiles() {
        List<String> ret = new ArrayList<>();
        for (String path : DEFAULT_SEARCH_PATHS) {
            for (String file : DEFAULT_SEARCH_FILES) {
                ret.add(path + "/" + ROOT_PATH + "/" + file);
            }
        }
        return ret;
    }

    public List<String> getSearchBundleFiles(String id, String... suffixes) {
        List<String> ret = new ArrayList<>();
        for (String path : DEFAULT_SEARCH_PATHS) {
            for (String suffix : suffixes) {
                if (suffix == null) {
                    suffix = "";
                }
                ret.add(path + "/" + ROOT_PATH + "/" + BUNDLE_PATH + "/" + id + suffix);
            }

        }
        return ret;
    }

    @Override
    public InputStream get(String id) {
        String val = getString(id);
        if (val != null) {
            return new ByteArrayInputStream(val.getBytes(StandardCharsets.UTF_8));
        }

        InputStream is = getBundle(id, null);
        if (is != null) {
            return is;
        }
        return null;
    }

    @Override
    public String getString(String id) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        List<String> list = getSearchFiles();
        for (String item : list) {
            if (item == null || item.isEmpty()) {
                continue;
            }
            if (item.startsWith("classpath:")
                    || item.startsWith("classpath*:")) {
                try {
                    String name = item.substring(item.indexOf(":") + 1);
                    if (name.startsWith("/")) {
                        name = name.substring(1);
                    }
                    Enumeration<URL> enums = loader.getResources(name);
                    while (enums.hasMoreElements()) {
                        URL url = enums.nextElement();
                        try (InputStream stream = url.openStream()) {
                            Properties properties = new Properties();
                            properties.load(stream);
                            String val = properties.getProperty(id);
                            if (val != null) {
                                return val;
                            }
                        } catch (Exception e) {

                        }
                    }
                } catch (Exception e) {

                }
            } else {
                File file = new File(item);
                if (file.exists() && file.isFile()) {
                    try (InputStream stream = new FileInputStream(file)) {
                        Properties properties = new Properties();
                        properties.load(stream);
                        String val = properties.getProperty(id);
                        if (val != null) {
                            return val;
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }

        InputStream is = null;
        try {
            is = getBundle(id, DEFAULT_STRING_SUFFIXES);
            if (is != null) {
                String val = StreamUtil.readString(is, StandardCharsets.UTF_8.name(), true);
                return val;
            }
        } catch (Exception e) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {

                }
            }
        }


        return null;
    }

    @Override
    public InputStream getBundle(String id, String... suffixes) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        List<String> bundleFiles = getSearchBundleFiles(id, suffixes);
        for (String item : bundleFiles) {
            if (item == null || item.isEmpty()) {
                continue;
            }
            if (item.startsWith("classpath:")
                    || item.startsWith("classpath*:")) {
                try {
                    String name = item.substring(item.indexOf(":") + 1);
                    if (name.startsWith("/")) {
                        name = name.substring(1);
                    }
                    Enumeration<URL> enums = loader.getResources(name);
                    while (enums.hasMoreElements()) {
                        URL url = enums.nextElement();
                        return url.openStream();
                    }
                } catch (Exception e) {

                }
            } else {
                File file = new File(item);
                if (file.exists() && file.isFile()) {
                    try {
                        return new FileInputStream(file);
                    } catch (Exception e) {

                    }
                }
            }
        }
        return null;
    }
}
