package i2f.os;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

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
        if (charset == null || "".equals(charset)) {
            charset = System.getProperty("file.encoding");
        }
        if (charset == null || "".equals(charset)) {
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

    public static String runCmd(String cmd) {
        try {
            Runtime runtime = Runtime.getRuntime();

            Process process = runtime.exec(cmd);
            InputStream is = process.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
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

            process.waitFor();

            String str = new String(bos.toByteArray(), getCmdCharset());
            return str;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
