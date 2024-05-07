package i2f.natives.windows.test;

import i2f.natives.windows.NativeWindows;
import i2f.natives.windows.consts.WinDeviceCapsIndex;
import i2f.natives.windows.consts.WinSystemMetrics;

/**
 * @author Ice2Faith
 * @date 2024/5/7 16:24
 * @desc
 */
public class TestNativeWindows {
    public static void main(String[] args) {
        System.out.println(NativeWindows.hello());
        System.out.println(NativeWindows.getSystemMetrics(WinSystemMetrics.SM_CXSCREEN));
        System.out.println(NativeWindows.getSystemMetrics(WinSystemMetrics.SM_CYSCREEN));

        long hdc = NativeWindows.getDC(0);
        System.out.println(hdc);
        System.out.println(NativeWindows.getDeviceCaps(hdc, WinDeviceCapsIndex.LOGPIXELSX));
        System.out.println(NativeWindows.getDeviceCaps(hdc, WinDeviceCapsIndex.LOGPIXELSY));

        NativeWindows.releaseDC(0, hdc);

        long foregroundWindow = NativeWindows.getForegroundWindow();
        System.out.println(foregroundWindow);
        System.out.println(NativeWindows.getWindowText(foregroundWindow));

        long desktopWindow = NativeWindows.getDesktopWindow();
        System.out.println(desktopWindow);
        System.out.println(NativeWindows.getWindowText(desktopWindow));
    }
}
