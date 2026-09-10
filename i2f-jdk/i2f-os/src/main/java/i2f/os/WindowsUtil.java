package i2f.os;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2026/9/10 19:38
 * @desc
 */
public class WindowsUtil {
    public static String execPowershell(boolean requireOutput, long waitForMillsSeconds, String command, String[] envp, File dir, String charset) {
        if(!OsUtil.isWindows()){
            throw new IllegalStateException("current os is not windows, not support powershell");
        }
        List<String> cmdList = new ArrayList<>();
        cmdList.addAll(Arrays.asList(
                "powershell.exe",
                "-NoProfile",              // 不加载用户配置文件，加快启动
                "-ExecutionPolicy", "Bypass", // 绕过执行策略限制
                "-Command"
        ));
        cmdList.add(command);
        return OsUtil.execCmd(requireOutput, waitForMillsSeconds, cmdList.toArray(new String[0]), envp, dir, charset);
    }

    public static String execPowershellScript(boolean requireOutput, long waitForMillsSeconds, String command, String[] envp, File dir, String charset) {
        if(!OsUtil.isWindows()){
            throw new IllegalStateException("current os is not windows, not support powershell");
        }
        String fileName= "ps-"+UUID.randomUUID().toString().replace("-", "").toLowerCase()+".ps1";
        File scriptFile=new File(fileName);
        try {
            if (dir != null) {
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                scriptFile = new File(dir, fileName);
            }

            try (FileOutputStream fos = new FileOutputStream(scriptFile)) {
                // powershell 执行的脚本，默认按照系统字符集，因此有两个方案，保证脚本顺利执行
                // 一个是写入带有BOM头的UTF脚本，兼容性较好
                // 一个是写入系统字符集编码的脚本
                // 写入 UTF-8 BOM 头：EF BB BF
                fos.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
                // 写入命令
                fos.write(command.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                throw new IllegalStateException("write script to file failed", e);
            }
            List<String> cmdList = new ArrayList<>();
            cmdList.addAll(Arrays.asList(
                    "powershell.exe",
                    "-NoProfile",              // 不加载用户配置文件，加快启动
                    "-ExecutionPolicy", "Bypass", // 绕过执行策略限制
                    "-File"
            ));
            cmdList.add(fileName);
            return OsUtil.execCmd(requireOutput, waitForMillsSeconds, cmdList.toArray(new String[0]), envp, dir, charset);
        }finally {
            if(scriptFile.exists()){
                scriptFile.delete();
            }
        }
    }
}
