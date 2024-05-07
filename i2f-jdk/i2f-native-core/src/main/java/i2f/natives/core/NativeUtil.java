package i2f.natives.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2024/5/7 15:38
 * @desc
 */
public class NativeUtil {
    public static String getLibName(String name) {
        return System.mapLibraryName(name);
    }

    public static InputStream getClasspathLib(String name) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream is = loader.getResourceAsStream(getLibName(name));
        return is;
    }

    public static void loadClasspathLib(String name) {
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        String libName = getLibName(name);
        File dir = new File("lib");
        File file = new File(dir, new File(libName).getName());
        if (!file.exists()) {
            file.getParentFile().mkdirs();

            ClassLoader loader = Thread.currentThread().getContextClassLoader();

            InputStream is = loader.getResourceAsStream(libName);
            if (is == null) {
                throw new IllegalStateException("no such lib found in classpath:" + libName);
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                byte[] buf = new byte[8192];
                int len = 0;
                while ((len = is.read(buf)) > 0) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
                fos.close();
            } catch (Exception e) {
                throw new IllegalStateException("release lib [" + libName + "] error: " + e.getMessage(), e);
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (Exception e) {

                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (Exception e) {

                }
            }
        }

        System.load(file.getAbsolutePath());
    }
}
