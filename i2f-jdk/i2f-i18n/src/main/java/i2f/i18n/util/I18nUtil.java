package i2f.i18n.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/9/30 22:09
 * @desc
 */
public class I18nUtil {
    public static final String[] DEFAULT_TYPES = {
            "classpath:",
            "file:"
    };
    public static final String[] DEFAULT_DIRECTORIES = {
            "i18n/",
            "resources/",
            "config/",
            "lang/"
    };
    public static final String[] DEFAULT_FILENAMES = {
            "i18n",
            "message",
            "string"
    };

    public static List<String> cartesianProduct(List<List<String>> tables) {
        List<String> left = Collections.singletonList("");
        for (List<String> right : tables) {
            left = cartesianProductNext(left, right);
        }
        return left;
    }

    public static List<String> cartesianProductNext(List<String> left, List<String> right) {
        List<String> ret = new ArrayList<>();
        for (String ls : left) {
            if (ls == null) {
                ls = "";
            }
            for (String rs : right) {
                if (rs == null) {
                    rs = "";
                }
                ret.add(ls.concat(rs));
            }
        }
        return ret;
    }

    public static String firstNotEmpty(String... strs) {
        for (String str : strs) {
            if (str != null && !str.isEmpty()) {
                return str;
            }
        }
        return null;
    }

    public static String trimLeft(String str) {
        if (str == null) {
            return null;
        }
        int i = 0;
        while (i < str.length() && Character.isWhitespace(str.charAt(i))) {
            i++;
        }
        return str.substring(i);
    }

    public static InputStream getFileInputStream(String searchName) {
        List<InputStream> list = new ArrayList<>(1);
        getFileInputStream(list, searchName);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public static List<InputStream> getFileInputStream(List<InputStream> inputs,
                                                       String searchName) {
        if (searchName == null || searchName.isEmpty()) {
            return inputs;
        }
        try {
            File file = new File(searchName);
            if (file.exists()) {
                InputStream is = new FileInputStream(file);
                inputs.add(is);
            }
        } catch (Exception e) {

        }
        return inputs;
    }

    public static InputStream getClasspathInputStreams(String searchName,
                                                       ClassLoader classLoader) {
        List<InputStream> list = new ArrayList<>(1);
        getClasspathInputStreams(list, searchName, classLoader, true);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public static List<InputStream> getClasspathInputStreams(List<InputStream> inputs,
                                                             String searchName,
                                                             ClassLoader classLoader,
                                                             boolean matchOne) {
        if (searchName == null || searchName.isEmpty()) {
            return inputs;
        }
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        try {
            Enumeration<URL> resources = classLoader.getResources(searchName);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                if (url != null) {
                    try {
                        InputStream is = url.openStream();
                        if (is != null) {
                            inputs.add(is);
                            if (matchOne) {
                                return inputs;
                            }
                        }
                    } catch (IOException e) {

                    }
                }
            }
        } catch (IOException e) {

        }
        return inputs;
    }
}
