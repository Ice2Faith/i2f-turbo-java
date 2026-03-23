package i2f.ai.std.skill;

import i2f.io.stream.StreamUtil;
import i2f.resources.ResourceUtil;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/3/23 19:30
 * @desc
 */
public class SkillsHelper {
    public static String[] POSSIBLE_SKILL_FILE_NAME = {
            "SKILL.md", "skill.md",
            "index.md",
            "README.md", "readme.md",
    };

    public static Map<String, SkillDefinition> scanFileSystemSkills() throws Exception {
        Map<String, SkillDefinition> ret = new LinkedHashMap<>();
        File dir = new File("./skills");
        File[] files = dir.listFiles();
        if (files == null) {
            return ret;
        }
        for (File file : files) {
            if (file.isFile()) {
                continue;
            }
            File[] children = file.listFiles();
            if (children == null) {
                continue;
            }
            File mainFile = null;
            for (File child : children) {
                if (child.isFile()) {
                    for (String item : POSSIBLE_SKILL_FILE_NAME) {
                        if (item.equals(child.getName())) {
                            if (child.length() > 0) {
                                mainFile = child;
                                break;
                            }
                        }
                    }
                    if (mainFile != null) {
                        break;
                    }
                }
            }
            if (mainFile == null) {
                continue;
            }
            String text = StreamUtil.readString(mainFile);
            String[] arr = text.split("[\\-]{3,}", 3);
            SkillDefinition definition = new SkillDefinition();
            for (String block : arr) {
                String[] lines = block.split("\n");
                for (String line : lines) {
                    String[] pair = line.split(":", 2);
                    if (pair.length != 2) {
                        continue;
                    }
                    String attrName = pair[0].trim();
                    String attrValue = pair[1].trim();
                    if ("name".equalsIgnoreCase(attrName)) {
                        definition.setName(attrValue);
                    } else if ("description".equalsIgnoreCase(attrName)) {
                        definition.setDescription(attrValue);
                    } else if ("tags".equalsIgnoreCase(attrName)) {
                        List<String> tags = new ArrayList<>();
                        String[] tagArr = attrValue.split(",");
                        for (String tag : tagArr) {
                            tag = tag.trim();
                            if (tag.isEmpty()) {
                                continue;
                            }
                            tags.add(tag);
                        }
                        definition.setTags(tags);
                    } else if ("version".equalsIgnoreCase(attrName)) {
                        definition.setVersion(attrValue);
                    } else if ("author".equalsIgnoreCase(attrName)) {
                        definition.setAuthor(attrValue);
                    }
                }
                if (definition.getName() != null && definition.getDescription() != null) {
                    break;
                }
            }
            if (definition.getName() != null && definition.getDescription() != null) {
                ret.put(definition.getName(), definition);
            }
        }
        return ret;
    }

    public static URL getSkillResource(String path) throws Exception {
        URL url = ResourceUtil.getResource(path);
        if (url != null) {
            return url;
        }
        return ResourceUtil.getClasspathResource(path);
    }

    public static String safeSkillResourcePath(String resourcePath) {
        if (resourcePath == null || resourcePath.isEmpty()) {
            throw new IllegalArgumentException("bad resourcePath accept because of it is empty!");
        }
        resourcePath = resourcePath.replace("\\", "/");
        LinkedList<String> parts = new LinkedList<>();
        String[] arr = resourcePath.split("/");
        for (String item : arr) {
            if (".".equals(item)) {
                continue;
            }
            if ("-".equals(item)) {
                continue;
            }
            if ("..".equals(item)) {
                parts.removeLast();
            }
            if (item.isEmpty()) {
                continue;
            }
            if (!item.matches("^[a-zA-Z0-9\\-_]$")) {
                throw new IllegalArgumentException("bad resourcePath accept!");
            }
            parts.add(item);
        }
        return String.join("/", parts);
    }
}
