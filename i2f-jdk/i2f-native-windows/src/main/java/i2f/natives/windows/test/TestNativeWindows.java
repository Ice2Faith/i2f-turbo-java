package i2f.natives.windows.test;

import i2f.natives.windows.NativeWindows;
import i2f.natives.windows.consts.WinCreateToolhelo32SnapshotFlag;
import i2f.natives.windows.consts.WinDeviceCapsIndex;
import i2f.natives.windows.consts.WinSystemMetrics;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/5/7 16:24
 * @desc
 */
public class TestNativeWindows {
    public static void main(String[] args) {
        if (true) {
            System.out.println("---------------------");
            System.out.println(NativeWindows.hello());
            System.out.println(NativeWindows.getSystemMetrics(WinSystemMetrics.SM_CXSCREEN));
            System.out.println(NativeWindows.getSystemMetrics(WinSystemMetrics.SM_CYSCREEN));
        }

        if (true) {
            System.out.println("---------------------");
            long hdc = NativeWindows.getDC(0);
            System.out.println(hdc);
            System.out.println(NativeWindows.getDeviceCaps(hdc, WinDeviceCapsIndex.LOGPIXELSX));
            System.out.println(NativeWindows.getDeviceCaps(hdc, WinDeviceCapsIndex.LOGPIXELSY));

            NativeWindows.releaseDC(0, hdc);
        }

        if (true) {
            System.out.println("---------------------");
            long foregroundWindow = NativeWindows.getForegroundWindow();
            System.out.println(foregroundWindow);
            System.out.println(NativeWindows.getWindowText(foregroundWindow));
            System.out.println(NativeWindows.getClassName(foregroundWindow));
        }

        if (true) {
            System.out.println("---------------------");
            long desktopWindow = NativeWindows.getDesktopWindow();
            System.out.println(desktopWindow);
            System.out.println(NativeWindows.getWindowText(desktopWindow));
            System.out.println(NativeWindows.getClassName(desktopWindow));
        }

        if (true) {
            System.out.println("---------------");
            long hSnapshot = NativeWindows.createToolhelp32Snapshot(WinCreateToolhelo32SnapshotFlag.TH32CS_SNAPPROCESS, 0);
            if (!NativeWindows.isInvalidHandle(hSnapshot)) {
                String info = NativeWindows.process32First(hSnapshot);
                while (info != null) {
                    String[] arr = info.split(";#;");
                    Map<String, String> map = new LinkedHashMap<>();
                    for (String item : arr) {
                        String[] pair = item.split(":", 2);
                        map.put(pair[0], pair[1]);
                    }
                    System.out.println(map);
                    info = NativeWindows.process32Next(hSnapshot);
                }
                NativeWindows.closeHandle(hSnapshot);
            }
        }

        if (true) {
            System.out.println("---------------");
            long hSnapshot = NativeWindows.createToolhelp32Snapshot(WinCreateToolhelo32SnapshotFlag.TH32CS_SNAPMODULE, 0);
            if (!NativeWindows.isInvalidHandle(hSnapshot)) {
                String info = NativeWindows.module32First(hSnapshot);
                while (info != null) {
                    String[] arr = info.split(";#;");
                    Map<String, String> map = new LinkedHashMap<>();
                    for (String item : arr) {
                        String[] pair = item.split(":", 2);
                        map.put(pair[0], pair[1]);
                    }
                    System.out.println(map);
                    info = NativeWindows.module32Next(hSnapshot);
                }
                NativeWindows.closeHandle(hSnapshot);
            }
        }
        if (true) {
            System.out.println("---------------");
            long hSnapshot = NativeWindows.createToolhelp32Snapshot(WinCreateToolhelo32SnapshotFlag.TH32CS_SNAPTHREAD, 0);
            if (!NativeWindows.isInvalidHandle(hSnapshot)) {
                String info = NativeWindows.thread32First(hSnapshot);
                while (info != null) {
                    String[] arr = info.split(";#;");
                    Map<String, String> map = new LinkedHashMap<>();
                    for (String item : arr) {
                        String[] pair = item.split(":", 2);
                        map.put(pair[0], pair[1]);
                    }
                    System.out.println(map);
                    info = NativeWindows.thread32Next(hSnapshot);
                }
                NativeWindows.closeHandle(hSnapshot);
            }
        }

        if (true) {
            String logicalDriveStrings = NativeWindows.getLogicalDriveStrings();
            System.out.println(logicalDriveStrings);
        }
    }
}
