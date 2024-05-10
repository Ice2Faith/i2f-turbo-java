package i2f.natives.windows.test;

import i2f.natives.windows.WinApi;
import i2f.natives.windows.consts.WinCreateToolhelo32SnapshotFlag;
import i2f.natives.windows.consts.WinDeviceCapsIndex;
import i2f.natives.windows.consts.WinSystemMetrics;
import i2f.natives.windows.types.*;

import java.util.List;

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
    }
}
