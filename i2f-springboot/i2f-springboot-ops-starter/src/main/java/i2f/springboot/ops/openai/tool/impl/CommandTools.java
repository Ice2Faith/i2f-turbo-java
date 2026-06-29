package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.os.OsUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/6/22 16:38
 * @desc
 */
@ConditionalOnExpression("${ai.tools.command.enable:true}")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Tools
public class CommandTools {

    @Autowired(required = false)
    private LocalFileTools localFileTools;

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE,
                    AiTags.READONLY_VALUE
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
                    AiTags.HUMAN_VALUE
            }, description = "run an command line, implements by java process."
    )
    public String run_command_line(@ToolParam(value = "commandArray", description = "the command array, for example [\"ipconfig\",\"/all\"] or [\"cmd\",\"/k\",\"start\",\"calc\"]")
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
        String ret = OsUtil.runCmd(commandArray.toArray(new String[0]), null, dir, null);
        return ret;
    }
}
