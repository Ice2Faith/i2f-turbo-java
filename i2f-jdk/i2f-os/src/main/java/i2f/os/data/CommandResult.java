package i2f.os.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/9/10 20:55
 * @desc
 */
@Data
@NoArgsConstructor
public class CommandResult{
    protected int exitCode;
    protected boolean executeTimeout;
    protected String stdout;
    public static CommandResult of(Process process,String stdout){
        CommandResult ret = new CommandResult();
        ret.setStdout(stdout);
        try{
            int code = process.exitValue();
            ret.setExitCode(code);
            ret.setExecuteTimeout(false);
        }catch(Exception e){
            ret.setExecuteTimeout(true);
            ret.setExitCode(Integer.MIN_VALUE);
        }
        return ret;
    }
}
