package i2f.turbo.idea.plugin.inject.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import i2f.io.stream.StreamUtil;
import i2f.lru.LruMap;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectItem;
import i2f.turbo.idea.plugin.inject.utils.JsonUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/4/16 11:24
 * @desc
 */
public class ProjectInjectConfig {

    protected static final LruMap<String, Map.Entry<Long, List<LanguageInjectItem>>> CACHE_INJECT_CONFIG = new LruMap<>(30);
    /**
     * 按 type 再缓存一层：注入器会对「每一个 XML 属性值 / YAML 标量 / JSON 字符串」
     * 调用本方法，原来的 1.2s TTL + 每次 stream 过滤会产生大量重复 I/O 与临时对象。
     */
    protected static final LruMap<String, Map.Entry<Long, List<LanguageInjectItem>>> CACHE_INJECT_CONFIG_TYPE = new LruMap<>(60);
    protected static final long CACHE_TTL_MS = 30_000L;

    public static List<LanguageInjectItem> getProjectInjectConfigForType(Project project, String type) {
        if (type == null) {
            return new ArrayList<>();
        }
        String key = project.getProjectFilePath() + "#" + type;
        Map.Entry<Long, List<LanguageInjectItem>> cached = CACHE_INJECT_CONFIG_TYPE.get(key);
        if (cached != null && cached.getKey() >= System.currentTimeMillis()) {
            return cached.getValue();
        }
        List<LanguageInjectItem> list = getProjectInjectConfig(project).stream()
                .filter(e -> type.equalsIgnoreCase(e.getType()))
                .collect(Collectors.toList());
        CACHE_INJECT_CONFIG_TYPE.put(key, new AbstractMap.SimpleEntry<>(System.currentTimeMillis() + CACHE_TTL_MS, list));
        return list;
    }

    public static List<LanguageInjectItem> getProjectInjectConfig(Project project) {
        String projectFilePath = project.getProjectFilePath();
        Map.Entry<Long, List<LanguageInjectItem>> entry = CACHE_INJECT_CONFIG.get(projectFilePath);
        if (entry != null && entry.getKey() >= System.currentTimeMillis()) {
            return new ArrayList<>(entry.getValue());
        }
        List<LanguageInjectItem> ret = getProjectInjectConfigDirect(projectFilePath);
        CACHE_INJECT_CONFIG.put(projectFilePath, new AbstractMap.SimpleEntry<>(System.currentTimeMillis() + CACHE_TTL_MS, new ArrayList<>(ret)));
        return ret;
    }

    public static List<LanguageInjectItem> getProjectInjectConfigDirect(String projectFilePath) {
        List<LanguageInjectItem> ret = new ArrayList<>();
        try {

            if (projectFilePath == null) {
                return ret;
            }

            File file = new File(projectFilePath);
            String absolutePath = file.getAbsolutePath();
            int idx = absolutePath.lastIndexOf(".idea");
            if (idx >= 0) {
                file = new File(absolutePath.substring(0, idx));
            }
            file = getConfigFile(file);

            if (!file.exists()) {
                return ret;
            }

            if (!file.isFile()) {
                return ret;
            }

            String configJson = StreamUtil.readString(file);
            ret = JsonUtils.parseJson(configJson, new TypeReference<List<LanguageInjectItem>>() {
            });

            return ret;
        } catch (Throwable e) {

        }
        return ret;
    }

    public static File getConfigFile(File baseDir) {
        String[] searchFiles = {
                "language-inject-template.jsonc",
                "language-inject-template.json",
                ".idea/language-inject-template.jsonc",
                ".idea/language-inject-template.json"
        };
        for (String searchFile : searchFiles) {
            File ret = new File(baseDir, searchFile);
            if (ret.exists() && ret.isFile()) {
                return ret;
            }
        }
        return new File(baseDir, "language-inject-template.json");
    }


    public static Language matchLanguage(String name) {
        Language lang = Language.findLanguageByID(name);
        if (lang != null) {
            return lang;
        }
        Collection<Language> list = Language.getRegisteredLanguages();

        if ("velocity".equalsIgnoreCase(name)) {
            name = "vtl";
        }

        name = name.toLowerCase();

        Language contains = null;
        for (Language item : list) {
            String id = item.getID();
            if (id.equalsIgnoreCase(name)) {
                return item;
            }
            if (id.toLowerCase().contains(name)) {
                contains = item;
            }
        }
        return contains;
    }
}
