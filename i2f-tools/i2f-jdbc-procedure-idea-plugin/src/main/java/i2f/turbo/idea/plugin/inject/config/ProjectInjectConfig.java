package i2f.turbo.idea.plugin.inject.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import i2f.lru.LruMap;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectItem;
import i2f.turbo.idea.plugin.inject.utils.JsonUtils;
import i2f.turbo.idea.plugin.inject.utils.StreamUtil;

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

    public static List<LanguageInjectItem> getProjectInjectConfigForType(Project project,String type){
        List<LanguageInjectItem> list = getProjectInjectConfig(project);
        return list.stream().filter(e->e.getType().equalsIgnoreCase(type)).collect(Collectors.toList());
    }

    public static List<LanguageInjectItem> getProjectInjectConfig(Project project) {
        String projectFilePath = project.getProjectFilePath();
        Map.Entry<Long, List<LanguageInjectItem>> entry = CACHE_INJECT_CONFIG.get(projectFilePath);
        if (entry != null && entry.getKey() >= System.currentTimeMillis()) {
            return new ArrayList<>(entry.getValue());
        }
        List<LanguageInjectItem> ret = getProjectInjectConfigDirect(projectFilePath);
        CACHE_INJECT_CONFIG.put(projectFilePath, new AbstractMap.SimpleEntry<>(System.currentTimeMillis() + 1200, new ArrayList<>(ret)));
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
        } catch (Exception e) {

        }
        return ret;
    }

    public static File getConfigFile(File baseDir){
        File ret = new File(baseDir, "language-inject-template.jsonc");
        if(ret.exists() && ret.isFile()){
            return ret;
        }
        ret = new File(baseDir, "language-inject-template.json");
        return ret;
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
