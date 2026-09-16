package i2f.springboot.ops.openai.tts.cmd;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2026/9/16 19:33
 * @desc
 */
@Conditional(TermuxTtsSpeaker.AvaliableCondition.class)
@Data
@NoArgsConstructor
@Component
public class TermuxTtsSpeaker implements CommandTtsSpeaker {
    public static final String COMMAND = "termux-tts-speak";
    public static final int PRIOR = 100;
    protected static volatile Process lastProcess = null;

    public static class AvaliableCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return getCommand() != null;
        }
    }

    static {
        getCommand();
    }

    private static AtomicReference<AtomicReference<String>> cacheCommand = new AtomicReference<>();
    private static ReentrantLock lockCommand = new ReentrantLock();

    public static String getCommand() {
        AtomicReference<String> optional = cacheCommand.get();
        // 这里借助atomic来存储null无值的情况
        if (optional != null) {
            return optional.get();
        }
        lockCommand.lock();
        try {
            // 临界区再次检查，有值直接返回
            AtomicReference<String> cached = cacheCommand.get();
            if (cached != null) {
                return cached.get();
            }

            String ret = null;
            String[] names = {COMMAND};
            for (String name : names) {
                if (isCommandAvailable(name)) {
                    ret = name;
                    break;
                }
            }
            cacheCommand.set(new AtomicReference<>(ret));
            return ret;
        } finally {
            lockCommand.unlock();
        }
    }

    public static boolean isCommandAvailable(String command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command, "-h");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                int exitCode = process.waitFor();
                return exitCode == 0 && line != null && line.toLowerCase().contains(COMMAND);
            }
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public boolean available() {
        return getCommand() != null;
    }

    @Override
    public int prior() {
        return PRIOR;
    }

    @Override
    public void speak(String content) throws Throwable {
        try {
            if (lastProcess != null) {
                lastProcess.destroyForcibly();
                lastProcess = null;
            }
        } catch (Throwable e) {
            // ignore
        }


        lastProcess = new ProcessBuilder(COMMAND,
                "-s", "MUSIC",
                content
        ).start();
    }
}
