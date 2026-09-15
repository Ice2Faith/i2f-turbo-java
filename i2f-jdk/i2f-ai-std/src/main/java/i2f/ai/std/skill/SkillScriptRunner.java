package i2f.ai.std.skill;

import i2f.os.data.CommandResult;

import java.io.File;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/9/15 20:08
 * @desc
 */
public interface SkillScriptRunner {
    String suffix();

    CommandResult runScript(File scriptFile, List<String> commandArguments) throws Exception;
}
