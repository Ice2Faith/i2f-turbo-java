package i2f.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/7/31 17:06
 * @desc
 */
public class JvmUtil {

    public static RuntimeMXBean getRuntimeMXBean() {
        return ManagementFactory.getRuntimeMXBean();
    }

    public static String getPid() {
        String name = getRuntimeMXBean().getName();
        String[] arr = name.split("@", 2);
        if (arr.length == 2) {
            return arr[0];
        }
        return "-1";
    }

    public static String getStartUser() {
        String name = getRuntimeMXBean().getName();
        String[] arr = name.split("@", 2);
        if (arr.length == 2) {
            return arr[1];
        }
        return "";
    }

    public static List<String> getInputArguments() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments();
    }

    public static boolean isDebug() {
        List<String> args = getInputArguments();
        for (String arg : args) {
            if (arg.startsWith("-Xrunjdwp") || arg.startsWith("-agentlib:jdwp")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAgent() {
        List<String> args = getInputArguments();
        for (String arg : args) {
            if (arg.startsWith("-javaagent:")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNoVerify() {
        List<String> args = getInputArguments();
        for (String arg : args) {
            if ("-noverify".equals(arg) || "-Xverify:none".equals(arg)) {
                return true;
            }
        }
        return false;
    }
}
