package i2f.natives.windows;

import i2f.natives.core.NativeUtil;

/**
 * @author Ice2Faith
 * @date 2024/5/7 11:41
 * @desc
 */
public class NativeWindows {

    static {
        NativeUtil.loadClasspathLib("lib/NativesWindows");

    }

    public static native String hello();

    public static native int getSystemMetrics(int metric);

    public static native long getDC(long hwnd);

    public static native int releaseDC(long hwnd, long hdc);

    public static native int getDeviceCaps(long hdc, int index);

    public static native long getForegroundWindow();

    public static native long getDesktopWindow();

    public static native String getWindowText(long hwnd);
}
