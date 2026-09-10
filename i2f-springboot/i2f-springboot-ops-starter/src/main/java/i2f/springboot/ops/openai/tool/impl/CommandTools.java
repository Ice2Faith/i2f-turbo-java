package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.ai.std.tool.intent.ToolIntent;
import i2f.ai.std.tool.intent.ToolIntentItem;
import i2f.os.OsUtil;
import i2f.os.data.CommandResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/6/22 16:38
 * @desc
 */
@ToolIntent(items = @ToolIntentItem(value=CommandTools.TOOL_INTENT_VALUE,description = CommandTools.TOOL_INTENT_DESCRIPTION))
@ConditionalOnExpression("${ai.tools.command.enable:false}")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Tools
public class CommandTools {
    public static final String TOOL_INTENT_VALUE="command";
    public static final String TOOL_INTENT_DESCRIPTION="提供命令行执行、操作系统类型判断";

    @Autowired(required = false)
    private LocalFileTools localFileTools;

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE,
                    AiTags.READONLY_VALUE,
                    AiTags.OS_VALUE
            }, description = "get current os type"
    )
    public String get_os_type() {
        String ret = "";
        if (OsUtil.isWindows()) {
            ret += "Windows";
        } else if (OsUtil.isLinux()) {
            ret += "Linux";
        }
        if (OsUtil.is64bit()) {
            ret += " 64bit";
        } else {
            ret += " 32bit";
        }
        return ret;
    }

    @Tool(
            tags = {
                    AiTags.EXECUTABLE_VALUE,
                    AiTags.HUMAN_VALUE,
                    AiTags.COMMAND_VALUE
            }, description = "run an normal command line, implements by java process, some command maybe need in `cmd /c` or `sh -c` when not found command."
    )
    public CommandResult run_command_line(@ToolParam(value = "commandArray", description = "the command array, for example [\"ipconfig\",\"/all\"] or [\"cmd\",\"/c\",\"start\",\"calc\"]")
                                   List<String> commandArray,
                                   @ToolParam(value = "workdir", description = "command workdir, cloud be null, means default user dir, for example 'user' or '/home' ")
                                   String workdir) {
        File dir = null;
        if (workdir == null || workdir.isEmpty()) {
            workdir = ".";
        }
        if (localFileTools == null) {
            throw new IllegalStateException("missing local-file secure control.");
        }
        dir = localFileTools.getFile(workdir);
        CommandResult ret = OsUtil.execCmdForResult(true, TimeUnit.MINUTES.toMillis(3), commandArray.toArray(new String[0]), null, dir, null);
        return ret;
    }
}
