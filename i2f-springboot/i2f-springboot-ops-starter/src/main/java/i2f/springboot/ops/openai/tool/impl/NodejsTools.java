package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.intent.ToolIntent;
import i2f.ai.std.tool.intent.ToolIntentItem;
import i2f.os.OsUtil;
import i2f.os.data.CommandResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2026/9/10 19:57
 * @desc
 */
@ToolIntent(items = @ToolIntentItem(value="nodejs",description = "提供执行nodejs脚本的能力"))
@ConditionalOnExpression("${ai.tools.nodejs.enable:false}")
@Conditional(NodejsTools.NodejsInstalledCondition.class)
@Data
@NoArgsConstructor
@Component
public class NodejsTools {

    public static class NodejsInstalledCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return getNodejsCommand()!=null;
        }
    }

    private static AtomicReference<AtomicReference<String>> cacheNodejsCommand = new AtomicReference<>();
    private static ReentrantLock lockNodejsCommand = new ReentrantLock();
    public static String getNodejsCommand(){
        AtomicReference<String> optional = cacheNodejsCommand.get();
        // 这里借助atomic来存储null无值的情况
        if(optional!=null){
            return optional.get();
        }
        lockNodejsCommand.lock();
        try {
            // 临界区再次检查，有值直接返回
            AtomicReference<String> cached = cacheNodejsCommand.get();
            if (cached != null) {
                return cached.get();
            }

            String ret = null;
            String[] names = {"node"};
            for (String name : names) {
                if (isNodejsAvailable(name)) {
                    ret = name;
                    break;
                }
            }
            cacheNodejsCommand.set(new AtomicReference<>(ret));
            return ret;
        }finally {
            lockNodejsCommand.unlock();
        }
    }

    public static boolean isNodejsAvailable(String command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command, "-v");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                int exitCode = process.waitFor();
                return exitCode == 0 && line != null && line.trim().toLowerCase().startsWith("v");
            }
        } catch (Throwable e) {
            return false;
        }
    }

    @Autowired(required = false)
    private LocalFileTools localFileTools;

    @Tool(
            tags = {
                    AiTags.EXECUTABLE_VALUE,
                    AiTags.HUMAN_VALUE,
                    AiTags.SCRIPT_VALUE
            }, description = "run an nodejs script, command will run as a temp js script."
    )
    public CommandResult run_nodejs_script(@ToolParam(value = "script", description = "the nodejs full script content, for example \"print(1)\"")
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
        CommandResult ret = execNodejsScript(true, TimeUnit.MINUTES.toMillis(3),
                script, null, dir, null);
        return ret;
    }

    public static CommandResult execNodejsScript(boolean requireOutput, long waitForMillsSeconds, String command, String[] envp, File dir, String charset) {
        String fileName= "nodejs-"+ UUID.randomUUID().toString().replace("-", "").toLowerCase()+".js";
        File scriptFile=new File(fileName);
        String nodejs= getNodejsCommand();
        try {
            if (dir != null) {
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                scriptFile = new File(dir, fileName);
            }

            try (FileOutputStream fos = new FileOutputStream(scriptFile)) {
                // 写入命令
                fos.write(command.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                throw new IllegalStateException("write script to file failed", e);
            }
            List<String> cmdList = new ArrayList<>();
            cmdList.add(nodejs);
            cmdList.add(fileName);
            return OsUtil.execCmdForResult(requireOutput, waitForMillsSeconds, cmdList.toArray(new String[0]), envp, dir, charset);
        }finally {
            if(scriptFile.exists()){
                scriptFile.delete();
            }
        }
    }
}
