package i2f.natives.windows;

import i2f.convert.Converters;
import i2f.graphics.d2.Point;
import i2f.graphics.d2.Size;
import i2f.graphics.d2.shape.Rectangle;
import i2f.natives.windows.consts.*;
import i2f.natives.windows.types.*;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/5/10 8:50
 * @desc
 */
public class WinApi {
    public static String hello() {
        return NativesWindows.hello();
    }

    public static int getSystemMetrics(int metric) {
        return NativesWindows.getSystemMetrics(metric);
    }

    public static Hdc getDC(Hwnd hwnd) {
        long ret = NativesWindows.getDC(hwnd.value());
        return new Hdc(ret);
    }

    public static int releaseDC(Hwnd hwnd, Hdc hdc) {
        return NativesWindows.releaseDC(hwnd.value(), hdc.value());
    }

    public static int getDeviceCaps(Hdc hdc, int index) {
        return NativesWindows.getDeviceCaps(hdc.value(), index);
    }

    public static double getScreenScaleFactor() {
        Hdc hdeskDc = getDC(Hwnd.ZERO);
        int sxDpi = getDeviceCaps(hdeskDc, WinDeviceCapsIndex.LOGPIXELSX);
        int syDpi = getDeviceCaps(hdeskDc, WinDeviceCapsIndex.LOGPIXELSY);
        //normally,sxDpi should equals syDpi
        return sxDpi * 1.0 / 96;//scale rate is 100% when dpi is 96
    }

    public static Hwnd getForegroundWindow() {
        long ret = NativesWindows.getForegroundWindow();
        return new Hwnd(ret);
    }

    public static Hwnd getDesktopWindow() {
        long ret = NativesWindows.getDesktopWindow();
        return new Hwnd(ret);
    }

    public static String getWindowText(Hwnd hwnd) {
        return NativesWindows.getWindowText(hwnd.value());
    }

    public static boolean setWindowText(Hwnd hwnd, String title) {
        return NativesWindows.setWindowText(hwnd.value(), title);
    }

    public static Hwnd windowFromPoint(int x, int y) {
        long ret = NativesWindows.windowFromPoint(x, y);
        return new Hwnd(ret);
    }

    public static Hwnd windowFromPoint(Point p) {
        long ret = NativesWindows.windowFromPoint((int) p.x, (int) p.y);
        return new Hwnd(ret);
    }


    public static Point getCursorPos() {
        int[] ret = NativesWindows.getCursorPos();
        return new Point(ret[0], ret[1]);
    }

    public static Hwnd windowFromCursorPos() {
        Point pos = getCursorPos();
        return windowFromPoint(pos);
    }

    public static int messageBox(Hwnd hwnd, String content, String title, int type) {
        return NativesWindows.messageBox(hwnd.value(), content, title, type);
    }

    public static boolean messageBeep(int type) {
        return NativesWindows.messageBeep(type);
    }

    public static Rectangle getWindowRect(Hwnd hwnd) {
        int[] ret = NativesWindows.getWindowRect(hwnd.value());
        Rectangle rect = new Rectangle(new Point(ret[0], ret[1]),
                new Size(ret[2] - ret[0], ret[3] - ret[1]));
        return rect;
    }

    public static boolean setWindowPos(Hwnd hwnd, Hwnd hwndInsertAfter, int x, int y, int cx, int cy, int flag) {
        return NativesWindows.setWindowPos(hwnd.value(), hwndInsertAfter.value(), x, y, cx, cy, flag);
    }


    public static boolean showWindow(Hwnd hwnd, int cmdShow) {
        return NativesWindows.showWindow(hwnd.value(), cmdShow);
    }

    public static Hwnd setParent(Hwnd hwndChild, Hwnd hwndNewParent) {
        long ret = NativesWindows.setParent(hwndChild.value(), hwndNewParent.value());
        return new Hwnd(ret);
    }

    public static Hwnd findWindow(String className, String windowName) {
        long ret = NativesWindows.findWindow(className, windowName);
        return new Hwnd(ret);
    }

    public static Hwnd getWindow(Hwnd hwnd, int cmd) {
        long ret = NativesWindows.getWindow(hwnd.value(), cmd);
        return new Hwnd(ret);
    }

    public static LResult sendMessage(Hwnd hwnd, int msg, long wParamPtr, long lParamPtr) {
        long ret = NativesWindows.sendMessage(hwnd.value(), msg, wParamPtr, lParamPtr);
        return new LResult(ret);
    }

    public static boolean postMessage(Hwnd hwnd, int msg, long wParamPtr, long lParamPtr) {
        return NativesWindows.postMessage(hwnd.value(), msg, wParamPtr, lParamPtr);
    }

    public static long getWindowThreadProcessId(Hwnd hwnd) {
        return NativesWindows.getWindowThreadProcessId(hwnd.value());
    }

    public static Handle openProcess(long dwDesiredAccess, boolean bInheritHandle, long dwProcessId) {
        long ret = NativesWindows.openProcess(dwDesiredAccess, bInheritHandle, dwProcessId);
        return new Handle(ret);
    }

    public static boolean isInvalidHandle(Handle handle) {
        return NativesWindows.isInvalidHandle(handle.value());
    }

    public static boolean terminateProcess(Handle hProcess, int uExitCode) {
        return NativesWindows.terminateProcess(hProcess.value(), uExitCode);
    }

    public static boolean closeHandle(Handle hObject) {
        return NativesWindows.closeHandle(hObject.value());
    }

    public static int winExec(String cmdLine, int uCmdShow) {
        return NativesWindows.winExec(cmdLine, uCmdShow);
    }

    public static Hwnd getNextWindow(Hwnd hwnd, int cmd) {
        long ret = NativesWindows.getNextWindow(hwnd.value(), cmd);
        return new Hwnd(ret);
    }

    public static String getClassName(Hwnd hwnd) {
        return NativesWindows.getClassName(hwnd.value());
    }

    public static Handle getCurrentProcess() {
        long ret = NativesWindows.getCurrentProcess();
        return new Handle(ret);
    }

    public static long getCurrentProcessId() {
        return NativesWindows.getCurrentProcessId();
    }

    public static Handle openProcessToken(Handle hProcess, long dwDesiredAccess) {
        long ret = NativesWindows.openProcessToken(hProcess.value(), dwDesiredAccess);
        return new Handle(ret);
    }

    public static Luid lookupPrivilegeValue(String pSystemName, String pName) {
        long[] ret = NativesWindows.lookupPrivilegeValue(pSystemName, pName);
        if (ret.length == 0) {
            return null;
        }
        return new Luid(ret[0], ret[1]);
    }

    public static boolean adjustTokenPrivileges(Handle tokenHandle, boolean disableAllPrivileges,
                                                int attributes,
                                                long luidLowPart,
                                                long luidHighPart) {
        return NativesWindows.adjustTokenPrivileges(tokenHandle.value(), disableAllPrivileges,
                attributes,
                luidLowPart,
                luidHighPart);
    }

    public static boolean adjustTokenPrivileges(Handle tokenHandle, boolean disableAllPrivileges,
                                                int attributes, Luid luid) {
        return NativesWindows.adjustTokenPrivileges(tokenHandle.value(), disableAllPrivileges,
                attributes,
                luid.lowPart, luid.highPart);
    }

    public static boolean adjustProcessPrivileges(Handle processHandle,
                                                  String seName,
                                                  boolean enable) {
        return NativesWindows.adjustProcessPrivileges(processHandle.value(), seName, enable);
    }

    public static long getWindowLong(Hwnd hwnd, int index) {
        return NativesWindows.getWindowLong(hwnd.value(), index);
    }

    public static long getClassLong(Hwnd hwnd, int index) {
        return NativesWindows.getClassLong(hwnd.value(), index);
    }

    public static long setWindowLong(Hwnd hwnd, int index, long dwNewLong) {
        return NativesWindows.setWindowLong(hwnd.value(), index, dwNewLong);
    }

    public static long setClassLong(Hwnd hwnd, int index, long dwNewLong) {
        return NativesWindows.setClassLong(hwnd.value(), index, dwNewLong);
    }

    public static LayeredWindowAttributes getLayeredWindowAttributes(Hwnd hwnd) {
        long[] ret = NativesWindows.getLayeredWindowAttributes(hwnd.value());
        if (ret.length == 0) {
            return null;
        }
        return new LayeredWindowAttributes((int) ret[0], (byte) ret[1], ret[2]);
    }

    public static boolean setLayeredWindowAttributes(Hwnd hwnd, int color, byte alpha, long flags) {
        return NativesWindows.setLayeredWindowAttributes(hwnd.value(), color, alpha, flags);
    }

    public static boolean setLayeredWindowAttributes(Hwnd hwnd, LayeredWindowAttributes attributes) {
        return NativesWindows.setLayeredWindowAttributes(hwnd.value(), attributes.color, attributes.alpha, attributes.flag);
    }

    public static void setWindowTransparentColor(Hwnd hWnd, int colorKey) {
        //窗口透明
        long wlong = getWindowLong(hWnd, WinGetWindowLongIndex.GWL_EXSTYLE) | WinWindowStyle.WS_EX_LAYERED;
        setWindowLong(hWnd, WinGetWindowLongIndex.GWL_EXSTYLE, wlong);
        //穿透点击
        setLayeredWindowAttributes(hWnd, colorKey, (byte) 0, WinLayeredWindowAttributesFlag.LWA_COLORKEY);
    }

    public static void setWindowTransparentAlpha(Hwnd hWnd, double transRate) {
        //窗口透明
        long wlong = getWindowLong(hWnd, WinGetWindowLongIndex.GWL_EXSTYLE) | WinWindowStyle.WS_EX_LAYERED;
        setWindowLong(hWnd, WinGetWindowLongIndex.GWL_EXSTYLE, wlong);
        //不穿透点击
        setLayeredWindowAttributes(hWnd, 0x000000, (byte) ((1.0 - transRate) * 255), WinLayeredWindowAttributesFlag.LWA_ALPHA);
    }

    public static void removeWindowTaskBarIcon(Hwnd hWnd) {
        long wlong = getWindowLong(hWnd, WinGetWindowLongIndex.GWL_EXSTYLE) | WinWindowStyle.WS_EX_LAYERED | WinWindowStyle.WS_EX_TOOLWINDOW;//添加透明、任务栏不显示图标属性
        setWindowLong(hWnd, WinGetWindowLongIndex.GWL_EXSTYLE, wlong);
    }

    public static void removeWindowBorder(Hwnd hWnd) {
        //消除边框
        setWindowLong(hWnd, WinGetWindowLongIndex.GWL_STYLE, getWindowLong(hWnd, WinGetWindowLongIndex.GWL_STYLE) - WinWindowStyle.WS_CAPTION);
    }

    public static void setWindowTopMost(Hwnd hWnd) {
        setWindowPos(hWnd, new Hwnd(WinSetWindowPosInsertAfterHwnd.HWND_TOPMOST), 0, 0, 0, 0, WinSetWindowPosFlag.SWP_NOMOVE | WinSetWindowPosFlag.SWP_NOSIZE);
    }

    public static List<Hwnd> enumWindows() {
        long[] ret = NativesWindows.enumWindows();
        List<Hwnd> list = new ArrayList<>(Math.max(ret.length, 32));
        for (long item : ret) {
            list.add(new Hwnd(item));
        }
        return list;
    }

    public static List<Hwnd> enumChildWindows(Hwnd hwndParent) {
        long[] ret = NativesWindows.enumChildWindows(hwndParent.value());
        List<Hwnd> list = new ArrayList<>(Math.max(ret.length, 32));
        for (long item : ret) {
            list.add(new Hwnd(item));
        }
        return list;
    }

    public static List<Hwnd> enumThreadWindows(long threadId) {
        long[] ret = NativesWindows.enumThreadWindows(threadId);
        List<Hwnd> list = new ArrayList<>(Math.max(ret.length, 32));
        for (long item : ret) {
            list.add(new Hwnd(item));
        }
        return list;
    }

    public static List<Hwnd> enumDesktopWindows(HDesk hDesktop) {
        long[] ret = NativesWindows.enumDesktopWindows(hDesktop.value());
        List<Hwnd> list = new ArrayList<>(Math.max(ret.length, 32));
        for (long item : ret) {
            list.add(new Hwnd(item));
        }
        return list;
    }

    public static Hwnd superFindWindow(String szTitle, String szClassName, Hwnd parent) {
        List<Hwnd> list = superFindAllWindow(szTitle, szClassName, parent, true);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public static List<Hwnd> superFindAllWindow(String szTitle, String szClassName, Hwnd parent) {
        return superFindAllWindow(szTitle, szClassName, parent, false);
    }

    public static List<Hwnd> superFindAllWindow(String szTitle, String szClassName, Hwnd parent, boolean matchOne) {
        List<Hwnd> ret = new ArrayList<>();
        String text = getWindowText(parent);
        String className = getClassName(parent);
        if (Objects.equals(szTitle, text) && Objects.equals(szClassName, className)) {
            ret.add(parent);
            if (matchOne) {
                return ret;
            }
        }
        List<Hwnd> list = enumChildWindows(parent);
        for (Hwnd item : list) {
            String itemText = getWindowText(item);
            String itemClassName = getClassName(parent);
            if (Objects.equals(szTitle, itemText) && Objects.equals(szClassName, itemClassName)) {
                ret.add(item);
                if (matchOne) {
                    return ret;
                }
            }
            return superFindAllWindow(szTitle, szClassName, item, matchOne);
        }
        return ret;
    }

    public static boolean postQuitMsg(Hwnd hwnd) {
        return postMessage(hwnd, WinSendMessageMsg.WM_QUIT, 0, 0);
    }

    public static boolean postDestroyMsg(Hwnd hwnd) {
        return postMessage(hwnd, WinSendMessageMsg.WM_DESTROY, 0, 0);
    }

    public static boolean postCloseMsg(Hwnd hwnd) {
        return postMessage(hwnd, WinSendMessageMsg.WM_CLOSE, 0, 0);
    }

    public static boolean postPaintMsg(Hwnd hwnd) {
        return postMessage(hwnd, WinSendMessageMsg.WM_PAINT, 0, 0);
    }

    public static boolean setForegroundWindow(Hwnd hwnd) {
        return NativesWindows.setForegroundWindow(hwnd.value());
    }

    public static Hwnd getActiveWindow() {
        long ret = NativesWindows.getActiveWindow();
        return new Hwnd(ret);
    }

    public static Hwnd setActiveWindow(Hwnd hwnd) {
        long ret = NativesWindows.setActiveWindow(hwnd.value());
        return new Hwnd(ret);
    }

    public static boolean enableWindow(Hwnd hwnd, boolean enable) {
        return NativesWindows.enableWindow(hwnd.value(), enable);
    }

    public static boolean isWindowEnabled(Hwnd hwnd) {
        return NativesWindows.isWindowEnabled(hwnd.value());
    }

    public static Hdc getWindowDC(Hwnd hwnd) {
        long ret = NativesWindows.getWindowDC(hwnd.value());
        return new Hdc(ret);
    }

    public static boolean isWindowVisible(Hwnd hwnd) {
        return NativesWindows.isWindowVisible(hwnd.value());
    }

    public static boolean isWindowUnicode(Hwnd hwnd) {
        return NativesWindows.isWindowUnicode(hwnd.value());
    }

    public static boolean isChild(Hwnd hwndParent, Hwnd hwnd) {
        return NativesWindows.isChild(hwndParent.value(), hwnd.value());
    }

    public static boolean isIconic(Hwnd hwnd) {
        return NativesWindows.isIconic(hwnd.value());
    }

    public static boolean openIcon(Hwnd hwnd) {
        return NativesWindows.openIcon(hwnd.value());
    }

    public static boolean isZoomed(Hwnd hwnd) {
        return NativesWindows.isZoomed(hwnd.value());
    }

    public static Hwnd getParent(Hwnd hwnd) {
        long ret = NativesWindows.getParent(hwnd.value());
        return new Hwnd(ret);
    }

    public static Hwnd findWindowEx(Hwnd hwndParent, Hwnd hwndChildAfter, String className, String windowText) {
        long ret = NativesWindows.findWindowEx(hwndParent.value(), hwndChildAfter.value(), className, windowText);
        return new Hwnd(ret);
    }

    public static Hwnd windowFromDC(Hdc hdc) {
        long ret = NativesWindows.windowFromDC(hdc.value());
        return new Hwnd(ret);
    }

    public static boolean moveWindow(Hwnd hwnd, int x, int y, int width, int height, boolean repaint) {
        return NativesWindows.moveWindow(hwnd.value(), x, y, width, height, repaint);
    }

    public static boolean moveWindow(Hwnd hwnd, Rectangle rect, boolean repaint) {
        return NativesWindows.moveWindow(hwnd.value(), (int) rect.point.x, (int) rect.point.y, (int) rect.size.dx, (int) rect.size.dy, repaint);
    }

    public static Handle createToolhelp32Snapshot(long dwFlags, long processId) {
        long ret = NativesWindows.createToolhelp32Snapshot(dwFlags, processId);
        return new Handle(ret);
    }

    public static Handle createToolhelp32Snapshot(long dwFlags) {
        long ret = NativesWindows.createToolhelp32Snapshot(dwFlags, 0);
        return new Handle(ret);
    }

    public static ProcessEntry32 parseProcessEntry32(String str) {
        if (str == null) {
            return null;
        }
        Map<String, String> map = new LinkedHashMap<>();
        String[] arr = str.split(";#;");
        for (String item : arr) {
            String[] pair = item.split(":", 2);
            map.put(pair[0], pair[1]);
        }
        ProcessEntry32 ret = new ProcessEntry32();
        ret.dwSize = Converters.parseInt(map.get("dwSize"), 0);
        ret.cntUsage = Converters.parseInt(map.get("cntUsage"), 0);
        ret.th32ProcessID = Converters.parseLong(map.get("th32ProcessID"), 0);
        ret.th32DefaultHeapID = Converters.parseLong(map.get("th32DefaultHeapID"), 0);
        ret.th32ModuleID = Converters.parseLong(map.get("th32ModuleID"), 0);
        ret.cntThreads = Converters.parseInt(map.get("cntThreads"), 0);
        ret.th32ParentProcessID = Converters.parseLong(map.get("th32ParentProcessID"), 0);
        ret.pcPriClassBase = Converters.parseLong(map.get("pcPriClassBase"), 0);
        ret.dwFlags = Converters.parseLong(map.get("dwFlags"), 0);
        ret.szExeFile = map.get("szExeFile");
        return ret;
    }

    public static ProcessEntry32 process32First(Handle hSnapshot) {
        String str = NativesWindows.process32First(hSnapshot.value());
        return parseProcessEntry32(str);
    }

    public static ProcessEntry32 process32Next(Handle hSnapshot) {
        String str = NativesWindows.process32Next(hSnapshot.value());
        return parseProcessEntry32(str);
    }

    public static List<ProcessEntry32> listProcess32() {
        return listProcess32(0);
    }

    public static List<ProcessEntry32> listProcess32(long processId) {
        List<ProcessEntry32> ret = new ArrayList<>();
        Handle hSnapshot = WinApi.createToolhelp32Snapshot(WinCreateToolhelo32SnapshotFlag.TH32CS_SNAPPROCESS, processId);
        if (!WinApi.isInvalidHandle(hSnapshot)) {
            ProcessEntry32 info = WinApi.process32First(hSnapshot);
            while (info != null) {
                ret.add(info);
                info = WinApi.process32Next(hSnapshot);
            }
            WinApi.closeHandle(hSnapshot);
        }
        return ret;
    }


    public static ModuleEntry32 parseModuleEntry32(String str) {
        if (str == null) {
            return null;
        }
        Map<String, String> map = new LinkedHashMap<>();
        String[] arr = str.split(";#;");
        for (String item : arr) {
            String[] pair = item.split(":", 2);
            map.put(pair[0], pair[1]);
        }
        ModuleEntry32 ret = new ModuleEntry32();
        ret.dwSize = Converters.parseInt(map.get("dwSize"), 0);
        ret.th32ModuleID = Converters.parseLong(map.get("th32ModuleID"), 0);
        ret.th32ProcessID = Converters.parseLong(map.get("th32ProcessID"), 0);
        ret.GlblcntUsage = Converters.parseInt(map.get("GlblcntUsage"), 0);
        ret.ProccntUsage = Converters.parseInt(map.get("ProccntUsage"), 0);
        ret.modBaseSize = Converters.parseLong(map.get("modBaseSize"), 0);
        ret.hModule = new HModule(Converters.parseLong(map.get("hModule"), 0));
        ret.szModule = map.get("szModule");
        ret.szExePath = map.get("szExePath");
        return ret;
    }


    public static ModuleEntry32 module32First(Handle hSnapshot) {
        String str = NativesWindows.module32First(hSnapshot.value());
        return parseModuleEntry32(str);
    }

    public static ModuleEntry32 module32Next(Handle hSnapshot) {
        String str = NativesWindows.module32Next(hSnapshot.value());
        return parseModuleEntry32(str);
    }

    public static List<ModuleEntry32> listModule32() {
        return listModule32(0);
    }

    public static List<ModuleEntry32> listModule32(long processId) {
        List<ModuleEntry32> ret = new ArrayList<>();
        Handle hSnapshot = WinApi.createToolhelp32Snapshot(WinCreateToolhelo32SnapshotFlag.TH32CS_SNAPMODULE, processId);
        if (!WinApi.isInvalidHandle(hSnapshot)) {
            ModuleEntry32 info = WinApi.module32First(hSnapshot);
            while (info != null) {
                ret.add(info);
                info = WinApi.module32Next(hSnapshot);
            }
            WinApi.closeHandle(hSnapshot);
        }
        return ret;
    }

    public static ThreadEntry32 parseThreadEntry32(String str) {
        if (str == null) {
            return null;
        }
        Map<String, String> map = new LinkedHashMap<>();
        String[] arr = str.split(";#;");
        for (String item : arr) {
            String[] pair = item.split(":", 2);
            map.put(pair[0], pair[1]);
        }
        ThreadEntry32 ret = new ThreadEntry32();
        ret.dwSize = Converters.parseInt(map.get("dwSize"), 0);
        ret.cntUsage = Converters.parseInt(map.get("cntUsage"), 0);
        ret.th32ThreadID = Converters.parseLong(map.get("th32ThreadID"), 0);
        ret.th32OwnerProcessID = Converters.parseLong(map.get("th32OwnerProcessID"), 0);
        ret.tpBasePri = Converters.parseLong(map.get("tpBasePri"), 0);
        ret.tpDeltaPri = Converters.parseLong(map.get("tpDeltaPri"), 0);
        ret.dwFlags = Converters.parseLong(map.get("dwFlags"), 0);

        return ret;
    }


    public static ThreadEntry32 thread32First(Handle hSnapshot) {
        String str = NativesWindows.thread32First(hSnapshot.value());
        return parseThreadEntry32(str);
    }

    public static ThreadEntry32 thread32Next(Handle hSnapshot) {
        String str = NativesWindows.thread32Next(hSnapshot.value());
        return parseThreadEntry32(str);
    }

    public static List<ThreadEntry32> listThread32() {
        return listThread32(0);
    }

    public static List<ThreadEntry32> listThread32(long processId) {
        List<ThreadEntry32> ret = new ArrayList<>();
        Handle hSnapshot = WinApi.createToolhelp32Snapshot(WinCreateToolhelo32SnapshotFlag.TH32CS_SNAPTHREAD, processId);
        if (!WinApi.isInvalidHandle(hSnapshot)) {
            ThreadEntry32 info = WinApi.thread32First(hSnapshot);
            while (info != null) {
                ret.add(info);
                info = WinApi.thread32Next(hSnapshot);
            }
            WinApi.closeHandle(hSnapshot);
        }
        return ret;
    }

    public static long getExitCodeProcess(Handle hProcess) {
        return NativesWindows.getExitCodeProcess(hProcess.value());
    }


    public static long getExitCodeThread(Handle hThread) {
        return NativesWindows.getExitCodeThread(hThread.value());
    }

    public static long getLastError() {
        return NativesWindows.getLastError();
    }

    public static void setLastError(long errCode) {
        NativesWindows.setLastError(errCode);
    }

    public static void setLastErrorEx(long errCode, long type) {
        NativesWindows.setLastErrorEx(errCode, type);
    }

    public static Handle openThread(long dwDesiredAccess, boolean inheritHandle, long dwThreadId) {
        long ret = NativesWindows.openThread(dwDesiredAccess, inheritHandle, dwThreadId);
        return new Handle(ret);
    }

    public static long suspendThread(Handle hThread) {
        return NativesWindows.suspendThread(hThread.value());
    }

    public static long resumeThread(Handle hThread) {
        return NativesWindows.resumeThread(hThread.value());
    }

    public static List<String> getLogicalDriveStrings() {
        String str = NativesWindows.getLogicalDriveStrings();
        String[] arr = str.split(";#;");
        List<String> ret = new ArrayList<>();
        for (String item : arr) {
            ret.add(item);
        }
        return ret;
    }

    public static long getDriveType(String rootPathName) {
        return NativesWindows.getDriveType(rootPathName);
    }

    public static long getFileAttributes(String fileName) {
        return NativesWindows.getFileAttributes(fileName);
    }

    public static boolean setFileAttributes(String fileName, long attribute) {
        return NativesWindows.setFileAttributes(fileName, attribute);
    }

    public static int deleteFileToRecycleBin(String fileName) {
        return NativesWindows.deleteFileToRecycleBin(fileName);
    }

    public static Rectangle parseRect(String str) {
        if (str == null) {
            return null;
        }
        String[] arr = str.split(",");
        int[] vals = new int[4];
        for (int i = 0; i < 4; i++) {
            vals[i] = Converters.parseInt(arr[i], 0);
        }
        return new Rectangle(vals[0], vals[1],
                vals[2] - vals[0], vals[3] - vals[1]);
    }

    public static WindowInfo parseWindowInfo(String str) {
        if (str == null) {
            return null;
        }
        Map<String, String> map = new LinkedHashMap<>();
        String[] arr = str.split(";#;");
        for (String item : arr) {
            String[] pair = item.split(":", 2);
            map.put(pair[0], pair[1]);
        }
        WindowInfo ret = new WindowInfo();
        ret.cbSize = Converters.parseInt(map.get("cbSize"), 0);
        ret.rcWindow = parseRect(map.get("rcWindow"));
        ret.rcClient = parseRect(map.get("rcClient"));
        ret.dwStyle = Converters.parseLong(map.get("dwStyle"), 0);
        ret.dwExStyle = Converters.parseLong(map.get("dwExStyle"), 0);
        ret.dwWindowStatus = Converters.parseLong(map.get("dwWindowStatus"), 0);
        ret.cxWindowBorders = Converters.parseLong(map.get("cxWindowBorders"), 0);
        ret.cyWindowBorders = Converters.parseLong(map.get("cyWindowBorders"), 0);
        ret.atomWindowType = Converters.parseInt(map.get("atomWindowType"), 0);
        ret.wCreatorVersion = Converters.parseInt(map.get("wCreatorVersion"), 0);
        return ret;
    }

    public static WindowInfo getWindowInfo(Hwnd hwnd) {
        String str = NativesWindows.getWindowInfo(hwnd.value());
        return parseWindowInfo(str);
    }


    public static Point clientToScreen(Hwnd hwnd, Point p) {
        int[] ret = NativesWindows.clientToScreen(hwnd.value(), (int) p.x, (int) p.y);
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }

    public static Point screenToClient(Hwnd hwnd, Point p) {
        int[] ret = NativesWindows.screenToClient(hwnd.value(), (int) p.x, (int) p.y);
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }


    public static void keyboardEvent(int bVk, int bScan, long dwFlags) {
        NativesWindows.keyboardEvent(bVk, bScan, dwFlags);
    }

    public static void keyboardDownEvent(int bVk) {
        NativesWindows.keyboardEvent(bVk, 0, WinKeyboardEventFlag.KEYEVENTF_KEYDOWN);
    }

    public static void keyboardUpEvent(int bVk) {
        NativesWindows.keyboardEvent(bVk, 0, WinKeyboardEventFlag.KEYEVENTF_KEYUP);
    }

    public static void keyboardClickEvent(int bVk) {
        keyboardDownEvent(bVk);
        sleep(5);
        keyboardUpEvent(bVk);
    }

    public static void keyboardCombineEvent(int... bVks) {
        for (int bVk : bVks) {
            keyboardDownEvent(bVk);
            sleep(5);
        }
        for (int bVk : bVks) {
            sleep(5);
            keyboardUpEvent(bVk);
        }
    }

    public static void keyboardCopyEvent() {
        keyboardCombineEvent(WinKeyboardEventVk.VK_CONTROL, 'C');
    }

    public static void keyboardPasteEvent() {
        keyboardCombineEvent(WinKeyboardEventVk.VK_CONTROL, 'V');
    }

    public static void keyboardCutEvent() {
        keyboardCombineEvent(WinKeyboardEventVk.VK_CONTROL, 'X');
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {

        }
    }

    public static void mouseEvent(long dwFlags, int dx, int dy, int dwData) {
        NativesWindows.mouseEvent(dwFlags, dx, dy, dwData);
    }

    public static void mouseEvent(long dwFlag) {
        NativesWindows.mouseEvent(dwFlag, 0, 0, 0);
    }

    public static void mouseLeftDown() {
        NativesWindows.mouseEvent(WinMouseEventFlag.MOUSEEVENTF_LEFTDOWN, 0, 0, 0);
    }

    public static void mouseLeftUp() {
        NativesWindows.mouseEvent(WinMouseEventFlag.MOUSEEVENTF_LEFTUP, 0, 0, 0);
    }

    public static void mouseLeftClick() {
        mouseLeftDown();
        sleep(5);
        mouseLeftUp();
    }

    public static void mouseRightDown() {
        NativesWindows.mouseEvent(WinMouseEventFlag.MOUSEEVENTF_RIGHTDOWN, 0, 0, 0);
    }

    public static void mouseRightUp() {
        NativesWindows.mouseEvent(WinMouseEventFlag.MOUSEEVENTF_RIGHTUP, 0, 0, 0);
    }

    public static void mouseRightClick() {
        mouseRightDown();
        sleep(5);
        mouseRightUp();
    }

    public static void mouseMove(int dx, int dy) {
        NativesWindows.mouseEvent(WinMouseEventFlag.MOUSEEVENTF_MOVE, dx, dy, 0);
    }

    public static void mouseWheel(int delta) {
        NativesWindows.mouseEvent(WinMouseEventFlag.MOUSEEVENTF_WHEEL, 0, 0, delta);
    }

    public static boolean setCursorPos(int x, int y) {
        return NativesWindows.setCursorPos(x, y);
    }

    public static boolean setCursorPos(Point p) {
        return NativesWindows.setCursorPos((int) p.x, (int) p.y);
    }

}
