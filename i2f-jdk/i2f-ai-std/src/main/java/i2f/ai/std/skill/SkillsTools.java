package i2f.ai.std.skill;

import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.io.stream.StreamUtil;
import i2f.os.OsUtil;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/3/23 14:11
 * @desc
 */
@Tools
public class SkillsTools {

    @Tool(description = "获取技能(skill)的详细教程")
    public String getSkillDocument(@ToolParam(description = "技能名称，例如：search_website") String skillName) throws Exception {
        if (skillName == null || !skillName.matches("^[a-zA-Z0-9\\-_\\.]+$")) {
            throw new IllegalArgumentException("bad skillName accept");
        }
        URL url = null;
        for (String fileName : SkillsHelper.POSSIBLE_SKILL_FILE_NAME) {
            url = SkillsHelper.getSkillResource("skills/" + skillName + "/" + fileName);
            if (url != null) {
                if("file".equalsIgnoreCase(url.getProtocol())){
                    File file = new File(url.toURI());
                    if(file.exists() && file.isFile()){
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

    @Tool(description = "获取技能(skill)中的其他资源文件")
    public String getSkillAsset(@ToolParam(description = "技能名称，例如：search_website") String skillName,
                                @ToolParam(description = "资源路径，例如：script/test.py") String resourcePath) throws Exception {
        if (skillName == null || !skillName.matches("^[a-zA-Z0-9\\-_\\.]+$")) {
            throw new IllegalArgumentException("bad skillName accept");
        }
        resourcePath = SkillsHelper.safeSkillResourcePath(resourcePath);

        URL url = SkillsHelper.getSkillResource("skills/" + skillName + "/" + resourcePath);
        if (url == null) {
            throw new IllegalStateException("skill [" + skillName + "] asset [" + resourcePath + "] not found");
        }
        if("file".equalsIgnoreCase(url.getProtocol())){
            File file = new File(url.toURI());
            if(!file.exists()){
                throw new IllegalStateException("skill [" + skillName + "] asset file [" + resourcePath + "] not found");
            }
            if(!file.isFile()){
                throw new IllegalStateException("skill [" + skillName + "] asset file [" + resourcePath + "] not is file");
            }
        }
        return StreamUtil.readString(url);
    }

    @Tool(description = "执行技能(skill)中的命令行脚本")
    public String runSkillCommand(@ToolParam(description = "技能名称，例如：search_website") String skillName,
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
        if(!scriptFile.exists()){
            throw new IllegalStateException("skill [" + skillName + "] script file [" + scriptPath + "] not found");
        }
        if(!scriptFile.isFile()){
            throw new IllegalStateException("skill [" + skillName + "] script file [" + scriptPath + "] not is file");
        }
        String name = scriptFile.getName();
        String suffix = "";
        int idx = name.lastIndexOf(".");
        if (idx >= 0) {
            suffix = name.substring(idx).toLowerCase();
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

        return OsUtil.execCmd(true, TimeUnit.MINUTES.toSeconds(3),
                commandArr.toArray(new String[0]),
                null,
                new File(scriptFile.getAbsolutePath()).getParentFile(),
                OsUtil.getCmdCharset());
    }


}
