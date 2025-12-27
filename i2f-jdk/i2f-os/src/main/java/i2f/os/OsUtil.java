package i2f.os;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2024/4/30 10:43
 * @desc
 */
public class OsUtil {

    public static boolean isWindows() {
        return System.getProperty("os.name", "unknown").toLowerCase().contains("window");
    }

    public static boolean isLinux() {
        return System.getProperty("os.name", "unknown").toLowerCase().contains("linux");
    }

    public static String getCmdCharset() {
        String charset = System.getProperty("sun.jnu.encoding");
        if (charset == null || charset.isEmpty()) {
            charset = System.getProperty("file.encoding");
        }
        if (charset == null || charset.isEmpty()) {
            charset = Charset.defaultCharset().name();
        }
        return charset;
    }

    public static String subline(String str, int lineIndex, int lineCount) {
        String[] arr = str.split("\n");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lineCount; i++) {
            if (i + lineIndex >= arr.length) {
                break;
            }
            builder.append(arr[i + lineIndex]);
            builder.append("\n");
        }
        return builder.toString();
    }

    public static void startCmd(String cmd) {
        runCmd(cmd, getCmdCharset());
    }

    public static void startCmd(String cmd, String charset) {
        startCmd(cmd, null, null, charset);
    }

    public static void startCmd(String cmd, String[] envp, File dir, String charset) {
        execCmd(false, -1, cmd, envp, dir, charset);
    }

    public static String runCmd(String cmd) {
        return runCmd(cmd, getCmdCharset());
    }

    public static String runCmd(String cmd, String charset) {
        return runCmd(cmd, null, null, charset);
    }

    public static String runCmd(String cmd, String[] envp, File dir, String charset) {
        return execCmd(true, TimeUnit.MINUTES.toMillis(3), cmd, envp, dir, charset);
    }

    public static String execCmd(boolean requireOutput, long waitForMillsSeconds, String cmd, String[] envp, File dir, String charset) {
        try {
            Runtime runtime = Runtime.getRuntime();

            Process process = runtime.exec(cmd, envp, dir);
            return getProcessStdout(requireOutput, waitForMillsSeconds, process, charset);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    public static void startCmd(String... cmdArr) {
        runCmd(cmdArr, getCmdCharset());
    }

    public static void startCmd(String[] cmdArr, String charset) {
        startCmd(cmdArr, null, null, charset);
    }

    public static void startCmd(String[] cmdArr, String[] envp, File dir, String charset) {
        execCmd(false, -1, cmdArr, envp, dir, charset);
    }

    public static String runCmd(String... cmdArr) {
        return runCmd(cmdArr, getCmdCharset());
    }

    public static String runCmd(String[] cmdArr, String charset) {
        return runCmd(cmdArr, null, null, charset);
    }

    public static String runCmd(String[] cmdArr, String[] envp, File dir, String charset) {
        return execCmd(true, TimeUnit.MINUTES.toMillis(3), cmdArr, envp, dir, charset);
    }

    public static String execCmd(boolean requireOutput, long waitForMillsSeconds, String[] cmdArr, String[] envp, File dir, String charset) {
        try {
            Runtime runtime = Runtime.getRuntime();

            Process process = runtime.exec(cmdArr, envp, dir);
            return getProcessStdout(requireOutput, waitForMillsSeconds, process, charset);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static String getProcessStdout(boolean requireOutput, long waitForMillsSeconds, Process process, String charset) throws IOException, InterruptedException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (requireOutput) {
            InputStream is = process.getInputStream();
            byte[] buf = new byte[4096];
            int len = 0;
            while ((len = is.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            bos.flush();

            InputStream es = process.getErrorStream();
            while ((len = es.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            bos.flush();
        }

        if (waitForMillsSeconds >= 0) {
            process.waitFor(waitForMillsSeconds, TimeUnit.MILLISECONDS);
        } else {
            process.waitFor();
        }

        if (!requireOutput) {
            return null;
        }
        if (charset == null || charset.isEmpty()) {
            charset = getCmdCharset();
        }
        String str = new String(bos.toByteArray(), charset);
        return str;
    }

    public static boolean is64bit() {
        String osArch = System.getProperty("os.arch");
        if ("amd64".equalsIgnoreCase(osArch)) {
            return true;
        } else if ("x86_64".equalsIgnoreCase(osArch)) {
            return true;
        } else if ("arm64".equalsIgnoreCase(osArch)) {
            return true;
        } else if ("ppc64".equalsIgnoreCase(osArch)) {
            return true;
        }
        String vmName = System.getProperty("java.vm.name");
        if (vmName != null) {
            if (vmName.toLowerCase().contains("64-bit")) {
                return true;
            }
        }
        String sunArch = System.getProperty("sun.arch.data.model");
        if ("64".equalsIgnoreCase(sunArch)) {
            return true;
        }
        return false;
    }
}
