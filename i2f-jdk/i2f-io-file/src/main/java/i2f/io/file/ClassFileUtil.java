package i2f.io.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2026/9/24 8:30
 * @desc
 */
public class ClassFileUtil {

    private static final Map<Integer, String> MAJOR_TO_JDK = new ConcurrentHashMap<>();

    static {
        MAJOR_TO_JDK.put(45, "1.1");
        MAJOR_TO_JDK.put(46, "1.2");
        MAJOR_TO_JDK.put(47, "1.3");
        MAJOR_TO_JDK.put(48, "1.4");
        MAJOR_TO_JDK.put(49, "5");
        MAJOR_TO_JDK.put(50, "6");
        MAJOR_TO_JDK.put(51, "7");
        MAJOR_TO_JDK.put(52, "8");
        MAJOR_TO_JDK.put(53, "9");
        MAJOR_TO_JDK.put(54, "10");
        MAJOR_TO_JDK.put(55, "11");
        MAJOR_TO_JDK.put(56, "12");
        MAJOR_TO_JDK.put(57, "13");
        MAJOR_TO_JDK.put(58, "14");
        MAJOR_TO_JDK.put(59, "15");
        MAJOR_TO_JDK.put(60, "16");
        MAJOR_TO_JDK.put(61, "17");
        MAJOR_TO_JDK.put(62, "18");
        MAJOR_TO_JDK.put(63, "19");
        MAJOR_TO_JDK.put(64, "20");
        MAJOR_TO_JDK.put(65, "21");
        MAJOR_TO_JDK.put(66, "22");
        MAJOR_TO_JDK.put(67, "23");
        MAJOR_TO_JDK.put(68, "24");
        MAJOR_TO_JDK.put(69, "25");
        MAJOR_TO_JDK.put(70, "26");
        MAJOR_TO_JDK.put(71, "27");
    }

    public static int getMajorVersion(URL url) throws IOException {
        return getMajorVersion(url.openStream());
    }

    public static int getMajorVersion(File file) throws IOException {
        return getMajorVersion(new FileInputStream(file));
    }

    public static boolean isClassFile(File file) {
        String name = file.getName();
        if (name.endsWith(".class")) {
            return true;
        }
        return false;
    }

    /**
     * 读取 class 文件流的 majorVersion
     * 如果不是 class 文件，返回 -1
     * 自动关闭流
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static int getMajorVersion(InputStream is) throws IOException {
        try (InputStream tis = is) {
            // 读取头字节
            byte[] header = new byte[8];
            int read = tis.read(header);
            if (read < 8) {
                return -1;
            }

            // 验证魔数 0xCAFEBABE
            int magic = ((header[0] & 0xFF) << 24)
                    | ((header[1] & 0xFF) << 16)
                    | ((header[2] & 0xFF) << 8)
                    | (header[3] & 0xFF);
            if (magic != 0xCAFEBABE) {
                return -1;
            }

            // 第 7-8 字节是 major_version
            int majorVersion = ((header[6] & 0xFF) << 8)
                    | (header[7] & 0xFF);

            return majorVersion;
        }
    }

    /**
     * majorVersion 转换为对应的 jdk 版本号
     * 无法转换的返回 null
     *
     * @param majorVersion
     * @return
     */
    public static String majorVersionToJdkVersion(int majorVersion) {
        String ret = MAJOR_TO_JDK.get(majorVersion);
        return ret;
    }
}
