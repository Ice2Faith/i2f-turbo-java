package i2f.os;

import i2f.os.linux.perf.LinuxUtil;
import i2f.os.windows.perf.WindowsUtil;

/**
 * @author Ice2Faith
 * @date 2024/5/7 8:59
 * @desc
 */
public class PerfUtil {
    public static double getCpuLoadPercent() {
        if (OsUtil.isWindows()) {
            return WindowsUtil.getCpuLoadPercent();
        } else if (OsUtil.isLinux()) {
            return LinuxUtil.getCpuLoadPercent();
        }
        return -1;
    }

    public static double getMemoryUsedPercent() {
        if (OsUtil.isWindows()) {
            return WindowsUtil.getMemoryUsedPercent();
        } else if (OsUtil.isLinux()) {
            return LinuxUtil.getMemoryUsedPercent();
        }
        return -1;
    }

    public static double getDiskUsedPercent() {
        if (OsUtil.isWindows()) {
            return WindowsUtil.getDiskUsedPercent();
        } else if (OsUtil.isLinux()) {
            return LinuxUtil.getDiskUsedPercent();
        }
        return -1;
    }
}
