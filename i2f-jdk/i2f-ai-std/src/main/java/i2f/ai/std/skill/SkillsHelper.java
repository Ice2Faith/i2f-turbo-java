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

    public static Map<String, SkillDefinition> scanFileSystemSkills() {
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
            String text = null;
            try {
                text = StreamUtil.readString(mainFile);
            } catch (Exception e) {
                continue;
            }
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

    public static String convertSkillDefinitionsAsSystemPrompt(Map<String, SkillDefinition> skillMap) {
        if (skillMap == null || skillMap.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();

        builder.append(
                "## 技能(skill)设定\n" +
                        "- 你拥有以下技能(skill)；\n" +
                        "- 如果有需要使用这些技能的时候，使用下述的技能名称获取技能的详细教程(使用工具[getSkillDocument])；\n" +
                        "- 每一个技能都没有其他资源文件和脚本文件可供使用，也就是这两个工具（[getSkillAsset]和[runSkillCommand]）不可使用；\n" +
                        "- 除非这一个技能的详细教程中明确提及了，才会有相应的资源文件和脚本文件，才能够使用这两个工具；\n" +
                        "- 如果这个技能的教程中有明确提及到了其他资源文件，才能获取提及到的资源文件阅读(使用工具[getSkillAsset])，***没有明确提及的资源不能使用工具获取***；\n" +
                        "- 如果这个技能的教程中有明确提及需要运行的命令行脚本，才能在命令行运行提及到的脚本（使用工具[runSkillCommand]），***没有明确提及的脚本不能使用工具运行***；\n" +
                        "- ***特别注意***，技能(skill)和工具(tool)不一样；\n" +
                        "-***技能只能使用上面的三个工具进行调用***，***不允许获取教程中未明确提及的资源***，***不允许执行教程中未明确提及的脚本***；\n" +
                        ""
        ).append("\n");

        builder.append("### 技能(skill)列表").append("\n");
        for (Map.Entry<String, SkillDefinition> entry : skillMap.entrySet()) {
            SkillDefinition definition = entry.getValue();
            builder.append("#### ").append("技能名称：").append(definition.getName()).append("\n")
                    .append("- ").append("技能描述：").append(definition.getDescription()).append("\n")
                    .append("\n");
        }

        return builder.toString();
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
            if (!item.matches("^[a-zA-Z0-9\\-_\\.]+$")) {
                throw new IllegalArgumentException("bad resourcePath accept!");
            }
            parts.add(item);
        }
        return String.join("/", parts);
    }
}
