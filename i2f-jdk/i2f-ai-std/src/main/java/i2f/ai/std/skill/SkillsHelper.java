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
                        Set<String> tags = new LinkedHashSet<>();
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
                "# 技能系统\n" +
                        "\n" +
                        "- 技能系统，你具有一系列的技能，根据需要使用合适的技能帮助完成目标\n" +
                        "- 技能包含基础的技能文档，以及可能包含的技能资源、技能脚本\n" +
                        "- 当你需要使用某个技能时，需要使用 `get_skill_document` 工具先获取技能的文档\n" +
                        "- 技能文档会指导你如何使用这个技能完成目标\n" +
                        "- 技能文档中，可能还会提及到技能附加的资源或者脚本\n" +
                        "- 如果需要阅读这些附加的资源或者脚本，需要使用 `get_skill_resource` 工具获取内容进行阅读\n" +
                        "- 如果技能中需要执行技能中的脚本，需要使用 `run_skill_script` 工具进行运行脚本\n" +
                        "\n" +
                        "## 使用规范\n" +
                        "\n" +
                        "- 技能(skill)是一个独立的体系，只能使用 `get_skill_document`/ `get_skill_resource`/  `run_skill_script` 三个工具进行交互\n" +
                        "- 技能是基于工具(tool)实现的，不要把技能与工具进行混淆\n" +
                        "- 其他工具不能够与技能进行交互，因为设计上不支持\n" +
                        "- 每个技能是互相隔离的，因此三个工具调用时，都需要带上技能名称\n" +
                        "- 每个技能都是特殊设计的，因此是严谨的，只有技能文档中提及的资源或者脚本才是真实存在的，没有提及是不存在的\n" +
                        "- 因此，不要猜测、假设任何技能中存在某些资源或者脚本，应该严格遵守文档内容" +
                        ""
        ).append("\n");

        builder.append("## 技能(skill)定义列表").append("\n");
        for (Map.Entry<String, SkillDefinition> entry : skillMap.entrySet()) {
            SkillDefinition definition = entry.getValue();
            builder.append("### ").append("技能名称：").append(definition.getName()).append("\n")
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
