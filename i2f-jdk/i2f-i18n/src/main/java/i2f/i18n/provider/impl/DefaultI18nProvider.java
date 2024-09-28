package i2f.i18n.provider.impl;

import i2f.i18n.I18n;
import i2f.i18n.data.I18nItem;
import i2f.i18n.parser.I18nParser;
import i2f.i18n.parser.impl.PropertiesI18nParser;
import i2f.i18n.parser.impl.XmlI18nParser;
import i2f.i18n.provider.I18nProvider;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 按照规则从默认的i18n配置文件中加载i18n配置
 * 并提供读取i18n配置的get方法
 * 默认加载的文件列表请查看DefaultI18nProvider.DEFAULT_FILE_NAMES中的内容
 * 例如：i18n/i18n-%s.properties
 * 则表示从%s为占位符的语言的i18n配置文件
 * 例如语言为zh-CN
 * 则对应的加载文件为：i18n/i18n-zh-CN.properties
 * get获取值的查找顺序为：传入参数.lang>I18n.getThreadLang()>I18n.DEFAULT_LANG
 * 也就是：传入lang>线程lang>全局默认lang
 * 所以，默认情况下
 * 传入lang>线程lang>default
 * 所以，你可以不传入lang
 * 同时，你可以使用I18n.setThreadLang()来设置线程lang
 * 如果你使用在web项目中，则可以摘过滤器获取客户端的lang来设置线程lang以实现多语言支持
 *
 * @author Ice2Faith
 * @date 2024/9/28 15:25
 */
@Data
@NoArgsConstructor
public class DefaultI18nProvider implements I18nProvider {
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
    public static final String[] DEFAULT_SUFFIXES = {
            ".properties",
            ".xml"
    };
    public static final String[] DEFAULT_FILE_NAMES = cartesianProduct(
            Arrays.asList(
                    Arrays.asList(DEFAULT_TYPES),
                    Arrays.asList(DEFAULT_DIRECTORIES),
                    Arrays.asList(DEFAULT_FILENAMES),
                    Collections.singletonList("-%s"),
                    Arrays.asList(DEFAULT_SUFFIXES)
            )
    ).toArray(new String[0]);
    protected CopyOnWriteArrayList<String> fileNames = new CopyOnWriteArrayList<>(Arrays.asList(DEFAULT_FILE_NAMES));
    protected ConcurrentHashMap<String, ConcurrentHashMap<String, String>> cacheMap = new ConcurrentHashMap<>();

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

    public Map<String, String> getLangMap(String lang) {
        if (lang == null) {
            lang = I18n.getThreadLang();
        }
        if (lang == null) {
            lang = I18n.DEFAULT_LANG;
        }
        Map<String, String> map = cacheMap.get(lang);
        if (map != null) {
            return map;
        }
        loadLangMap(lang);
        map = cacheMap.get(lang);
        if (map == null) {
            map = cacheMap.get(I18n.DEFAULT_LANG);
        }
        return map;
    }


    public void loadLangMap(String lang) {
        if (lang == null) {
            return;
        }
        synchronized (String.valueOf(Math.abs(lang.hashCode())%32).intern()) {
            List<I18nItem> items = new ArrayList<>();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            for (String fileName : fileNames) {
                boolean isClasspath = true;
                int idx = fileName.indexOf(":");
                if (idx >= 0) {
                    String prefix = fileName.substring(0, idx);
                    fileName = fileName.substring(idx + 1);
                    if ("classpath".equalsIgnoreCase(prefix)
                            || "classpath*".equalsIgnoreCase(prefix)) {
                        isClasspath = true;
                    } else if ("file".equalsIgnoreCase(prefix)) {
                        isClasspath = false;
                    } else {
                        continue;
                    }
                }
                fileName = String.format(fileName, lang);
                boolean isProperties = true;
                idx = fileName.lastIndexOf(".");
                if (idx >= 0) {
                    String suffix = fileName.substring(idx + 1);
                    if ("properties".equalsIgnoreCase(suffix)) {
                        isProperties = true;
                    } else if ("xml".equalsIgnoreCase(suffix)) {
                        isProperties = false;
                    } else {
                        continue;
                    }
                }
                List<InputStream> inputs = new ArrayList<>();
                List<String> searchNames = Collections.singletonList(fileName);
                if (I18n.DEFAULT_LANG.equals(lang)) {
                    searchNames = Arrays.asList(fileName, fileName.replace("-" + I18n.DEFAULT_LANG, ""));
                }
                for (String searchName : searchNames) {
                    if (isClasspath) {
                        try {
                            Enumeration<URL> resources = classLoader.getResources(searchName);
                            while (resources.hasMoreElements()) {
                                URL url = resources.nextElement();
                                if (url != null) {
                                    try {
                                        InputStream is = url.openStream();
                                        if (is != null) {
                                            inputs.add(is);
                                        }
                                    } catch (IOException e) {

                                    }
                                }
                            }
                        } catch (IOException e) {

                        }
                    } else {
                        try {
                            File file = new File(searchName);
                            if (file.exists()) {
                                InputStream is = new FileInputStream(file);
                                inputs.add(is);
                            }
                        } catch (Exception e) {

                        }
                    }
                }
                for (InputStream input : inputs) {
                    try {
                        if (isProperties) {
                            I18nParser parser = new PropertiesI18nParser(lang, input);
                            Collection<I18nItem> list = parser.parse();
                            items.addAll(list);
                        } else {
                            I18nParser parser = new XmlI18nParser(lang, input);
                            Collection<I18nItem> list = parser.parse();
                            items.addAll(list);
                        }
                    } catch (Exception e) {

                    }
                }
            }

            for (I18nItem item : items) {
                String itemLang = item.getLang();
                String itemName = item.getName();
                String itemValue = item.getValue();
                if (itemName == null) {
                    continue;
                }
                if (itemValue == null) {
                    continue;
                }
                if (itemLang == null) {
                    itemLang = lang;
                }
                ConcurrentHashMap<String, String> langMap = cacheMap.computeIfAbsent(itemLang, (langKey) -> {
                    return new ConcurrentHashMap<>();
                });
                langMap.computeIfAbsent(itemName, (nameKey) -> itemValue);
            }
            cacheMap.computeIfAbsent(lang, (langKey) -> {
                return new ConcurrentHashMap<>();
            });
        }
    }

    @Override
    public String get(String lang, String name) {
        if (name == null) {
            return null;
        }
        Map<String, String> langMap = getLangMap(lang);
        if (langMap != null) {
            String ret = langMap.get(name);
            if (ret != null) {
                return ret;
            }
        }
        langMap = getLangMap(I18n.DEFAULT_LANG);
        if (langMap != null) {
            String ret = langMap.get(name);
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }
}
