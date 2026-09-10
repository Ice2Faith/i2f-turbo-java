package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.intent.ToolIntent;
import i2f.ai.std.tool.intent.ToolIntentItem;
import i2f.os.OsUtil;
import i2f.os.WindowsUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/9/10 19:57
 * @desc
 */
@ToolIntent(items = @ToolIntentItem(value=CommandTools.TOOL_INTENT_VALUE,description = CommandTools.TOOL_INTENT_DESCRIPTION))
@ConditionalOnExpression(CommandTools.CONDITION_EXPRESS)
@Conditional(PowershellTools.WindowsFormCondition.class)
@Data
@NoArgsConstructor
@Component
public class PowershellTools {
    public static class WindowsFormCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            if (!OsUtil.isWindows()) {
                return false;
            }
            return true;
        }
    }

    @Autowired(required = false)
    private LocalFileTools localFileTools;

    @Tool(
            tags = {
                    AiTags.EXECUTABLE_VALUE,
                    AiTags.HUMAN_VALUE,
                    AiTags.COMMAND_VALUE
            }, description = "run an powershell command line."
    )
    public String run_powershell_command(@ToolParam(value = "command", description = "the powershell command, for example \"Get-Process | Select-Object -First 5\"")
                                   String command,
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
        String ret = WindowsUtil.execPowershell(true, TimeUnit.MINUTES.toMillis(3),
                command, null, dir, null);
        return ret;
    }

    @Tool(
            tags = {
                    AiTags.EXECUTABLE_VALUE,
                    AiTags.HUMAN_VALUE,
                    AiTags.COMMAND_VALUE
            }, description = "run an powershell script, command will run as a temp ps1 script."
    )
    public String run_powershell_script(@ToolParam(value = "script", description = "the powershell full script content, for example \"Get-Process | Select-Object -First 5\"")
                                   String script,
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
        String ret = WindowsUtil.execPowershellScript(true, TimeUnit.MINUTES.toMillis(3),
                script, null, dir, null);
        return ret;
    }
}
