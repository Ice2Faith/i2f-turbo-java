package i2f.springboot.ops.openai.skill;

import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import i2f.ai.std.skill.SkillScriptRunner;
import i2f.os.data.CommandResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/9/15 20:18
 * @desc
 */
@ConditionalOnExpression("${ai.skills.runner.groovy.enable:true}")
@ConditionalOnClass(GroovyShell.class)
@Data
@NoArgsConstructor
@Component
public class GroovySkillScriptRunner implements SkillScriptRunner {
    public static final String SUFFIX = ".groovy";

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String suffix() {
        return SUFFIX;
    }

    @Override
    public CommandResult runScript(File scriptFile, List<String> commandArguments) throws Exception {
        CommandResult result = new CommandResult();
        // 假定超时
        result.setExecuteTimeout(true);
        result.setExitCode(Integer.MIN_VALUE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream captureStream = new PrintStream(baos, true, "UTF-8");

        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            try {

                Binding binding = new Binding();
                binding.setVariable("out", captureStream);

                GroovyShell shell = new GroovyShell(binding);
                Object ret = shell.run(scriptFile, commandArguments);
                // 转换结果
                try {
                    if (ret instanceof CharSequence) {
                        ret = String.valueOf(ret);
                    } else {
                        ret = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ret);
                    }
                } catch (Exception e) {
                    ret = String.valueOf(ret);
                }
                result.setStdout(String.valueOf(ret));
                result.setExitCode(0);
                result.setExecuteTimeout(false);
            } catch (Throwable e) {

            } finally {
                latch.countDown();
            }
        }).start();

        try {
            latch.await(3, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // ignore
        }

        String stdout = new String(baos.toByteArray(), "UTF-8");
        String content = result.getStdout();

        String payload = "groovy last returns: \n" +
                content + "\n" +
                "------------------------------------\n" +
                "stdout:\n" +
                stdout + "\n";
        result.setStdout(payload);


        return result;
    }
}
