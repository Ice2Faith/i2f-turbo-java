package i2f.natives.windows;

import i2f.natives.core.NativeUtil;

/**
 * @author Ice2Faith
 * @date 2024/5/13 18:08
 * @desc
 */
public class NativesWindows8 {
    static {
        NativeUtil.loadClasspathLib("lib/NativesWindows8");
    }

    public static native String hello();


    public static native int[] getDpiForMonitor(long hMonitor, int dpiType);

    public static native int getScaleFactorForMonitor(long hMonitor);

}
