package i2f.ai.std.skill;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.context.std.IContext;
import i2f.io.stream.StreamUtil;
import i2f.os.OsUtil;
import i2f.os.data.CommandResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/3/23 14:11
 * @desc
 */
@Data
@NoArgsConstructor
@Tools(tags = {
        AiTags.SKILL_VALUE
})
public class SkillsTools {

    protected volatile IContext context;

    public SkillsTools(IContext context) {
        this.context = context;
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE,
                    AiTags.READONLY_VALUE,
            },
            description = "获取技能(skill)的文档")
    public String get_skill_document(@ToolParam(description = "技能名称，例如：search_website") String skillName) throws Exception {
        if (skillName == null || !skillName.matches("^[a-zA-Z0-9\\-_\\.]+$")) {
            throw new IllegalArgumentException("bad skillName accept");
        }
        URL url = null;
        for (String fileName : SkillsHelper.POSSIBLE_SKILL_FILE_NAME) {
            url = SkillsHelper.getSkillResource("skills/" + skillName + "/" + fileName);
            if (url != null) {
                if ("file".equalsIgnoreCase(url.getProtocol())) {
                    File file = new File(url.toURI());
                    if (file.exists() && file.isFile()) {
                        break;
                    }
                }
            }
        }
        if (url == null) {
            throw new IllegalStateException("skill [" + skillName + "] not found");
        }

        String text = StreamUtil.readString(url);
        return text;
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE,
                    AiTags.READONLY_VALUE,
            }, description = "获取技能(skill)中的资源或者脚本")
    public String get_skill_resource(@ToolParam(description = "技能名称，例如：search_website") String skillName,
                                     @ToolParam(description = "资源路径，例如：script/test.py") String resourcePath) throws Exception {
        if (skillName == null || !skillName.matches("^[a-zA-Z0-9\\-_\\.]+$")) {
            throw new IllegalArgumentException("bad skillName accept");
        }
        resourcePath = SkillsHelper.safeSkillResourcePath(resourcePath);

        URL url = SkillsHelper.getSkillResource("skills/" + skillName + "/" + resourcePath);
        if (url == null) {
            throw new IllegalStateException("skill [" + skillName + "] asset [" + resourcePath + "] not found");
        }
        if ("file".equalsIgnoreCase(url.getProtocol())) {
            File file = new File(url.toURI());
            if (!file.exists()) {
                throw new IllegalStateException("skill [" + skillName + "] asset file [" + resourcePath + "] not found");
            }
            if (!file.isFile()) {
                throw new IllegalStateException("skill [" + skillName + "] asset file [" + resourcePath + "] not is file");
            }
        }
        return StreamUtil.readString(url);
    }

    @Tool(
            tags = {
                    AiTags.EXECUTABLE_VALUE,
                    AiTags.HUMAN_VALUE,
                    AiTags.COMMAND_VALUE
            }, description = "执行技能(skill)中的命令行脚本"
    )
    public CommandResult run_skill_script(@ToolParam(description = "技能名称，例如：search_website") String skillName,
                                          @ToolParam(description = "脚本路径，例如：script/test.py") String scriptPath,
                                          @ToolParam(description = "脚本的命令行参数，例如：-o -l test.txt") List<String> commandArguments) throws Exception {
        if (skillName == null || !skillName.matches("^[a-zA-Z0-9\\-_\\.]+$")) {
            throw new IllegalArgumentException("bad skillName accept");
        }
        scriptPath = SkillsHelper.safeSkillResourcePath(scriptPath);

        if (commandArguments == null) {
            commandArguments = new ArrayList<>();
        }

        URL url = SkillsHelper.getSkillResource("skills/" + skillName + "/" + scriptPath);
        if (url == null) {
            throw new IllegalStateException("skill [" + skillName + "] script asset [" + scriptPath + "] not found");
        }
        File scriptFile = new File(url.toURI());
        if (!scriptFile.exists()) {
            throw new IllegalStateException("skill [" + skillName + "] script file [" + scriptPath + "] not found");
        }
        if (!scriptFile.isFile()) {
            throw new IllegalStateException("skill [" + skillName + "] script file [" + scriptPath + "] not is file");
        }
        return runScript(scriptFile,commandArguments);
    }

    public SkillScriptRunner getScriptRunner(String suffix) {
        if(context==null){
            return null;
        }
        try {
            List<SkillScriptRunner> beans = context.getBeans(SkillScriptRunner.class);
            for (SkillScriptRunner runner : beans) {
                if (Objects.equals(runner.suffix(), suffix)) {
                    return runner;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    public CommandResult runScript(File scriptFile,List<String> commandArguments) throws Exception {
        String name = scriptFile.getName();
        String suffix = "";
        int idx = name.lastIndexOf(".");
        if (idx >= 0) {
            suffix = name.substring(idx).toLowerCase();
        }

        SkillScriptRunner runner = getScriptRunner(suffix);
        if(runner!=null){
            return runner.runScript(scriptFile,commandArguments);
        }

        List<String> commandArr = new ArrayList<>();

        if (".py".equals(suffix)) {
            commandArr.add("python");
        } else if (".pl".equals(suffix)) {
            commandArr.add("perl");
        } else if (".js".equals(suffix)) {
            commandArr.add("node");
        } else if (OsUtil.isWindows()) {
            commandArr.add("cmd");
            commandArr.add("/c");
        } else {
            commandArr.add("sh");
        }
        commandArr.add(scriptFile.getName());
        commandArr.addAll(commandArguments);

        return OsUtil.execCmdForResult(true, TimeUnit.MINUTES.toSeconds(3),
                commandArr.toArray(new String[0]),
                null,
                new File(scriptFile.getAbsolutePath()).getParentFile(),
                OsUtil.getCmdCharset());
    }


}
