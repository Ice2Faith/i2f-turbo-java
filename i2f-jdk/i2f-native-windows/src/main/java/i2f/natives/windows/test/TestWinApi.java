package i2f.natives.windows.test;

import i2f.natives.windows.WinApi;
import i2f.natives.windows.consts.*;
import i2f.natives.windows.types.*;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/5/7 16:24
 * @desc
 */
public class TestWinApi {
    public static void main(String[] args) {
        if (true) {
            System.out.println("---------------------");
            System.out.println(WinApi.hello());
            System.out.println(WinApi.getSystemMetrics(WinSystemMetrics.SM_CXSCREEN));
            System.out.println(WinApi.getSystemMetrics(WinSystemMetrics.SM_CYSCREEN));
        }

        if (true) {
            System.out.println("---------------------");
            Hdc hdc = WinApi.getDC(Hwnd.ZERO);
            System.out.println(hdc);
            System.out.println(WinApi.getDeviceCaps(hdc, WinDeviceCapsIndex.LOGPIXELSX));
            System.out.println(WinApi.getDeviceCaps(hdc, WinDeviceCapsIndex.LOGPIXELSY));

            WinApi.releaseDC(Hwnd.ZERO, hdc);
        }

        if (true) {
            System.out.println("---------------------");
            Hwnd foregroundWindow = WinApi.getForegroundWindow();
            System.out.println(foregroundWindow);
            System.out.println(WinApi.getWindowText(foregroundWindow));
            System.out.println(WinApi.getClassName(foregroundWindow));
        }

        if (true) {
            System.out.println("---------------------");
            Hwnd desktopWindow = WinApi.getDesktopWindow();
            System.out.println(desktopWindow);
            System.out.println(WinApi.getWindowText(desktopWindow));
            System.out.println(WinApi.getClassName(desktopWindow));
        }

        if (true) {
            System.out.println("---------------");
            Handle hSnapshot = WinApi.createToolhelp32Snapshot(WinCreateToolhelo32SnapshotFlag.TH32CS_SNAPPROCESS, 0);
            if (!WinApi.isInvalidHandle(hSnapshot)) {
                ProcessEntry32 info = WinApi.process32First(hSnapshot);
                while (info != null) {
                    System.out.println(info);
                    info = WinApi.process32Next(hSnapshot);
                }
                WinApi.closeHandle(hSnapshot);
            }
        }

        if (true) {
            System.out.println("---------------");
            Handle hSnapshot = WinApi.createToolhelp32Snapshot(WinCreateToolhelo32SnapshotFlag.TH32CS_SNAPMODULE, 0);
            if (!WinApi.isInvalidHandle(hSnapshot)) {
                ModuleEntry32 info = WinApi.module32First(hSnapshot);
                while (info != null) {
                    System.out.println(info);
                    info = WinApi.module32Next(hSnapshot);
                }
                WinApi.closeHandle(hSnapshot);
            }
        }
        if (true) {
            System.out.println("---------------");
            Handle hSnapshot = WinApi.createToolhelp32Snapshot(WinCreateToolhelo32SnapshotFlag.TH32CS_SNAPTHREAD, 0);
            if (!WinApi.isInvalidHandle(hSnapshot)) {
                ThreadEntry32 info = WinApi.thread32First(hSnapshot);
                while (info != null) {
                    System.out.println(info);
                    info = WinApi.thread32Next(hSnapshot);
                }
                WinApi.closeHandle(hSnapshot);
            }
        }

        if (true) {
            System.out.println("---------------");
            List<String> logicalDriveStrings = WinApi.getLogicalDriveStrings();
            System.out.println(logicalDriveStrings);
        }

        if (true) {
            System.out.println("---------------");
            Hwnd hwnd = WinApi.windowFromCursorPos();
            String text = WinApi.getWindowText(hwnd);
            String className = WinApi.getClassName(hwnd);
            System.out.println(hwnd);
            System.out.println(text);
            System.out.println(className);
        }

        if (true) {
            System.out.println("---------------");
            WindowInfo info = WinApi.getWindowInfo(WinApi.getDesktopWindow());
            System.out.println(info);
        }

        if (true) {
            System.out.println("---------------");
            Long currentProcessId = WinApi.getCurrentProcessId();
            Hwnd currentHwnd = null;
            List<Hwnd> hwnds = WinApi.enumWindows();
            for (Hwnd hwnd : hwnds) {
                long windowThreadProcessId = WinApi.getWindowThreadProcessId(hwnd);
                if (windowThreadProcessId == currentProcessId) {
                    currentHwnd = hwnd;
                    break;
                }
            }

            System.out.println(currentProcessId);
            if (currentHwnd != null) {
                String text = WinApi.getWindowText(currentHwnd);
                String className = WinApi.getClassName(currentHwnd);
                System.out.println(currentHwnd);
                System.out.println(text);
                System.out.println(className);
            }
        }

        if (true) {
            System.out.println("---------------");
            HKey hkey = WinApi.regOpenKeyEx(new HKey(WinRegOpenKeyHkey.HKEY_CURRENT_USER), "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run\\", 0, WinRegOpenKeySamDesired.KEY_READ);
            int index = 0;
            while (true) {
                RegEnumValueInfo info = WinApi.regEnumValue(hkey, index);
                if (info == null) {
                    break;
                }
                System.out.println(info);
                index++;
            }
            WinApi.regCloseKey(hkey);
        }

        if (true) {
            System.out.println("---------------");
            HKey hkey = WinApi.regOpenKeyEx(new HKey(WinRegOpenKeyHkey.HKEY_CURRENT_USER), "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\", 0, WinRegOpenKeySamDesired.KEY_READ);
            int index = 0;
            while (true) {
                RegEnumKeyExInfo info = WinApi.regEnumKeyEx(hkey, index);
                if (info == null) {
                    break;
                }
                System.out.println(info);
                index++;
            }
            WinApi.regCloseKey(hkey);
        }

        if (true) {
            System.out.println("---------------");
            WinApi.regAddBootItem("notepad", "C:\\Windows\\system32\\nodepad.exe");
        }

        if (true) {
            System.out.println("---------------");
            Map<String, List<RegEnumValueInfo>> listMap = WinApi.regAllBootValues();
            for (Map.Entry<String, List<RegEnumValueInfo>> entry : listMap.entrySet()) {
                System.out.println(entry.getKey());
                for (RegEnumValueInfo info : entry.getValue()) {
                    System.out.println("\t" + info);
                }
            }
        }

        if (true) {
            System.out.println("---------------");
            WinApi.regDeleteBootItem("notepad");
        }

        if (true) {
            System.out.println("---------------");
            Map<String, List<RegEnumValueInfo>> listMap = WinApi.regAllBootValues();
            for (Map.Entry<String, List<RegEnumValueInfo>> entry : listMap.entrySet()) {
                System.out.println(entry.getKey());
                for (RegEnumValueInfo info : entry.getValue()) {
                    System.out.println("\t" + info);
                }
            }
        }

        if (true) {
            System.out.println("---------------");
            ScHandle scHandle = WinApi.openSCManager(WinOpenScManagerDesiredAccess.SC_MANAGER_ENUMERATE_SERVICE);
            List<ServiceStatusInfo> list = WinApi.enumServicesStatus(scHandle, WinEnumServiceStatusServiceType.SERVICE_TYPE_ALL, WinEnumServiceStatusServiceState.SERVICE_STATE_ALL);
            if (list != null) {
                for (ServiceStatusInfo info : list) {
                    System.out.println(info);
                }
            }

            WinApi.closeServiceHandle(scHandle);
        }
    }
}
