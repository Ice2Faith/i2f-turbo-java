package i2f.natives.windows.test;

import i2f.natives.windows.NativesWindows;
import i2f.natives.windows.consts.device.WinDeviceCapsIndex;
import i2f.natives.windows.consts.process.WinCreateToolhelo32SnapshotFlag;
import i2f.natives.windows.consts.register.WinRegOpenKeyHkey;
import i2f.natives.windows.consts.register.WinRegOpenKeySamDesired;
import i2f.natives.windows.consts.window.WinSystemMetrics;

import java.util.Arrays;
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
            System.out.println(NativesWindows.hello());
            System.out.println(NativesWindows.getSystemMetrics(WinSystemMetrics.SM_CXSCREEN));
            System.out.println(NativesWindows.getSystemMetrics(WinSystemMetrics.SM_CYSCREEN));
        }

        if (true) {
            System.out.println("---------------------");
            long hdc = NativesWindows.getDC(0);
            System.out.println(hdc);
            System.out.println(NativesWindows.getDeviceCaps(hdc, WinDeviceCapsIndex.LOGPIXELSX));
            System.out.println(NativesWindows.getDeviceCaps(hdc, WinDeviceCapsIndex.LOGPIXELSY));

            NativesWindows.releaseDC(0, hdc);
        }

        if (true) {
            System.out.println("---------------------");
            long foregroundWindow = NativesWindows.getForegroundWindow();
            System.out.println(foregroundWindow);
            System.out.println(NativesWindows.getWindowText(foregroundWindow));
            System.out.println(NativesWindows.getClassName(foregroundWindow));
        }

        if (true) {
            System.out.println("---------------------");
            long desktopWindow = NativesWindows.getDesktopWindow();
            System.out.println(desktopWindow);
            System.out.println(NativesWindows.getWindowText(desktopWindow));
            System.out.println(NativesWindows.getClassName(desktopWindow));
        }

        if (true) {
            System.out.println("---------------");
            long hSnapshot = NativesWindows.createToolhelp32Snapshot(WinCreateToolhelo32SnapshotFlag.TH32CS_SNAPPROCESS, 0);
            if (!NativesWindows.isInvalidHandle(hSnapshot)) {
                String info = NativesWindows.process32First(hSnapshot);
                while (info != null) {
                    String[] arr = info.split(";#;");
                    Map<String, String> map = new LinkedHashMap<>();
                    for (String item : arr) {
                        String[] pair = item.split(":", 2);
                        map.put(pair[0], pair[1]);
                    }
                    System.out.println(map);
                    info = NativesWindows.process32Next(hSnapshot);
                }
                NativesWindows.closeHandle(hSnapshot);
            }
        }

        if (true) {
            System.out.println("---------------");
            long hSnapshot = NativesWindows.createToolhelp32Snapshot(WinCreateToolhelo32SnapshotFlag.TH32CS_SNAPMODULE, 0);
            if (!NativesWindows.isInvalidHandle(hSnapshot)) {
                String info = NativesWindows.module32First(hSnapshot);
                while (info != null) {
                    String[] arr = info.split(";#;");
                    Map<String, String> map = new LinkedHashMap<>();
                    for (String item : arr) {
                        String[] pair = item.split(":", 2);
                        map.put(pair[0], pair[1]);
                    }
                    System.out.println(map);
                    info = NativesWindows.module32Next(hSnapshot);
                }
                NativesWindows.closeHandle(hSnapshot);
            }
        }
        if (true) {
            System.out.println("---------------");
            long hSnapshot = NativesWindows.createToolhelp32Snapshot(WinCreateToolhelo32SnapshotFlag.TH32CS_SNAPTHREAD, 0);
            if (!NativesWindows.isInvalidHandle(hSnapshot)) {
                String info = NativesWindows.thread32First(hSnapshot);
                while (info != null) {
                    String[] arr = info.split(";#;");
                    Map<String, String> map = new LinkedHashMap<>();
                    for (String item : arr) {
                        String[] pair = item.split(":", 2);
                        map.put(pair[0], pair[1]);
                    }
                    System.out.println(map);
                    info = NativesWindows.thread32Next(hSnapshot);
                }
                NativesWindows.closeHandle(hSnapshot);
            }
        }

        if (true) {
            System.out.println("---------------");
            String logicalDriveStrings = NativesWindows.getLogicalDriveStrings();
            System.out.println(logicalDriveStrings);
        }

        if (true) {
            System.out.println("---------------");
            int[] pos = NativesWindows.getCursorPos();
            long hwnd = NativesWindows.windowFromPoint(pos[0], pos[1]);
            String text = NativesWindows.getWindowText(hwnd);
            String className = NativesWindows.getClassName(hwnd);
            System.out.println(Arrays.toString(pos));
            System.out.println(hwnd);
            System.out.println(text);
            System.out.println(className);
        }

        if (true) {
            System.out.println("---------------");
            String info = NativesWindows.getWindowInfo(NativesWindows.getDesktopWindow());
            if (info != null) {
                String[] arr = info.split(";#;");
                Map<String, String> map = new LinkedHashMap<>();
                for (String item : arr) {
                    String[] pair = item.split(":", 2);
                    map.put(pair[0], pair[1]);
                }
                System.out.println(map);
            }
        }

        if (true) {
            System.out.println("---------------");
            long hkey = NativesWindows.regOpenKeyEx(WinRegOpenKeyHkey.HKEY_CURRENT_USER, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run\\", 0, WinRegOpenKeySamDesired.KEY_READ);
            int index = 0;
            while (true) {
                String info = NativesWindows.regEnumValue(hkey, index);
                if (info == null) {
                    break;
                }
                System.out.println(info);
                index++;
            }
            NativesWindows.regCloseKey(hkey);
        }

        if (true) {
            System.out.println("---------------");
            long hkey = NativesWindows.regOpenKeyEx(WinRegOpenKeyHkey.HKEY_CURRENT_USER, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\", 0, WinRegOpenKeySamDesired.KEY_READ);
            int index = 0;
            while (true) {
                String info = NativesWindows.regEnumKeyEx(hkey, index);
                if (info == null) {
                    break;
                }
                System.out.println(info);
                index++;
            }
            NativesWindows.regCloseKey(hkey);
        }
    }
}

