package i2f.springboot.ops.util;

/**
 * @author Ice2Faith
 * @date 2025/11/13 21:24
 * @desc
 */
public class HumanUtil {
    public static String humanFileSize(long size) {
        if (size < 1024) {
            return String.format("%dB", size);
        }
        if (size < 1024 * 1024) {
            return String.format("%.2fKB", size / 1024.0);
        }
        if (size < 1024 * 1024 * 1024) {
            return String.format("%.2fMB", size / 1024.0 / 1024.0);
        }
        if (size < 1024L * 1024 * 1024 * 1024) {
            return String.format("%.2fGB", size / 1024.0 / 1024.0 / 1024.0);
        }
        return String.format("%.2fTB", size / 1024.0 / 1024.0 / 1024.0 / 1024.0);
    }
}
