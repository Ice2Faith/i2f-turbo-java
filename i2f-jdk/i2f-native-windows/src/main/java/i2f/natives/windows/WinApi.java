package i2f.natives.windows;

import i2f.convert.Converters;
import i2f.graphics.d2.Point;
import i2f.graphics.d2.Size;
import i2f.graphics.d2.shape.Rectangle;
import i2f.natives.core.MallocPtr;
import i2f.natives.core.NewArrayPtr;
import i2f.natives.core.NewPtr;
import i2f.natives.windows.consts.WinConsts;
import i2f.natives.windows.consts.access.WinGenericRights;
import i2f.natives.windows.consts.device.*;
import i2f.natives.windows.consts.file.WinFileAttribute;
import i2f.natives.windows.consts.file.WinFileCreationDisposition;
import i2f.natives.windows.consts.file.WinFileShareMode;
import i2f.natives.windows.consts.gdi.WinBitmapInfoHeaderCompression;
import i2f.natives.windows.consts.gdi.WinCreateDIBSectionUsage;
import i2f.natives.windows.consts.gdi.WinGdiPenStyle;
import i2f.natives.windows.consts.process.WinCreateToolhelo32SnapshotFlag;
import i2f.natives.windows.consts.process.WinLookupPrivilegeName;
import i2f.natives.windows.consts.process.WinOpenProcessDesiredAccess;
import i2f.natives.windows.consts.process.WinOpenThreadDesiredAccess;
import i2f.natives.windows.consts.register.*;
import i2f.natives.windows.consts.service.*;
import i2f.natives.windows.consts.shell.WinShEmptyRecycleBinFlag;
import i2f.natives.windows.consts.shell.WinShFileOperationFlag;
import i2f.natives.windows.consts.shell.WinShFileOperationFunc;
import i2f.natives.windows.consts.shell.WinShGetSpecialFolderLocationCsidl;
import i2f.natives.windows.consts.system.WinExitWindowsFlag;
import i2f.natives.windows.consts.system.WinOsVersionInfoPlatformId;
import i2f.natives.windows.consts.winapp.WinAppCallbacker;
import i2f.natives.windows.consts.window.*;
import i2f.natives.windows.types.Handle;
import i2f.natives.windows.types.WcharPtr;
import i2f.natives.windows.types.com.CoIUnknownPtr;
import i2f.natives.windows.types.com.CoTaskPtr;
import i2f.natives.windows.types.com.LpItemIdList;
import i2f.natives.windows.types.file.DiskFreeSpaceExInfo;
import i2f.natives.windows.types.file.FileAttributeExInfo;
import i2f.natives.windows.types.file.FileHandleInformation;
import i2f.natives.windows.types.gdi.*;
import i2f.natives.windows.types.process.*;
import i2f.natives.windows.types.register.HKey;
import i2f.natives.windows.types.register.RegEnumKeyExInfo;
import i2f.natives.windows.types.register.RegEnumValueInfo;
import i2f.natives.windows.types.register.RegValueInfo;
import i2f.natives.windows.types.service.ScHandle;
import i2f.natives.windows.types.service.ServiceStatusInfo;
import i2f.natives.windows.types.system.MemoryStatusEx;
import i2f.natives.windows.types.system.OsVersionInfo;
import i2f.natives.windows.types.winapp.BitmapDcPtr;
import i2f.natives.windows.types.window.*;

import java.io.File;
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

    public static boolean kbHit() {
        return NativesWindows.kbHit();
    }

    public static int getCh() {
        return NativesWindows.getCh();
    }

    public static void flushStdin() {
        NativesWindows.flushStdin();
    }

    public static void flushStdout() {
        NativesWindows.flushStdout();
    }

    public static int rgb(int r, int g, int b) {
        return NativesWindows.rgb(r, g, b);
    }

    public static int rgbOf(int r, int g, int b) {
        return ((b & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (r & 0x0ff);
    }

    public static int[] toRgb(int color) {
        return new int[]{(color & 0x0ff), ((color >>> 8) & 0x0ff), ((color >>> 16) & 0x0ff)};
    }

    public static WcharPtr envStringToWcharPtr(String str) {
        long ret = NativesWindows.envStringToWcharPtr(str);
        return new WcharPtr(ret);
    }

    public static void envFreeWcharPtr(WcharPtr ptr) {
        NativesWindows.envFreeWcharPtr(ptr.value());
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

    public static boolean adjustProcessSeDebugPrivileges(Handle processHandle) {
        return adjustProcessPrivileges(processHandle, WinLookupPrivilegeName.SE_DEBUG_NAME, true);
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
        Map<String, String> map = getJniStringMap(str);
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

    public static Map<String, String> getJniStringMap(String str) {
        Map<String, String> map = new LinkedHashMap<>();
        String[] arr = str.split(";#;");
        for (String item : arr) {
            String[] pair = item.split(":", 2);
            map.put(pair[0], pair[1]);
        }
        return map;
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
        Map<String, String> map = getJniStringMap(str);
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
        Map<String, String> map = getJniStringMap(str);
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

    public static boolean suspendProcess(long processId) {
        List<ThreadEntry32> list = listThread32(processId);
        if (list == null || list.isEmpty()) {
            return false;
        }
        for (ThreadEntry32 item : list) {
            if (item.th32OwnerProcessID == processId) {
                Handle hThread = openThread(WinOpenThreadDesiredAccess.THREAD_SUSPEND_RESUME, false, item.th32ThreadID);
                long ret = suspendThread(hThread);
                closeHandle(hThread);
            }
        }
        return true;
    }

    public static boolean resumeProcess(long processId) {
        List<ThreadEntry32> list = listThread32(processId);
        if (list == null || list.isEmpty()) {
            return false;
        }
        for (ThreadEntry32 item : list) {
            if (item.th32OwnerProcessID == processId) {
                Handle hThread = openThread(WinOpenThreadDesiredAccess.THREAD_SUSPEND_RESUME, false, item.th32ThreadID);
                long ret = resumeThread(hThread);
                closeHandle(hThread);
            }
        }
        return true;
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
        Map<String, String> map = getJniStringMap(str);
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

    public static void sleep(int millSeconds) {
        NativesWindows.sleep(millSeconds);
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

    public static HKey regOpenKeyEx(HKey hkey, String subKey, long ulOptions, long samDesired) {
        long ret = NativesWindows.regOpenKeyEx(hkey.value(), subKey, ulOptions, samDesired);
        return new HKey(ret);
    }

    public static HKey regOpenKeyEx(HKey hkey, String subKey) {
        long ret = NativesWindows.regOpenKeyEx(hkey.value(), subKey, 0, WinRegOpenKeySamDesired.KEY_ALL_ACCESS);
        return new HKey(ret);
    }

    public static HKey regOpenDefaultBootKey() {
        long ret = NativesWindows.regOpenKeyEx(WinRegOpenKeyHkey.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run\\", 0, WinRegOpenKeySamDesired.KEY_ALL_ACCESS);
        return new HKey(ret);
    }

    public static RegEnumValueInfo parseRegEnumValueInfo(String str) {
        if (str == null) {
            return null;
        }

        Map<String, String> map = getJniStringMap(str);
        RegEnumValueInfo ret = new RegEnumValueInfo();
        ret.index = Converters.parseInt(map.get("index"), 0);
        ret.valueName = map.get("valueName");
        ret.type = Converters.parseInt(map.get("type"), 0);
        ret.valueData = map.get("valueData");

        return ret;
    }

    public static RegEnumValueInfo regEnumValue(HKey hkey, int index) {
        String str = NativesWindows.regEnumValue(hkey.value(), index);
        return parseRegEnumValueInfo(str);
    }

    public static List<RegEnumValueInfo> regEnumValues(HKey hkey) {
        List<RegEnumValueInfo> ret = new ArrayList<>();
        int index = 0;
        while (true) {
            RegEnumValueInfo info = regEnumValue(hkey, index);
            if (info == null) {
                break;
            }
            ret.add(info);
            index++;
        }
        return ret;
    }

    public static boolean regCloseKey(HKey hkey) {
        return NativesWindows.regCloseKey(hkey.value());
    }

    public static RegEnumKeyExInfo parseRegEnumKeyExInfo(String str) {
        if (str == null) {
            return null;
        }

        Map<String, String> map = getJniStringMap(str);
        RegEnumKeyExInfo ret = new RegEnumKeyExInfo();
        ret.index = Converters.parseInt(map.get("index"), 0);
        ret.keyName = map.get("keyName");
        ret.className = map.get("className");
        ret.lastWriteTime = Converters.parseLong(map.get("lastWriteTime"), 0);

        return ret;
    }

    public static RegEnumKeyExInfo regEnumKeyEx(HKey hkey, int index) {
        String str = NativesWindows.regEnumKeyEx(hkey.value(), index);
        return parseRegEnumKeyExInfo(str);
    }

    public static List<RegEnumKeyExInfo> regEnumKeys(HKey hkey) {
        List<RegEnumKeyExInfo> ret = new ArrayList<>();
        int index = 0;
        while (true) {
            RegEnumKeyExInfo info = regEnumKeyEx(hkey, index);
            if (info == null) {
                break;
            }
            ret.add(info);
            index++;
        }
        return ret;
    }

    public static HKey regCreateKeyEx(HKey hkey, String subKey, String className, long dwOptions, long samDesired) {
        long ret = NativesWindows.regCreateKeyEx(hkey.value(), subKey, className, dwOptions, samDesired);
        return new HKey(ret);
    }

    public static HKey regCreateKeyEx(HKey hkey, String subKey, String className, long samDesired) {
        long ret = NativesWindows.regCreateKeyEx(hkey.value(), subKey, className, WinRegOption.REG_OPTION_NON_VOLATILE, samDesired);
        return new HKey(ret);
    }

    public static boolean regDeleteKey(HKey hkey, String subKey) {
        return NativesWindows.regDeleteKey(hkey.value(), subKey);
    }

    public static RegValueInfo parseRegValueInfo(String str) {
        if (str == null) {
            return null;
        }

        Map<String, String> map = getJniStringMap(str);
        RegValueInfo ret = new RegValueInfo();
        ret.type = Converters.parseInt(map.get("type"), 0);
        ret.valueData = map.get("valueData");

        return ret;
    }

    public static RegValueInfo regQueryValueEx(HKey hkey, String valueName) {
        String str = NativesWindows.regQueryValueEx(hkey.value(), valueName);
        return parseRegValueInfo(str);
    }

    public static boolean regSetValueEx(HKey hkey, String valueName, long type, String data) {
        return NativesWindows.regSetValueEx(hkey.value(), valueName, type, data);
    }

    public static boolean regSetValueEx(HKey hkey, String valueName, RegValueInfo info) {
        return NativesWindows.regSetValueEx(hkey.value(), valueName, info.type, info.valueData);
    }

    public static boolean regSetValueExString(HKey hkey, String valueName, String data) {
        return NativesWindows.regSetValueEx(hkey.value(), valueName, WinRegValueType.REG_SZ, data);
    }

    public static boolean regSetValueExInteger(HKey hkey, String valueName, int data) {
        return NativesWindows.regSetValueEx(hkey.value(), valueName, WinRegValueType.REG_DWORD, String.valueOf(data));
    }

    public static boolean regSetValueExLong(HKey hkey, String valueName, long data) {
        return NativesWindows.regSetValueEx(hkey.value(), valueName, WinRegValueType.REG_QWORD, String.valueOf(data));
    }

    public static boolean regDeleteValue(HKey hkey, String valueName) {
        return NativesWindows.regDeleteValue(hkey.value(), valueName);
    }

    public static Map<String, List<RegEnumValueInfo>> regAllBootValues() {
        Map<String, List<RegEnumValueInfo>> ret = new LinkedHashMap<>();
        for (int i = 0; i < WinRegBootKeys.ROOT_HKEYS.length; i++) {
            HKey hKey = regOpenKeyEx(new HKey(WinRegBootKeys.ROOT_HKEYS[i]), WinRegBootKeys.BOOT_PATHS[i], 0, WinRegOpenKeySamDesired.KEY_READ);
            List<RegEnumValueInfo> list = regEnumValues(hKey);
            if (!list.isEmpty()) {
                String key = WinRegOpenKeyHkey.NAME_MAP.get(WinRegBootKeys.ROOT_HKEYS[i]) + "\\" + WinRegBootKeys.BOOT_PATHS[i];
                ret.put(key, list);
            }
            regCloseKey(hKey);
        }
        return ret;
    }

    public static boolean regAddBootItem(String valueName, String valueData) {
        HKey hKey = regOpenKeyEx(new HKey(WinRegBootKeys.ROOT_HKEYS[0]), WinRegBootKeys.BOOT_PATHS[0], 0, WinRegOpenKeySamDesired.KEY_WRITE);
        boolean ret = regSetValueExString(hKey, valueName, valueData);
        regCloseKey(hKey);
        return ret;
    }

    public static boolean regDeleteBootItem(String valueName) {
        HKey hKey = regOpenKeyEx(new HKey(WinRegBootKeys.ROOT_HKEYS[0]), WinRegBootKeys.BOOT_PATHS[0], 0, WinRegOpenKeySamDesired.KEY_WRITE);
        boolean ret = regDeleteValue(hKey, valueName);
        regCloseKey(hKey);
        return ret;
    }

    public static ScHandle openSCManager(String machineName, String databaseName, long dwDesiredAccess) {
        long ret = NativesWindows.openSCManager(machineName, databaseName, dwDesiredAccess);
        return new ScHandle(ret);
    }

    public static ScHandle openSCManager(long dwDesiredAccess) {
        return openSCManager(null, null, dwDesiredAccess);
    }

    public static ServiceStatusInfo parseEnumServiceStatusLine(String str) {
        if (str == null) {
            return null;
        }
        Map<String, String> map = getJniStringMap(str);
        ServiceStatusInfo ret = new ServiceStatusInfo();
        ret.serviceName = map.get("serviceName");
        ret.displayName = map.get("displayName");
        ret.currentState = Converters.parseInt(map.get("currentState"), 0);
        ret.serviceType = Converters.parseInt(map.get("serviceType"), 0);
        ret.controlsAccepted = Converters.parseLong(map.get("controlsAccepted"), 0);
        ret.win32ExitCode = Converters.parseLong(map.get("win32ExitCode"), 0);
        ret.serviceSpecificExitCode = Converters.parseLong(map.get("serviceSpecificExitCode"), 0);
        ret.checkPoint = Converters.parseLong(map.get("checkPoint"), 0);
        ret.waitHint = Converters.parseLong(map.get("waitHint"), 0);
        return ret;
    }

    public static List<ServiceStatusInfo> parseEnumServiceStatus(String str) {
        if (str == null) {
            return null;
        }
        String[] lines = str.split(";\\$;");
        List<ServiceStatusInfo> ret = new ArrayList<>();
        for (String line : lines) {
            ServiceStatusInfo info = parseEnumServiceStatusLine(line);
            if (info != null) {
                ret.add(info);
            }
        }
        return ret;
    }

    public static List<ServiceStatusInfo> enumServicesStatus(ScHandle hScm, long serviceType, long serviceState) {
        String str = NativesWindows.enumServicesStatus(hScm.value(), serviceType, serviceState);
        return parseEnumServiceStatus(str);
    }

    public static List<ServiceStatusInfo> enumServicesStatus(long serviceType, long serviceState) {
        ScHandle hScm = openSCManager(WinOpenScManagerDesiredAccess.SC_MANAGER_ENUMERATE_SERVICE);
        if (hScm.isZero()) {
            return null;
        }
        List<ServiceStatusInfo> ret = enumServicesStatus(hScm, serviceType, serviceState);
        closeServiceHandle(hScm);
        return ret;
    }

    public static boolean closeServiceHandle(ScHandle hScm) {
        return NativesWindows.closeServiceHandle(hScm.value());
    }

    public static ScHandle openService(ScHandle hScm, String serviceName, long dwDesiredAccess) {
        long ret = NativesWindows.openService(hScm.value(), serviceName, dwDesiredAccess);
        return new ScHandle(ret);
    }

    public static boolean startService(ScHandle hService, String[] serviceArgVectors) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < serviceArgVectors.length; i++) {
            if (i > 0) {
                builder.append("\n");
            }
            String str = serviceArgVectors[i];
            if (str == null) {
                str = "";
            }
            str = str.replaceAll("\n", "");
            builder.append(str);
        }
        String args = builder.toString();
        return NativesWindows.startService(hService.value(), serviceArgVectors.length, args);
    }

    public static boolean startService(ScHandle hService) {
        return NativesWindows.startService(hService.value(), 0, null);
    }

    public static boolean controlService(ScHandle hService, int dwControl) {
        return NativesWindows.controlService(hService.value(), dwControl);
    }

    public static boolean stopService(ScHandle hService) {
        return NativesWindows.controlService(hService.value(), WinControlServiceControl.SERVICE_CONTROL_STOP);
    }

    public static boolean deleteService(ScHandle hService) {
        return NativesWindows.deleteService(hService.value());
    }

    public static ServiceStatusInfo queryServiceStatus(ScHandle hService) {
        String str = NativesWindows.queryServiceStatus(hService.value());
        return parseEnumServiceStatusLine(str);
    }

    public static boolean startService(String serviceName, String[] startServiceArgs) {
        ScHandle hScm = openSCManager(WinOpenScManagerDesiredAccess.SC_MANAGER_ALL_ACCESS);
        if (hScm.isZero()) {
            return false;
        }
        ScHandle hService = openService(hScm, serviceName, WinOpenServiceDesiredAccess.SERVICE_START);
        if (hService.isZero()) {
            closeServiceHandle(hScm);
            return false;
        }
        boolean ok = startService(hService, startServiceArgs);
        closeServiceHandle(hService);
        closeServiceHandle(hScm);
        return ok;
    }

    public static boolean stopService(String serviceName) {
        ScHandle hScm = openSCManager(WinOpenScManagerDesiredAccess.SC_MANAGER_ALL_ACCESS);
        if (hScm.isZero()) {
            return false;
        }
        ScHandle hService = openService(hScm, serviceName, WinOpenServiceDesiredAccess.SERVICE_STOP);
        if (hService.isZero()) {
            closeServiceHandle(hScm);
            return false;
        }
        boolean ok = stopService(hService);
        closeServiceHandle(hService);
        closeServiceHandle(hScm);
        return ok;
    }

    public static ScHandle createService(ScHandle hScm,
                                         String serviceName,
                                         String displayName,
                                         long dwDesiredAccess,
                                         long dwServiceType,
                                         long dwStartType,
                                         long dwErrorControl,
                                         String binaryPathName,
                                         String loadOrderGroup,
                                         String dependencies,
                                         String serviceStartName,
                                         String password) {
        long ret = NativesWindows.createService(hScm.value(), serviceName, displayName,
                dwDesiredAccess, dwServiceType, dwStartType, dwErrorControl,
                binaryPathName, loadOrderGroup, dependencies,
                serviceStartName, password);
        return new ScHandle(ret);
    }

    public static ScHandle createService(ScHandle hScm,
                                         String serviceName,
                                         String displayName,
                                         String binaryPathName) {
        long ret = NativesWindows.createService(hScm.value(), serviceName, displayName,
                WinOpenScManagerDesiredAccess.SC_MANAGER_ALL_ACCESS,
                WinEnumServiceStatusServiceType.SERVICE_WIN32_SHARE_PROCESS,
                WinCreateServiceStartType.SERVICE_AUTO_START,
                WinCreateServiceErrorControl.SERVICE_ERROR_NORMAL,
                binaryPathName, null, null,
                null, null);
        return new ScHandle(ret);
    }

    public static Handle createFile(String filePath,
                                    long dwDesiredAccess,
                                    long dwShareMode,
                                    long dwCreationDisposition,
                                    long dwFlagAndAttributes) {
        long ret = NativesWindows.createFile(filePath, dwDesiredAccess, dwShareMode, dwCreationDisposition, dwFlagAndAttributes);
        return new Handle(ret);
    }

    public static Handle openFileExist(String filePath) {
        return createFile(filePath,
                WinGenericRights.GENERIC_READ | WinGenericRights.GENERIC_WRITE,
                WinFileShareMode.FILE_SHARE_READ,
                WinFileCreationDisposition.OPEN_EXISTING,
                WinFileAttribute.FILE_ATTRIBUTE_NORMAL);
    }

    public static Handle openFileExistOrCreate(String filePath) {
        return createFile(filePath,
                WinGenericRights.GENERIC_READ | WinGenericRights.GENERIC_WRITE,
                WinFileShareMode.FILE_SHARE_READ,
                WinFileCreationDisposition.CREATE_ALWAYS,
                WinFileAttribute.FILE_ATTRIBUTE_NORMAL);
    }

    public static Handle openFileReadOnly(String filePath) {
        return createFile(filePath,
                WinGenericRights.GENERIC_READ,
                WinFileShareMode.FILE_SHARE_READ,
                WinFileCreationDisposition.OPEN_EXISTING,
                WinFileAttribute.FILE_ATTRIBUTE_NORMAL);
    }

    public static Handle openFileWriteOnly(String filePath) {
        return createFile(filePath,
                WinGenericRights.GENERIC_WRITE,
                WinFileShareMode.FILE_SHARE_READ,
                WinFileCreationDisposition.OPEN_EXISTING,
                WinFileAttribute.FILE_ATTRIBUTE_NORMAL);
    }

    public static boolean flushFileBuffers(Handle hFile) {
        return NativesWindows.flushFileBuffers(hFile.value());
    }

    public static int writeFile(Handle hFile,
                                byte[] buff,
                                int offset,
                                int length) {
        return NativesWindows.writeFile(hFile.value(), buff, offset, length);
    }

    public static int writeFile(Handle hFile,
                                byte[] buff) {
        return NativesWindows.writeFile(hFile.value(), buff, 0, -1);
    }

    public static int readFile(Handle hFile,
                               byte[] buff,
                               int offset) {
        return NativesWindows.readFile(hFile.value(), buff, offset);
    }

    public static int readFile(Handle hFile,
                               byte[] buff) {
        return NativesWindows.readFile(hFile.value(), buff, 0);
    }

    public static FileAttributeExInfo parseFileAttributesExInfo(String str) {
        if (str == null) {
            return null;
        }
        Map<String, String> map = getJniStringMap(str);
        FileAttributeExInfo ret = new FileAttributeExInfo();
        ret.dwFileAttributes = Converters.parseLong(map.get("dwFileAttributes"), 0);
        ret.ftCreationTime = Converters.parseLong(map.get("ftCreationTime"), 0);
        ret.ftLastAccessTime = Converters.parseLong(map.get("ftLastAccessTime"), 0);
        ret.ftLastWriteTime = Converters.parseLong(map.get("ftLastWriteTime"), 0);
        ret.nFileSize = Converters.parseLong(map.get("nFileSize"), 0);
        return ret;
    }

    public static FileAttributeExInfo getFileAttributesEx(String filePath) {
        String str = NativesWindows.getFileAttributesEx(filePath);
        return parseFileAttributesExInfo(str);
    }

    public static FileHandleInformation parseFileHandleInformationInfo(String str) {
        if (str == null) {
            return null;
        }
        Map<String, String> map = getJniStringMap(str);
        FileHandleInformation ret = new FileHandleInformation();
        ret.dwFileAttributes = Converters.parseLong(map.get("dwFileAttributes"), 0);
        ret.ftCreationTime = Converters.parseLong(map.get("ftCreationTime"), 0);
        ret.ftLastAccessTime = Converters.parseLong(map.get("ftLastAccessTime"), 0);
        ret.ftLastWriteTime = Converters.parseLong(map.get("ftLastWriteTime"), 0);
        ret.nFileSize = Converters.parseLong(map.get("nFileSize"), 0);
        ret.dwVolumeSerialNumber = Converters.parseLong(map.get("dwVolumeSerialNumber"), 0);
        ret.nNumberOfLinks = Converters.parseLong(map.get("nNumberOfLinks"), 0);
        ret.nFileIndex = Converters.parseLong(map.get("nFileIndex"), 0);
        return ret;
    }

    public static FileHandleInformation getFileInformationByHandle(Handle hFile) {
        String str = NativesWindows.getFileInformationByHandle(hFile.value());
        return parseFileHandleInformationInfo(str);
    }

    public static boolean setFileTime(Handle hFile,
                                      long creationTime,
                                      long lastAccessTime,
                                      long lastWriteTime
    ) {
        return NativesWindows.setFileTime(hFile.value(), creationTime, lastAccessTime, lastWriteTime);
    }

    public static boolean setFileCreationTime(Handle hFile,
                                              long creationTime
    ) {
        return NativesWindows.setFileTime(hFile.value(), creationTime, 0, 0);
    }

    public static boolean setFileLastAccessTime(Handle hFile,
                                                long lastAccessTime
    ) {
        return NativesWindows.setFileTime(hFile.value(), 0, lastAccessTime, 0);
    }

    public static boolean setFileLastWriteTime(Handle hFile,
                                               long lastWriteTime
    ) {
        return NativesWindows.setFileTime(hFile.value(), 0, 0, lastWriteTime);
    }

    public static boolean moveFile(String fromFilePath, String toFilePath) {
        return NativesWindows.moveFile(fromFilePath, toFilePath);
    }

    public static boolean copyFile(String fromFilePath, String toFilePath, boolean failIfExist) {
        return NativesWindows.copyFile(fromFilePath, toFilePath, failIfExist);
    }

    public static boolean coInitialize() {
        return NativesWindows.coInitialize();
    }

    public static boolean coInitializeEx(long dwCoInit) {
        return NativesWindows.coInitializeEx(dwCoInit);
    }

    public static void coUninitialize() {
        NativesWindows.coUninitialize();
    }

    public static LpItemIdList shGetSpecialFolderLocation(int csidl) {
        long ret = NativesWindows.shGetSpecialFolderLocation(csidl);
        return new LpItemIdList(ret);
    }

    public static String shGetPathFromIDList(LpItemIdList lpItemIdList) {
        return NativesWindows.shGetPathFromIDList(lpItemIdList.value());
    }

    public static void coTaskMemFree(CoTaskPtr coPtr) {
        NativesWindows.coTaskMemFree(coPtr.value());
    }


    public static String shGetSpecialFolderPath(int csidl) {
        return NativesWindows.shGetSpecialFolderPath(csidl);
    }

    public static String getSpecialFolderPath(int csidl) {
        if (!coInitialize()) {
            return null;
        }
        LpItemIdList idlist = shGetSpecialFolderLocation(csidl);
        if (idlist.isZero()) {
            coUninitialize();
            return null;
        }
        String ret = shGetPathFromIDList(idlist);
        coTaskMemFree(idlist);
        coUninitialize();
        return ret;
    }

    public static String getDesktopPath() {
        return getSpecialFolderPath(WinShGetSpecialFolderLocationCsidl.CSIDL_DESKTOP);
    }

    public static String getProgramsPath() {
        return getSpecialFolderPath(WinShGetSpecialFolderLocationCsidl.CSIDL_PROGRAMS);
    }

    public static String shGetIeQuickLunchPath() {
        String path = NativesWindows.shGetSpecialFolderPath(WinShGetSpecialFolderLocationCsidl.CSIDL_IE_QUICK_LUNCH);
        return path + WinShGetSpecialFolderLocationCsidl.IE_QUICK_LUNCH_SUB_PATH;
    }

    public static String getWindowsDirectory() {
        return NativesWindows.getWindowsDirectory();
    }

    public static String getSystemDirectory() {
        return NativesWindows.getSystemDirectory();
    }

    public static String getTempPath() {
        return NativesWindows.getTempPath();
    }

    public static CoIUnknownPtr coCreateInstance(long clsid, long clsctx, long iid) {
        long ret = NativesWindows.coCreateInstance(clsid, clsctx, iid);
        return new CoIUnknownPtr(ret);
    }

    public static long coReleaseInstance(CoIUnknownPtr ptrInstance) {
        return NativesWindows.coReleaseInstance(ptrInstance.value());
    }

    public static CoIUnknownPtr coInstanceQueryInterface(CoIUnknownPtr ptrInstance, long iid) {
        long ret = NativesWindows.coInstanceQueryInterface(ptrInstance.value(), iid);
        return new CoIUnknownPtr(ret);
    }

    public static boolean createFileShortcut(
            String srcFilePath,
            String lnkFilePath,
            String arguments,
            String workDirPath,
            String description,
            String iconPath,
            int iconIndex,
            int hotKey,
            int hotKeyVk,
            int showCmd) {
        return NativesWindows.createFileShortcut(srcFilePath, lnkFilePath, arguments, workDirPath, description, iconPath, iconIndex, hotKey, hotKeyVk, showCmd);
    }

    public static boolean createFileShortcut(
            String srcFilePath,
            String lnkFilePath,
            String arguments,
            String workDirPath,
            String description,
            int hotKey,
            int hotKeyVk,
            int showCmd) {
        return NativesWindows.createFileShortcut(srcFilePath, lnkFilePath, arguments, workDirPath, description, null, 0, hotKey, hotKeyVk, showCmd);
    }

    public static boolean createFileShortcut(
            String srcFilePath,
            String lnkFilePath,
            String arguments,
            String workDirPath,
            String description,
            int hotKey,
            int hotKeyVk) {
        return NativesWindows.createFileShortcut(srcFilePath, lnkFilePath, arguments, workDirPath, description, null, 0, hotKey, hotKeyVk, WinShowWindowCmdShow.SW_SHOWNORMAL);
    }

    public static boolean createFileShortcut(
            String srcFilePath,
            String lnkFilePath,
            String arguments,
            String workDirPath,
            String description) {
        return NativesWindows.createFileShortcut(srcFilePath, lnkFilePath, arguments, workDirPath, description, null, 0, 0, 0, WinShowWindowCmdShow.SW_SHOWNORMAL);
    }

    public static boolean createFileShortcut(
            String srcFilePath,
            String lnkFilePath,
            String arguments,
            String workDirPath) {
        return NativesWindows.createFileShortcut(srcFilePath, lnkFilePath, arguments, workDirPath, null, null, 0, 0, 0, WinShowWindowCmdShow.SW_SHOWNORMAL);
    }

    public static boolean createFileShortcut(
            String srcFilePath,
            String lnkFilePath,
            String arguments) {
        return NativesWindows.createFileShortcut(srcFilePath, lnkFilePath, arguments, null, null, null, 0, 0, 0, WinShowWindowCmdShow.SW_SHOWNORMAL);
    }

    public static boolean createFileShortcut(
            String srcFilePath,
            String lnkFilePath) {
        return NativesWindows.createFileShortcut(srcFilePath, lnkFilePath, null, null, null, null, 0, 0, 0, WinShowWindowCmdShow.SW_SHOWNORMAL);
    }

    public static boolean createFileShortcutDefault(
            String srcFilePath,
            String lnkFileDir,
            String arguments,
            String description) {
        File srcFile = new File(srcFilePath);
        String srcName = srcFile.getName();
        String srcSuffix = "";
        int idx = srcName.lastIndexOf(".");
        if (idx > 0) {
            srcSuffix = srcName.substring(idx);
            srcName = srcName.substring(0, idx);
        }
        File lnkFile = new File(lnkFileDir, srcName + ".lnk");
        return NativesWindows.createFileShortcut(srcFilePath,
                lnkFile.getAbsolutePath()
                , arguments,
                srcFile.getParentFile().getAbsolutePath(),
                description, null, 0,
                0, 0, WinShowWindowCmdShow.SW_SHOWNORMAL);
    }

    public static boolean createFileShortcutDefault(String srcFilePath,
                                                    String lnkFileDir,
                                                    String arguments) {
        return createFileShortcutDefault(srcFilePath, lnkFileDir, arguments, null);
    }

    public static boolean createFileShortcutDefault(String srcFilePath,
                                                    String lnkFileDir) {
        return createFileShortcutDefault(srcFilePath, lnkFileDir, null, null);
    }

    public static boolean createFileShortcutDesktopDefault(String srcFilePath,
                                                           String arguments,
                                                           String description) {
        String desktopPath = getDesktopPath();
        File desktopFile = new File(desktopPath);
        return createFileShortcutDefault(srcFilePath, desktopFile.getAbsolutePath(), arguments, description);
    }

    public static boolean createFileShortcutDesktopDefault(String srcFilePath,
                                                           String arguments) {
        return createFileShortcutDesktopDefault(srcFilePath, arguments, null);
    }

    public static boolean createFileShortcutDesktopDefault(String srcFilePath) {
        return createFileShortcutDesktopDefault(srcFilePath, null, null);
    }

    public static DiskFreeSpaceExInfo parseDiskFreeSpaceExInfo(String str) {
        if (str == null) {
            return null;
        }
        Map<String, String> map = getJniStringMap(str);
        DiskFreeSpaceExInfo ret = new DiskFreeSpaceExInfo();
        ret.freeBytesAvailableToCaller = Converters.parseLong(map.get("freeBytesAvailableToCaller"), 0);
        ret.totalNumberOfBytes = Converters.parseLong(map.get("totalNumberOfBytes"), 0);
        ret.totalNumberOfFreeBytes = Converters.parseLong(map.get("totalNumberOfFreeBytes"), 0);
        return ret;
    }

    public static DiskFreeSpaceExInfo getDiskFreeSpaceEx(String filePath) {
        String str = NativesWindows.getDiskFreeSpaceEx(filePath);
        return parseDiskFreeSpaceExInfo(str);
    }

    public static boolean shEmptyRecycleBin(Hwnd hwnd, String rootPath, long flags) {
        return NativesWindows.shEmptyRecycleBin(hwnd.value(), rootPath, flags);
    }

    public static boolean shEmptyRecycleBin(Hwnd hwnd, String rootPath) {
        return NativesWindows.shEmptyRecycleBin(hwnd.value(), rootPath,
                WinShEmptyRecycleBinFlag.SHERB_NOCONFIRMATION
                        | WinShEmptyRecycleBinFlag.SHERB_NOPROGRESSUI
                        | WinShEmptyRecycleBinFlag.SHERB_NOSOUND);
    }

    public static boolean shEmptyRecycleBin(char disk) {
        return shEmptyRecycleBin(Hwnd.ZERO, disk + ":\\");
    }

    public static boolean shEmptyRecycleBin() {
        return shEmptyRecycleBin(Hwnd.ZERO, null);
    }

    public static int shFileOperation(
            Hwnd hwnd,
            long wFunc,
            String pFrom,
            String pTo,
            long fFlags,
            boolean fAnyOperationsAborted,
            String lpszProgressTitle
    ) {
        return NativesWindows.shFileOperation(hwnd.value(), wFunc,
                pFrom, pTo,
                fFlags, fAnyOperationsAborted,
                lpszProgressTitle);
    }

    public static int shFileOperationDeleteToCycleBin(
            String pFrom
    ) {
        return NativesWindows.shFileOperation(0,
                WinShFileOperationFunc.FO_DELETE,
                pFrom, null,
                WinShFileOperationFlag.FOF_ALLOWUNDO | WinShFileOperationFlag.FOF_NO_UI,
                true,
                null);
    }

    public static int shFileOperationCopy(
            String pFrom,
            String pTo
    ) {
        return NativesWindows.shFileOperation(0,
                WinShFileOperationFunc.FO_COPY,
                pFrom, pTo,
                WinShFileOperationFlag.FOF_ALLOWUNDO | WinShFileOperationFlag.FOF_NO_UI,
                true,
                null);
    }

    public static int shFileOperationMove(
            String pFrom,
            String pTo
    ) {
        return NativesWindows.shFileOperation(0,
                WinShFileOperationFunc.FO_MOVE,
                pFrom, pTo,
                WinShFileOperationFlag.FOF_ALLOWUNDO | WinShFileOperationFlag.FOF_NO_UI,
                true,
                null);
    }

    public static HMonitor monitorFromWindow(Hwnd hwnd, long dwFlags) {
        long ret = NativesWindows.monitorFromWindow(hwnd.value(), dwFlags);
        return new HMonitor(ret);
    }

    public static HMonitor monitorFromWindow(Hwnd hwnd) {
        long ret = NativesWindows.monitorFromWindow(hwnd.value(), WinMonitorFromFlag.MONITOR_DEFAULTTOPRIMARY);
        return new HMonitor(ret);
    }

    public static HMonitor monitorPrimary() {
        Hwnd hwnd = getDesktopWindow();
        long ret = NativesWindows.monitorFromWindow(hwnd.value(), WinMonitorFromFlag.MONITOR_DEFAULTTOPRIMARY);
        closeHandle(hwnd);
        return new HMonitor(ret);
    }

    public static HMonitor monitorFromPoint(int x, int y, long dwFlags) {
        long ret = NativesWindows.monitorFromPoint(x, y, dwFlags);
        return new HMonitor(ret);
    }

    public static HMonitor monitorFromPoint(int x, int y) {
        long ret = NativesWindows.monitorFromPoint(x, y, WinMonitorFromFlag.MONITOR_DEFAULTTOPRIMARY);
        return new HMonitor(ret);
    }

    public static HMonitor monitorFromPoint(Point p, long dwFlags) {
        long ret = NativesWindows.monitorFromPoint((int) p.x, (int) p.y, dwFlags);
        return new HMonitor(ret);
    }

    public static HMonitor monitorFromPoint(Point p) {
        long ret = NativesWindows.monitorFromPoint((int) p.x, (int) p.y, WinMonitorFromFlag.MONITOR_DEFAULTTOPRIMARY);
        return new HMonitor(ret);
    }

    public static HMonitor monitorFromRect(int left, int top, int right, int bottom, long dwFlags) {
        long ret = NativesWindows.monitorFromRect(left, top, right, bottom, dwFlags);
        return new HMonitor(ret);
    }

    public static HMonitor monitorFromRect(int left, int top, int right, int bottom) {
        long ret = NativesWindows.monitorFromRect(left, top, right, bottom, WinMonitorFromFlag.MONITOR_DEFAULTTOPRIMARY);
        return new HMonitor(ret);
    }

    public static HMonitor monitorFromRect(Rectangle rect, long dwFlags) {
        long ret = NativesWindows.monitorFromRect((int) rect.point.x, (int) rect.point.y, (int) (rect.point.x + rect.size.dx), (int) (rect.point.y + rect.size.dy), dwFlags);
        return new HMonitor(ret);
    }

    public static HMonitor monitorFromRect(Rectangle rect) {
        long ret = NativesWindows.monitorFromRect((int) rect.point.x, (int) rect.point.y, (int) (rect.point.x + rect.size.dx), (int) (rect.point.y + rect.size.dy), WinMonitorFromFlag.MONITOR_DEFAULTTOPRIMARY);
        return new HMonitor(ret);
    }


    public static int colorAdjustLuma(int color, int n, boolean fScale) {
        return NativesWindows.colorAdjustLuma(color, n, fScale);
    }

    public static int colorHLSToRGB(
            int wHue,
            int wLuminance,
            int wSaturation) {
        return NativesWindows.colorHLSToRGB(wHue, wLuminance, wSaturation);
    }

    public static int[] colorRGBToHLS(int color) {
        return NativesWindows.colorRGBToHLS(color);
    }

    public static double[] hls2normalize(int[] hls) {
        return new double[]{hls[0] / 240.0, hls[1] / 240.0, hls[2] / 240.0};
    }

    public static int[] hls4normalize(double[] hls) {
        return new int[]{(int) (hls[0] * 240.0), (int) (hls[1] * 240.0), (int) (hls[2] * 240.0)};
    }


    public static LogFont parseLogFont(String str) {
        if (str == null) {
            return null;
        }
        Map<String, String> map = getJniStringMap(str);
        LogFont ret = new LogFont();
        ret.lfHeight = Converters.parseInt(map.get("lfHeight"), 0);
        ret.lfWidth = Converters.parseInt(map.get("lfWidth"), 0);
        ret.lfEscapement = Converters.parseInt(map.get("lfEscapement"), 0);
        ret.lfOrientation = Converters.parseInt(map.get("lfOrientation"), 0);
        ret.lfWeight = Converters.parseInt(map.get("lfWeight"), 0);
        ret.lfItalic = Converters.parseInt(map.get("lfItalic"), 0) == 1;
        ret.lfUnderline = Converters.parseInt(map.get("lfUnderline"), 0) == 1;
        ret.lfStrikeOut = Converters.parseInt(map.get("lfStrikeOut"), 0) == 1;
        ret.lfCharSet = Converters.parseInt(map.get("lfCharSet"), 0);
        ret.lfOutPrecision = Converters.parseInt(map.get("lfOutPrecision"), 0);
        ret.lfClipPrecision = Converters.parseInt(map.get("lfClipPrecision"), 0);
        ret.lfQuality = Converters.parseInt(map.get("lfQuality"), 0);
        ret.lfPitchAndFamily = Converters.parseInt(map.get("lfPitchAndFamily"), 0);
        ret.lfFaceName = map.get("lfFaceName");
        return ret;
    }


    public static long[] getProcessAffinityMask(Handle hProcess) {
        return NativesWindows.getProcessAffinityMask(hProcess.value());
    }

    public static boolean setProcessAffinityMask(Handle hProcess, long processAffinityMask) {
        return NativesWindows.setProcessAffinityMask(hProcess.value(), processAffinityMask);
    }

    public static long[] getProcessAffinityMaskByProcessId(long processId) {
        Handle hProcess = openProcess(WinOpenProcessDesiredAccess.PROCESS_QUERY_INFORMATION | WinOpenProcessDesiredAccess.PROCESS_QUERY_LIMITED_INFORMATION,
                false, processId);
        long[] ret = getProcessAffinityMask(hProcess);
        closeHandle(hProcess);
        return ret;
    }

    public static boolean setProcessAffinityMaskByProcessId(long processId, long processAffinityMask) {
        Handle hProcess = openProcess(WinOpenProcessDesiredAccess.PROCESS_SET_INFORMATION,
                false, processId);
        boolean ret = setProcessAffinityMask(hProcess, processAffinityMask);
        closeHandle(hProcess);
        return ret;
    }


    public static int getPriorityClass(Handle hProcess) {
        return NativesWindows.getPriorityClass(hProcess.value());
    }

    public static boolean setPriorityClass(Handle hProcess, int dwPriorityClass) {
        return NativesWindows.setPriorityClass(hProcess.value(), dwPriorityClass);
    }

    public static int getPriorityClassByProcessId(long processId) {
        Handle hProcess = openProcess(WinOpenProcessDesiredAccess.PROCESS_QUERY_INFORMATION | WinOpenProcessDesiredAccess.PROCESS_QUERY_LIMITED_INFORMATION,
                false, processId);
        int ret = getPriorityClass(hProcess);
        closeHandle(hProcess);
        return ret;
    }

    public static boolean setPriorityClassByProcessId(long processId, int dwPriorityClass) {
        Handle hProcess = openProcess(WinOpenProcessDesiredAccess.PROCESS_SET_INFORMATION,
                false, processId);
        boolean ret = setPriorityClass(hProcess, dwPriorityClass);
        closeHandle(hProcess);
        return ret;
    }


    public static int getThreadPriority(Handle hThread) {
        return NativesWindows.getThreadPriority(hThread.value());
    }

    public static boolean setThreadPriority(Handle hThread, int priority) {
        return NativesWindows.setThreadPriority(hThread.value(), priority);
    }

    public static long setThreadAffinityMask(Handle hThread, long threadAffinityMask) {
        return NativesWindows.setThreadAffinityMask(hThread.value(), threadAffinityMask);
    }

    public static boolean isWow64Process(Handle hProcess) {
        return NativesWindows.isWow64Process(hProcess.value());
    }

    public static Handle getCurrentThread() {
        long ret = NativesWindows.getCurrentThread();
        return new Handle(ret);
    }

    public static long getCurrentThreadId() {
        return NativesWindows.getCurrentThreadId();
    }

    public static String getCurrentDirectory() {
        return NativesWindows.getCurrentDirectory();
    }

    public static boolean setCurrentDirectory(String path) {
        return NativesWindows.setCurrentDirectory(path);
    }

    public static boolean createHardLink(String newFileName, String existsFileName) {
        return NativesWindows.createHardLink(newFileName, existsFileName);
    }

    public static boolean createSymbolicLink(String symlinkFileName, String targetFileName, int dwFlags) {
        return NativesWindows.createSymbolicLink(symlinkFileName, targetFileName, dwFlags);
    }

    public static boolean encryptFile(String fileName) {
        return NativesWindows.encryptFile(fileName);
    }

    public static boolean decryptFile(String fileName) {
        return NativesWindows.decryptFile(fileName);
    }

    public static boolean createDirectory(String fileName) {
        return NativesWindows.createDirectory(fileName);
    }

    public static boolean deleteFile(String fileName) {
        return NativesWindows.deleteFile(fileName);
    }

    public static int fileEncryptionStatus(String fileName) {
        return NativesWindows.fileEncryptionStatus(fileName);
    }

    public static int getBinaryType(String fileName) {
        return NativesWindows.getBinaryType(fileName);
    }

    public static boolean removeDirectory(String fileName) {
        return NativesWindows.removeDirectory(fileName);
    }

    public static String getComputerNameEx(int format) {
        return NativesWindows.getComputerNameEx(format);
    }

    public static long getPhysicallyInstalledSystemMemory() {
        return NativesWindows.getPhysicallyInstalledSystemMemory();
    }

    public static MemoryStatusEx parseMemoryStatusEx(String str) {
        if (str == null) {
            return null;
        }
        Map<String, String> map = getJniStringMap(str);
        MemoryStatusEx ret = new MemoryStatusEx();
        ret.dwLength = Converters.parseLong(map.get("dwLength"), 0);
        ret.dwMemoryLoad = Converters.parseLong(map.get("dwMemoryLoad"), 0);
        ret.ullTotalPhys = Converters.parseLong(map.get("ullTotalPhys"), 0);
        ret.ullAvailPhys = Converters.parseLong(map.get("ullAvailPhys"), 0);
        ret.ullTotalPageFile = Converters.parseLong(map.get("ullTotalPageFile"), 0);
        ret.ullAvailPageFile = Converters.parseLong(map.get("ullAvailPageFile"), 0);
        ret.ullTotalVirtual = Converters.parseLong(map.get("ullTotalVirtual"), 0);
        ret.ullAvailVirtual = Converters.parseLong(map.get("ullAvailVirtual"), 0);
        ret.ullAvailExtendedVirtual = Converters.parseLong(map.get("ullAvailExtendedVirtual"), 0);
        return ret;
    }

    public static MemoryStatusEx globalMemoryStatusEx() {
        String str = NativesWindows.globalMemoryStatusEx();
        return parseMemoryStatusEx(str);
    }

    public static boolean exitWindowsEx(int flags, int reason) {
        return NativesWindows.exitWindowsEx(flags, reason);
    }

    public static boolean exitWindows(int flag, boolean force) {
        OsVersionInfo info = getVersionEx();
        //如果是高版本就先权限提升
        if (info.dwPlatformId == WinOsVersionInfoPlatformId.VER_PLATFORM_WIN32_NT) {
            Handle hProcess = getCurrentProcess();
            adjustProcessPrivileges(hProcess, WinLookupPrivilegeName.SE_SHUTDOWN_NAME, true);
            closeHandle(hProcess);
        }
        if (force) {
            flag |= WinExitWindowsFlag.EWX_FORCE | WinExitWindowsFlag.EWX_FORCEIFHUNG;
        }
        return exitWindowsEx(flag, 0);
    }

    public static boolean shutdownWindows() {
        return exitWindows(WinExitWindowsFlag.EWX_SHUTDOWN, true);
    }

    public static boolean rebootWindows() {
        return exitWindows(WinExitWindowsFlag.EWX_REBOOT, true);
    }

    public static boolean logoffWindows() {
        return exitWindows(WinExitWindowsFlag.EWX_LOGOFF, true);
    }

    public static OsVersionInfo parseOsVersionInfo(String str) {
        if (str == null) {
            return null;
        }
        Map<String, String> map = getJniStringMap(str);
        OsVersionInfo ret = new OsVersionInfo();
        ret.dwOSVersionInfoSize = Converters.parseInt(map.get("dwOSVersionInfoSize"), 0);
        ret.dwMajorVersion = Converters.parseInt(map.get("dwMajorVersion"), 0);
        ret.dwMinorVersion = Converters.parseInt(map.get("dwMinorVersion"), 0);
        ret.dwBuildNumber = Converters.parseInt(map.get("dwBuildNumber"), 0);
        ret.dwPlatformId = Converters.parseInt(map.get("dwPlatformId"), 0);
        ret.szCSDVersion = map.get("szCSDVersion");

        return ret;
    }

    public static OsVersionInfo getVersionEx() {
        String str = NativesWindows.getVersionEx();
        return parseOsVersionInfo(str);
    }


    public static void keyboardVolumeMute() {
        keyboardClickEvent(WinKeyboardEventVk.VK_VOLUME_MUTE);
    }

    public static void keyboardVolumeUp() {
        keyboardClickEvent(WinKeyboardEventVk.VK_VOLUME_UP);
    }

    public static void keyboardVolumeDown() {
        keyboardClickEvent(WinKeyboardEventVk.VK_VOLUME_DOWN);
    }

    public static void keyboardVolumeToZero() {
        for (int i = 0; i < 100; i++) {
            sleep(30);
            keyboardVolumeDown();
        }
    }

    public static void keyboardVolumeToMax() {
        for (int i = 0; i < 100; i++) {
            sleep(30);
            keyboardVolumeUp();
        }
    }

    public static void keyboardVolumeToValue(int vol) {
        keyboardVolumeToZero();
        for (int i = 0; i < vol / 2; i++)//key once add 2% volume value on my pc
        {
            sleep(30);
            keyboardVolumeUp();
        }
    }

    public static void keyboardMediaNext() {
        keyboardClickEvent(WinKeyboardEventVk.VK_MEDIA_NEXT_TRACK);
    }

    public static void keyboardMediaPrevious() {
        keyboardClickEvent(WinKeyboardEventVk.VK_MEDIA_PREV_TRACK);
    }

    public static void keyboardMediaPlayPause() {
        keyboardClickEvent(WinKeyboardEventVk.VK_MEDIA_PLAY_PAUSE);
    }

    public static void keyboardMediaStop() {
        keyboardClickEvent(WinKeyboardEventVk.VK_MEDIA_STOP);
    }

    public static boolean swapMouseButton(boolean swap) {
        return NativesWindows.swapMouseButton(swap);
    }

    public static int getAsyncKeyState(int vk) {
        return NativesWindows.getAsyncKeyState(vk);
    }

    public static boolean isKeyDown(int vk) {
        return (NativesWindows.getAsyncKeyState(vk) & 0x08000) != 0;
    }

    public static int mciSendString(String command) {
        return NativesWindows.mciSendString(command);
    }

    public static void mciOpenMedia(String fileName) {
        mciSendString("open \"" + fileName + "\" alias media"); // 通过别名，后续命令可以直接使用别名 media
    }

    public static void mciPlayMedia() {
        mciSendString("play media");
    }

    public static void mciPauseMedia() {
        mciSendString("pause media");
    }

    public static void mciStopMedia() {
        mciSendString("stop media");
    }

    public static void mciResumeMedia() {
        mciSendString("resume media");
    }

    public static void mciCloseMedia() {
        mciSendString("close media");
    }

    public static void mciOpenCdAudioDoor() {
        mciSendString("set cdaudio door open");
    }

    public static void mciCloseCdAudioDoor() {
        mciSendString("set cdaudio door closed");
    }

    public static void lockScreen() {
        winExec("rundll32 user32.dll,LockWorkStation", WinShowWindowCmdShow.SW_HIDE);
    }

    public static void newPhoneLink(String name, String account, String password) {
        winExec(String.format("start rasdial %s %s %s", name, account, password), WinShowWindowCmdShow.SW_HIDE);
    }

    public static void netWirelessDisconnect() {
        winExec("netsh wlan disconnect", WinShowWindowCmdShow.SW_HIDE);
    }

    /*
	网卡名称，可以在这里查看：
	控制面板\网络和 Internet\网络连接
	*/
    public static void netDisableNetCard(String cardName) {
        winExec(String.format("netsh interface set interface \"%s\" disabled", cardName), WinShowWindowCmdShow.SW_HIDE);
    }

    public static void netEnableNetCard(String cardName) {
        winExec(String.format("netsh interface set interface \"%s\" enabled", cardName), WinShowWindowCmdShow.SW_HIDE);
    }

    public static void netEnableWirelessCard() {
        netEnableNetCard("WLAN");
    }

    public static void netDisableWirelessCard() {
        netDisableNetCard("WLAN");
    }

    public static void screenOff() {
        sendMessage(findWindow(null, null), WinSendMessageMsg.WM_SYSCOMMAND, WinSystemMenuCommand.SC_MONITORPOWER, WinSystemMenuCommand.MONITORPOWER_OFF);
    }

    public static void screenOn() {
        sendMessage(findWindow(null, null), WinSendMessageMsg.WM_SYSCOMMAND, WinSystemMenuCommand.SC_MONITORPOWER, WinSystemMenuCommand.MONITORPOWER_ON);
    }

    public static void screenLowPower() {
        sendMessage(findWindow(null, null), WinSendMessageMsg.WM_SYSCOMMAND, WinSystemMenuCommand.SC_MONITORPOWER, WinSystemMenuCommand.MONITORPOWER_POWERSAVE);
    }

    public static boolean deleteObject(HGdiObj hGdiObj) {
        return NativesWindows.deleteObject(hGdiObj.value());
    }

    public static boolean deleteDC(Hdc hdc) {
        return NativesWindows.deleteDC(hdc.value());
    }

    public static int getStretchBltMode(Hdc hdc) {
        return NativesWindows.getStretchBltMode(hdc.value());
    }

    public static int setStretchBltMode(Hdc hdc, int mode) {
        return NativesWindows.setStretchBltMode(hdc.value(), mode);
    }

    public static int getMapMode(Hdc hdc) {
        return NativesWindows.getMapMode(hdc.value());
    }

    public static int setMapMode(Hdc hdc, int mode) {
        return NativesWindows.setMapMode(hdc.value(), mode);
    }

    public static Point getViewportOrgEx(Hdc hdc) {
        int[] ret = NativesWindows.getViewportOrgEx(hdc.value());
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }

    public static Point setViewportOrgEx(Hdc hdc, int x, int y) {
        int[] ret = NativesWindows.setViewportOrgEx(hdc.value(), x, y);
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }

    public static Point setViewportOrgEx(Hdc hdc, Point p) {
        int[] ret = NativesWindows.setViewportOrgEx(hdc.value(), (int) p.x, (int) p.y);
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }

    public static Size getViewportExtEx(Hdc hdc) {
        int[] ret = NativesWindows.getViewportExtEx(hdc.value());
        if (ret.length == 0) {
            return null;
        }
        return new Size(ret[0], ret[1]);
    }

    public static Size setViewportExtEx(Hdc hdc, int cx, int cy) {
        int[] ret = NativesWindows.setViewportExtEx(hdc.value(), cx, cy);
        if (ret.length == 0) {
            return null;
        }
        return new Size(ret[0], ret[1]);
    }

    public static Size setViewportExtEx(Hdc hdc, Size s) {
        int[] ret = NativesWindows.setViewportExtEx(hdc.value(), (int) s.dx, (int) s.dy);
        if (ret.length == 0) {
            return null;
        }
        return new Size(ret[0], ret[1]);
    }

    public static Point getWindowOrgEx(Hdc hdc) {
        int[] ret = NativesWindows.getWindowOrgEx(hdc.value());
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }

    public static Point setWindowOrgEx(Hdc hdc, int x, int y) {
        int[] ret = NativesWindows.setWindowOrgEx(hdc.value(), x, y);
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }

    public static Point setWindowOrgEx(Hdc hdc, Point p) {
        int[] ret = NativesWindows.setWindowOrgEx(hdc.value(), (int) p.x, (int) p.y);
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }

    public static Size getWindowExtEx(Hdc hdc) {
        int[] ret = NativesWindows.getWindowExtEx(hdc.value());
        if (ret.length == 0) {
            return null;
        }
        return new Size(ret[0], ret[1]);
    }

    public static Size setWindowExtEx(Hdc hdc, int cx, int cy) {
        int[] ret = NativesWindows.setWindowExtEx(hdc.value(), cx, cy);
        if (ret.length == 0) {
            return null;
        }
        return new Size(ret[0], ret[1]);
    }

    public static Size setWindowExtEx(Hdc hdc, Size s) {
        int[] ret = NativesWindows.setWindowExtEx(hdc.value(), (int) s.dx, (int) s.dy);
        if (ret.length == 0) {
            return null;
        }
        return new Size(ret[0], ret[1]);
    }

    public static HGdiObj selectObject(Hdc hdc, HGdiObj hGdiObj) {
        long ret = NativesWindows.selectObject(hdc.value(), hGdiObj.value());
        return new HGdiObj(ret);
    }

    public static boolean bitBlt(
            Hdc hdc,
            int x,
            int y,
            int cx,
            int cy,
            Hdc hdcSrc,
            int x1,
            int y1,
            long rop
    ) {
        return NativesWindows.bitBlt(hdc.value(), x, y, cx, cy, hdcSrc.value(), x1, y1, rop);
    }

    public static boolean bitBlt(
            Hdc hdc,
            Rectangle rect,
            Hdc hdcSrc,
            Point p1,
            long rop
    ) {
        return NativesWindows.bitBlt(hdc.value(),
                (int) rect.point.x, (int) rect.point.y,
                (int) rect.size.dx, (int) rect.size.dy,
                hdcSrc.value(),
                (int) p1.x, (int) p1.y,
                rop);
    }

    public static HBrush createSolidBrush(int color) {
        long ret = NativesWindows.createSolidBrush(color);
        return new HBrush(ret);
    }

    public static HPen createPen(int style, int width, int color) {
        long ret = NativesWindows.createPen(style, width, color);
        return new HPen(ret);
    }

    public static HPen createPen(int width, int color) {
        long ret = NativesWindows.createPen(WinGdiPenStyle.PS_SOLID, width, color);
        return new HPen(ret);
    }

    public static HPen createPen(int color) {
        long ret = NativesWindows.createPen(WinGdiPenStyle.PS_SOLID, 1, color);
        return new HPen(ret);
    }

    public static Hdc createCompatibleDC(Hdc hdc) {
        long ret = NativesWindows.createCompatibleDC(hdc.value());
        return new Hdc(ret);
    }

    public static HBitmap createCompatibleBitmap(Hdc hdc, int cx, int cy) {
        long ret = NativesWindows.createCompatibleBitmap(hdc.value(), cx, cy);
        return new HBitmap(ret);
    }

    public static HBitmap createDIBSection(
            Hdc hdc,
            BitmapInfoPtr pBitmapInfo,
            int usage,
            Handle hSection,
            int offset) {
        long ret = NativesWindows.createDIBSection(hdc.value(), pBitmapInfo.value(),
                usage, hSection.value(), offset);
        return new HBitmap(ret);
    }

    public static void freeMallocPtr(MallocPtr ptr) {
        NativesWindows.freeMallocPtr(ptr.value());
    }

    public static void deleteNewPtr(NewPtr ptr) {
        NativesWindows.deleteNewPtr(ptr.value());
    }

    public static void deleteNewArrayPtr(NewArrayPtr ptr) {
        NativesWindows.deleteNewArrayPtr(ptr.value());
    }

    public static BitmapInfoHeaderPtr mallocBitmapInfoHeader(
            int width,
            int height,
            int planes,
            int bitCount,
            int compression,
            int clrUsed,
            int sizeImage
    ) {
        long ret = NativesWindows.mallocBitmapInfoHeader(width, height, planes, bitCount, compression, clrUsed, sizeImage);
        return new BitmapInfoHeaderPtr(ret);
    }

    public static BitmapInfoHeaderPtr mallocBitmapInfoHeader(
            int width,
            int height
    ) {
        int nBytesPerLine = ((width * 32 + 31) & (~31)) >>> 3;
        int sizeImage = nBytesPerLine * height;
        long ret = NativesWindows.mallocBitmapInfoHeader(width, height,
                1,
                32,
                WinBitmapInfoHeaderCompression.BI_RGB,
                0, sizeImage);
        return new BitmapInfoHeaderPtr(ret);
    }

    public static HBitmap createDIBSection(
            Hdc hdc,
            int width,
            int height,
            int usage,
            Handle hSection,
            int offset) {
        BitmapInfoHeaderPtr pBitmapInfo = mallocBitmapInfoHeader(width, height);
        HBitmap ret = createDIBSection(hdc, new BitmapInfoPtr(pBitmapInfo),
                usage, hSection, offset);
        freeMallocPtr(pBitmapInfo);
        return ret;
    }

    public static HBitmap createDIBSection(
            int width,
            int height) {
        BitmapInfoHeaderPtr pBitmapInfo = mallocBitmapInfoHeader(width, height);
        HBitmap ret = createDIBSection(new Hdc(0), new BitmapInfoPtr(pBitmapInfo),
                WinCreateDIBSectionUsage.DIB_RGB_COLORS, new Handle(0), 0);
        freeMallocPtr(pBitmapInfo);
        return ret;
    }

    public static HBrush createPatternBrush(HBitmap hBitmap) {
        long ret = NativesWindows.createPatternBrush(hBitmap.value());
        return new HBrush(ret);
    }

    public static LogBrushPtr mallocLogBrush(
            int color,
            int hatch,
            int style) {
        long ret = NativesWindows.mallocLogBrush(color, hatch, style);
        return new LogBrushPtr(ret);
    }

    public static HBrush createBrushIndirect(LogBrushPtr pLogBrush) {
        long ret = NativesWindows.createBrushIndirect(pLogBrush.value());
        return new HBrush(ret);
    }

    public static HBrush createBrushIndirect(int color,
                                             int hatch,
                                             int style) {
        LogBrushPtr logBrushPtr = mallocLogBrush(color, hatch, style);
        HBrush ret = createBrushIndirect(logBrushPtr);
        freeMallocPtr(logBrushPtr);
        return ret;
    }

    public static LogPenPtr mallocLogPen(
            int style,
            long widthX,
            long widthY,
            int color) {
        long ret = NativesWindows.mallocLogPen(style, widthX, widthY, color);
        return new LogPenPtr(ret);
    }

    public static HPen createPenIndirect(LogPenPtr pLogPen) {
        long ret = NativesWindows.createPenIndirect(pLogPen.value());
        return new HPen(ret);
    }

    public static HPen createPenIndirect(int style,
                                         long widthX,
                                         long widthY,
                                         int color) {
        LogPenPtr pLogPen = mallocLogPen(style, widthX, widthY, color);
        HPen ret = createPenIndirect(pLogPen);
        freeMallocPtr(pLogPen);
        return ret;
    }

    public static LogPenPtr mallocLogFont(
            int lfHeight,
            int lfWidth,
            int lfEscapement,
            int lfOrientation,
            int lfWeight,
            boolean lfItalic,
            boolean lfUnderline,
            boolean lfStrikeOut,
            int lfCharSet,
            int lfOutPrecision,
            int lfClipPrecision,
            int lfQuality,
            int lfPitchAndFamily,
            String lfFaceName) {
        long ret = NativesWindows.mallocLogFont(lfHeight, lfWidth, lfEscapement, lfOrientation, lfWeight, lfItalic, lfUnderline, lfStrikeOut, lfCharSet, lfOutPrecision, lfClipPrecision, lfQuality, lfPitchAndFamily, lfFaceName);
        return new LogPenPtr(ret);
    }

    public static HFont createFontIndirect(LogPenPtr pLogFont) {
        long ret = NativesWindows.createFontIndirect(pLogFont.value());
        return new HFont(ret);
    }

    public static HFont createFontIndirect(int lfHeight,
                                           int lfWidth,
                                           int lfEscapement,
                                           int lfOrientation,
                                           int lfWeight,
                                           boolean lfItalic,
                                           boolean lfUnderline,
                                           boolean lfStrikeOut,
                                           int lfCharSet,
                                           int lfOutPrecision,
                                           int lfClipPrecision,
                                           int lfQuality,
                                           int lfPitchAndFamily,
                                           String lfFaceName) {
        LogPenPtr pLogFont = mallocLogFont(lfHeight, lfWidth, lfEscapement, lfOrientation, lfWeight, lfItalic, lfUnderline, lfStrikeOut, lfCharSet, lfOutPrecision, lfClipPrecision, lfQuality, lfPitchAndFamily, lfFaceName);
        HFont ret = createFontIndirect(pLogFont);
        freeMallocPtr(pLogFont);
        return ret;
    }

    public static boolean ellipse(Hdc hdc, int left, int top, int right, int bottom) {
        return NativesWindows.ellipse(hdc.value(), left, top, right, bottom);
    }

    public static boolean ellipse(Hdc hdc, Rectangle rect) {
        return NativesWindows.ellipse(hdc.value(), (int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom());
    }

    public static boolean circle(Hdc hdc, int x, int y, int radius) {
        return NativesWindows.ellipse(hdc.value(), x - radius, y - radius, x + radius, y + radius);
    }

    public static boolean circle(Hdc hdc, Point center, int radius) {
        return NativesWindows.ellipse(hdc.value(), (int) center.x - radius, (int) center.y - radius, (int) center.x + radius, (int) center.y + radius);
    }

    public static boolean rectangle(Hdc hdc, int left, int top, int right, int bottom) {
        return NativesWindows.rectangle(hdc.value(), left, top, right, bottom);
    }

    public static boolean rectangle(Hdc hdc, Rectangle rect) {
        return NativesWindows.rectangle(hdc.value(), (int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom());
    }

    public static boolean square(Hdc hdc, int left, int top, int len) {
        return NativesWindows.rectangle(hdc.value(), left, top, left + len, top + len);
    }

    public static boolean square(Hdc hdc, Point p, int len) {
        return NativesWindows.rectangle(hdc.value(), (int) p.x, (int) p.y, (int) p.x + len, (int) p.y + len);
    }

    public static int setBkMode(Hdc hdc, int mode) {
        return NativesWindows.setBkMode(hdc.value(), mode);
    }

    public static int getBkMode(Hdc hdc) {
        return NativesWindows.getBkMode(hdc.value());
    }

    public static int setBkColor(Hdc hdc, int color) {
        return NativesWindows.setBkColor(hdc.value(), color);
    }

    public static int getBkColor(Hdc hdc) {
        return NativesWindows.getBkColor(hdc.value());
    }

    public static int setTextColor(Hdc hdc, int color) {
        return NativesWindows.setTextColor(hdc.value(), color);
    }

    public static int getTextColor(Hdc hdc) {
        return NativesWindows.getTextColor(hdc.value());
    }

    public static Rectangle getClientRect(Hwnd hwnd) {
        int[] ret = NativesWindows.getClientRect(hwnd.value());
        if (ret.length == 0) {
            return null;
        }
        return new Rectangle(ret[0], ret[1], ret[2] - ret[0], ret[3] - ret[1]);
    }

    public static long defWindowProc(Hwnd hwnd, long message, long wParam, long lParam) {
        return NativesWindows.defWindowProc(hwnd.value(), message, wParam, lParam);
    }

    public static HModule getModuleHandle(String moduleName) {
        long ret = NativesWindows.getModuleHandle(moduleName);
        return new HModule(ret);
    }

    public static HIcon extractIcon(HInstance hInstance, String exeFileName, int iconIndex) {
        long ret = NativesWindows.extractIcon(hInstance.value(), exeFileName, iconIndex);
        return new HIcon(ret);
    }

    public static boolean destroyIcon(HIcon hIcon) {
        return NativesWindows.destroyIcon(hIcon.value());
    }

    public static boolean freeConsole() {
        return NativesWindows.freeConsole();
    }

    public static boolean allocConsole() {
        return NativesWindows.allocConsole();
    }

    public static boolean attachConsole(long dwProcessId) {
        return NativesWindows.attachConsole(dwProcessId);
    }

    public static boolean updateWindow(Hwnd hwnd) {
        return NativesWindows.updateWindow(hwnd.value());
    }

    public static boolean invalidateRect(
            Hwnd hwnd,
            boolean nullRect,
            int left,
            int top,
            int right,
            int bottom,
            boolean erase) {
        return NativesWindows.invalidateRect(hwnd.value(),
                nullRect,
                left, top, right, bottom,
                erase);
    }

    public static boolean invalidateRect(
            Hwnd hwnd,
            int left,
            int top,
            int right,
            int bottom,
            boolean erase) {
        return NativesWindows.invalidateRect(hwnd.value(),
                false,
                left, top, right, bottom,
                erase);
    }

    public static boolean invalidateRect(
            Hwnd hwnd,
            Rectangle rect,
            boolean erase) {
        return NativesWindows.invalidateRect(hwnd.value(),
                false,
                (int) rect.left(),
                (int) rect.top(),
                (int) rect.right(),
                (int) rect.bottom(),
                erase);
    }

    public static boolean invalidateRect(
            Hwnd hwnd,
            boolean erase) {
        return NativesWindows.invalidateRect(hwnd.value(),
                true,
                0, 0, 0, 0,
                erase);
    }

    public static boolean invalidateRgn(
            Hwnd hwnd,
            HRgn hRgn,
            boolean erase
    ) {
        return NativesWindows.invalidateRgn(hwnd.value(), hRgn.value(), erase);
    }


    public static boolean getMessage(
            MsgPtr pMsg,
            Hwnd hwnd,
            long uMsgFilterMin,
            long uMsgFilterMax
    ) {
        return NativesWindows.getMessage(pMsg.value(), hwnd.value(), uMsgFilterMin, uMsgFilterMax);
    }

    public static long dispatchMessage(MsgPtr pMsg) {
        return NativesWindows.dispatchMessage(pMsg.value());
    }

    public static boolean translateMessage(MsgPtr pMsg) {
        return NativesWindows.translateMessage(pMsg.value());
    }

    public static MsgPtr mallocMsg() {
        long ret = NativesWindows.mallocMsg();
        return new MsgPtr(ret);
    }

    public static Hwnd getConsoleWindow() {
        long ret = NativesWindows.getConsoleWindow();
        return new Hwnd(ret);
    }

    public static int getXLParam(long lParam) {
        return NativesWindows.getXLParam(lParam);
    }

    public static int getYLParam(long lParam) {
        return NativesWindows.getYLParam(lParam);
    }

    public static int makeWord(int a, int b) {
        return NativesWindows.makeWord(a, b);
    }

    public static int hiWord(long a) {
        return NativesWindows.hiWord(a);
    }

    public static int loWord(long a) {
        return NativesWindows.loWord(a);
    }

    public static int getRValue(int color) {
        return NativesWindows.getRValue(color);
    }

    public static int getGValue(int color) {
        return NativesWindows.getGValue(color);
    }

    public static int getBValue(int color) {
        return NativesWindows.getBValue(color);
    }

    public static long getTickCount64() {
        return NativesWindows.getTickCount64();
    }

    public static long queryUnbiasedInterruptTime() {
        return NativesWindows.queryUnbiasedInterruptTime();
    }

    public static long queryProcessCycleTime(Handle hProcess) {
        return NativesWindows.queryProcessCycleTime(hProcess.value());
    }

    public static long queryThreadCycleTime(Handle hThread) {
        return NativesWindows.queryThreadCycleTime(hThread.value());
    }

    public static boolean beginPath(Hdc hdc) {
        return NativesWindows.beginPath(hdc.value());
    }

    public static boolean endPath(Hdc hdc) {
        return NativesWindows.endPath(hdc.value());
    }

    public static boolean abortPath(Hdc hdc) {
        return NativesWindows.abortPath(hdc.value());
    }

    public static int addFontResource(String name) {
        return NativesWindows.addFontResource(name);
    }

    public static boolean removeFontResource(String name) {
        return NativesWindows.removeFontResource(name);
    }

    public static boolean angleArc(
            Hdc hdc,
            int x,
            int y,
            int r,
            double startAngle,
            double sweepAngle) {
        return NativesWindows.angleArc(hdc.value(), x, y, r, startAngle, sweepAngle);
    }

    public static boolean angleArc(
            Hdc hdc,
            Point p,
            int r,
            double startAngle,
            double sweepAngle) {
        return NativesWindows.angleArc(hdc.value(), (int) p.x, (int) p.y, r, startAngle, sweepAngle);
    }

    public static boolean arc(
            Hdc hdc,
            int left,
            int top,
            int right,
            int bottom,
            int arcBeginX,
            int arcBeginY,
            int arcEndX,
            int arcEndY) {
        return NativesWindows.arc(hdc.value(), left, top, right, bottom, arcBeginX, arcBeginY, arcEndX, arcEndY);
    }

    public static boolean arc(
            Hdc hdc,
            Rectangle rect,
            Point begin,
            Point end) {
        return NativesWindows.arc(hdc.value(), (int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom(),
                (int) begin.x, (int) begin.y, (int) end.x, (int) end.y);
    }

    public static int getArcDirection(Hdc hdc) {
        return NativesWindows.getArcDirection(hdc.value());
    }

    public static int setArcDirection(Hdc hdc, int direction) {
        return NativesWindows.setArcDirection(hdc.value(), direction);
    }

    public static boolean arcTo(
            Hdc hdc,
            int left,
            int top,
            int right,
            int bottom,
            int arcBeginX,
            int arcBeginY,
            int arcEndX,
            int arcEndY) {
        return NativesWindows.arcTo(hdc.value(), left, top, right, bottom, arcBeginX, arcBeginY, arcEndX, arcEndY);
    }

    public static boolean arcTo(
            Hdc hdc,
            Rectangle rect,
            Point begin,
            Point end) {
        return NativesWindows.arcTo(hdc.value(), (int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom(),
                (int) begin.x, (int) begin.y, (int) end.x, (int) end.y);
    }

    public static boolean cancelDC(Hdc hdc) {
        return NativesWindows.cancelDC(hdc.value());
    }

    public static boolean chord(
            long hdc,
            int left,
            int top,
            int right,
            int bottom,
            int arcBeginX,
            int arcBeginY,
            int arcEndX,
            int arcEndY) {
        return NativesWindows.chord(hdc, left, top, right, bottom, arcBeginX, arcBeginY, arcEndX, arcEndY);
    }

    public static boolean chord(
            Hdc hdc,
            Rectangle rect,
            Point begin,
            Point end) {
        return NativesWindows.chord(hdc.value(), (int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom(),
                (int) begin.x, (int) begin.y, (int) end.x, (int) end.y);
    }

    public static boolean closeFigure(Hdc hdc) {
        return NativesWindows.closeFigure(hdc.value());
    }


    public static HPalette createHalftonePalette(Hdc hdc) {
        long ret = NativesWindows.createHalftonePalette(hdc.value());
        return new HPalette(ret);
    }

    public static int realizePalette(Hdc hdc) {
        return NativesWindows.realizePalette(hdc.value());
    }

    public static boolean resizePalette(HPalette pPalette, int n) {
        return NativesWindows.resizePalette(pPalette.value(), n);
    }

    public static HPalette selectPalette(Hdc hdc, HPalette pPalette, boolean forceBkgd) {
        long ret = NativesWindows.selectPalette(hdc.value(), pPalette.value(), forceBkgd);
        return new HPalette(ret);
    }

    public static HBrush createHatchBrush(int hatch, int color) {
        long ret = NativesWindows.createHatchBrush(hatch, color);
        return new HBrush(ret);
    }

    public static boolean fillPath(Hdc hdc) {
        return NativesWindows.fillPath(hdc.value());
    }

    public static boolean fillRgn(Hdc hdc, HRgn hRgn, HBrush hBrush) {
        return NativesWindows.fillRgn(hdc.value(), hRgn.value(), hBrush.value());
    }

    public static boolean flattenPath(Hdc hdc) {
        return NativesWindows.flattenPath(hdc.value());
    }

    public static boolean floodFill(Hdc hdc, int x, int y, int color) {
        return NativesWindows.floodFill(hdc.value(), x, y, color);
    }

    public static boolean floodFill(Hdc hdc, Point p, int color) {
        return NativesWindows.floodFill(hdc.value(), (int) p.x, (int) p.y, color);
    }

    public static boolean extFloodFill(Hdc hdc, int x, int y, int color, int type) {
        return NativesWindows.extFloodFill(hdc.value(), x, y, color, type);
    }

    public static boolean extFloodFill(Hdc hdc, Point p, int color, int type) {
        return NativesWindows.extFloodFill(hdc.value(), (int) p.x, (int) p.y, color, type);
    }

    public static boolean frameRgn(Hdc hdc, HRgn hRgn, HBrush hBrush, int w, int h) {
        return NativesWindows.frameRgn(hdc.value(), hRgn.value(), hBrush.value(), w, h);
    }

    public static boolean transparentBlt(
            Hdc hdcDst,
            int x,
            int y,
            int width,
            int height,
            Hdc hdcSrc,
            int srcX,
            int srcY,
            int srcWidth,
            int srcHeight,
            int transparentColor) {
        return NativesWindows.transparentBlt(hdcDst.value(), x, y, width, height, hdcSrc.value(), srcX, srcY, srcWidth, srcHeight, transparentColor);
    }

    public static boolean transparentBlt(
            Hdc hdcDst,
            Rectangle rectDst,
            Hdc hdcSrc,
            Rectangle rectSrc,
            int transparentColor) {
        return NativesWindows.transparentBlt(hdcDst.value(),
                (int) rectDst.point.x, (int) rectDst.point.y, (int) rectDst.size.dx, (int) rectDst.size.dy,
                hdcSrc.value(),
                (int) rectSrc.point.x, (int) rectSrc.point.y, (int) rectSrc.size.dx, (int) rectSrc.size.dy,
                transparentColor);
    }

    public static boolean stretchBlt(
            Hdc hdcDst,
            Rectangle rectDst,
            Hdc hdcSrc,
            Rectangle rectSrc,
            long rop) {
        return NativesWindows.stretchBlt(hdcDst.value(),
                (int) rectDst.point.x, (int) rectDst.point.y, (int) rectDst.size.dx, (int) rectDst.size.dy,
                hdcSrc.value(),
                (int) rectSrc.point.x, (int) rectSrc.point.y, (int) rectSrc.size.dx, (int) rectSrc.size.dy,
                rop);
    }

    public static Point getCurrentPositionEx(Hdc hdc) {
        int[] ret = NativesWindows.getCurrentPositionEx(hdc.value());
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }

    public static int getDCBrushColor(Hdc hdc) {
        return NativesWindows.getDCBrushColor(hdc.value());
    }

    public static int setDCBrushColor(Hdc hdc, int color) {
        return NativesWindows.setDCBrushColor(hdc.value(), color);
    }

    public static int getDCPenColor(Hdc hdc) {
        return NativesWindows.getDCPenColor(hdc.value());
    }

    public static int setDCPenColor(Hdc hdc, int color) {
        return NativesWindows.setDCPenColor(hdc.value(), color);
    }

    public static int getPixel(Hdc hdc, int x, int y) {
        return NativesWindows.getPixel(hdc.value(), x, y);
    }

    public static int getPixel(Hdc hdc, Point p) {
        return NativesWindows.getPixel(hdc.value(), (int) p.x, (int) p.y);
    }

    public static int setPixel(Hdc hdc, int x, int y, int color) {
        return NativesWindows.setPixel(hdc.value(), x, y, color);
    }

    public static int setPixel(Hdc hdc, Point p, int color) {
        return NativesWindows.setPixel(hdc.value(), (int) p.x, (int) p.y, color);
    }

    public static boolean setPixelV(Hdc hdc, int x, int y, int color) {
        return NativesWindows.setPixelV(hdc.value(), x, y, color);
    }

    public static boolean setPixelV(Hdc hdc, Point p, int color) {
        return NativesWindows.setPixelV(hdc.value(), (int) p.x, (int) p.y, color);
    }

    public static int getPolyFillMode(Hdc hdc) {
        return NativesWindows.getPolyFillMode(hdc.value());
    }

    public static int setPolyFillMode(Hdc hdc, int mode) {
        return NativesWindows.setPolyFillMode(hdc.value(), mode);
    }

    public static int getROP2(Hdc hdc) {
        return NativesWindows.getROP2(hdc.value());
    }

    public static int setROP2(Hdc hdc, int rop2) {
        return NativesWindows.setROP2(hdc.value(), rop2);
    }

    public static int getTextAlign(Hdc hdc) {
        return NativesWindows.getTextAlign(hdc.value());
    }

    public static int setTextAlign(Hdc hdc, int align) {
        return NativesWindows.setTextAlign(hdc.value(), align);
    }

    public static String getTextFace(Hdc hdc) {
        return NativesWindows.getTextFace(hdc.value());
    }

    public static Point moveToEx(Hdc hdc, Point p) {
        int[] ret = NativesWindows.moveToEx(hdc.value(), (int) p.x, (int) p.y);
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }

    public static boolean lineTo(Hdc hdc, Point p) {
        return NativesWindows.lineTo(hdc.value(), (int) p.x, (int) p.y);
    }

    public static Point lpToDp(Hdc hdc, Point p) {
        int[] ret = NativesWindows.lpToDp(hdc.value(), (int) p.x, (int) p.y);
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }

    public static Point dpToLp(Hdc hdc, Point p) {
        int[] ret = NativesWindows.dpToLp(hdc.value(), (int) p.x, (int) p.y);
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }

    public static boolean maskBlt(
            Hdc hdcDst,
            int x,
            int y,
            int width,
            int height,
            Hdc hdcSrc,
            int srcX,
            int srcY,
            HBitmap hBmMask,
            int xMask,
            int yMask,
            long rop) {
        return NativesWindows.maskBlt(hdcDst.value(), x, y, width, height, hdcSrc.value(), srcX, srcY, hBmMask.value(), xMask, yMask, rop);
    }

    public static boolean maskBlt(
            Hdc hdcDst,
            Rectangle rectDst,
            Hdc hdcSrc,
            Point ptSrc,
            HBitmap hBmMask,
            int xMask,
            int yMask,
            long rop) {
        return NativesWindows.maskBlt(hdcDst.value(),
                (int) rectDst.point.x, (int) rectDst.point.y, (int) rectDst.size.dx, (int) rectDst.size.dy,
                hdcSrc.value(),
                (int) ptSrc.x, (int) ptSrc.y,
                hBmMask.value(), xMask, yMask, rop);
    }

    public static int offsetClipRgn(Hdc hdc, int x, int y) {
        return NativesWindows.offsetClipRgn(hdc.value(), x, y);
    }

    public static int offsetClipRgn(Hdc hdc, Point p) {
        return NativesWindows.offsetClipRgn(hdc.value(), (int) p.x, (int) p.y);
    }

    public static int offsetRgn(HRgn hRgn, int x, int y) {
        return NativesWindows.offsetRgn(hRgn.value(), x, y);
    }

    public static int offsetRgn(HRgn hRgn, Point p) {
        return NativesWindows.offsetRgn(hRgn.value(), (int) p.x, (int) p.y);
    }

    public static Point offsetViewportOrgEx(Hdc hdc, Point p) {
        int[] ret = NativesWindows.offsetViewportOrgEx(hdc.value(), (int) p.x, (int) p.y);
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }

    public static Point offsetWindowOrgEx(Hdc hdc, Point p) {
        int[] ret = NativesWindows.offsetWindowOrgEx(hdc.value(), (int) p.x, (int) p.y);
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }

    public static boolean paintRgn(Hdc hdc, HRgn hRgn) {
        return NativesWindows.paintRgn(hdc.value(), hRgn.value());
    }

    public static boolean patBlt(Hdc hdc, int x, int y, int width, int height, long rop) {
        return NativesWindows.patBlt(hdc.value(), x, y, width, height, rop);
    }

    public static boolean patBlt(Hdc hdc, Rectangle rect, long rop) {
        return NativesWindows.patBlt(hdc.value(), (int) rect.point.x, (int) rect.point.y, (int) rect.size.dx, (int) rect.size.dy, rop);
    }

    public static HRgn pathToRegion(Hdc hdc) {
        long ret = NativesWindows.pathToRegion(hdc.value());
        return new HRgn(ret);
    }

    public static int[] points2flat(Point[] points) {
        int[] arr = new int[points.length * 2];
        for (int i = 0; i < points.length; i++) {
            arr[i * 2] = (int) points[i].x;
            arr[i * 2 + 1] = (int) points[i].y;
        }
        return arr;
    }

    public static boolean polyBezier(Hdc hdc, Point[] points) {
        int[] arr = points2flat(points);
        return NativesWindows.polyBezier(hdc.value(), arr);
    }

    public static boolean polyBezierTo(Hdc hdc, Point[] points) {
        int[] arr = points2flat(points);
        return NativesWindows.polyBezierTo(hdc.value(), arr);
    }

    public static boolean polygon(Hdc hdc, Point[] points) {
        int[] arr = points2flat(points);
        return NativesWindows.polygon(hdc.value(), arr);
    }

    public static boolean polyline(Hdc hdc, Point[] points) {
        int[] arr = points2flat(points);
        return NativesWindows.polyline(hdc.value(), arr);
    }

    public static boolean polylineTo(Hdc hdc, Point[] points) {
        int[] arr = points2flat(points);
        return NativesWindows.polylineTo(hdc.value(), arr);
    }

    public static boolean ptInRegion(HRgn hRgn, Point p) {
        return NativesWindows.ptInRegion(hRgn.value(), (int) p.x, (int) p.y);
    }

    public static boolean ptVisible(Hdc hdc, Point p) {
        return NativesWindows.ptVisible(hdc.value(), (int) p.x, (int) p.y);
    }

    public static boolean rectInRegion(HRgn hRgn, int left, int top, int right, int bottom) {
        return NativesWindows.rectInRegion(hRgn.value(), left, top, right, bottom);
    }

    public static boolean rectInRegion(HRgn hRgn, Rectangle rect) {
        return NativesWindows.rectInRegion(hRgn.value(), (int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom());
    }

    public static boolean rectVisible(Hdc hdc, int left, int top, int right, int bottom) {
        return NativesWindows.rectVisible(hdc.value(), left, top, right, bottom);
    }

    public static boolean rectVisible(Hdc hdc, Rectangle rect) {
        return NativesWindows.rectVisible(hdc.value(), (int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom());
    }

    public static int saveDC(Hdc hdc) {
        return NativesWindows.saveDC(hdc.value());
    }

    public static boolean restoreDC(Hdc hdc, int nSavedDC) {
        return NativesWindows.restoreDC(hdc.value(), nSavedDC);
    }

    public static boolean roundRect(Hdc hdc, int left, int top, int right, int bottom, int width, int height) {
        return NativesWindows.roundRect(hdc.value(), left, top, right, bottom, width, height);
    }

    public static boolean roundRect(Hdc hdc, Rectangle rect, int width, int height) {
        return NativesWindows.roundRect(hdc.value(), (int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom(), width, height);
    }

    public static int selectClipRgn(Hdc hdc, HRgn hRgn) {
        return NativesWindows.selectClipRgn(hdc.value(), hRgn.value());
    }

    public static boolean selectClipPath(Hdc hdc, int mode) {
        return NativesWindows.selectClipPath(hdc.value(), mode);
    }

    public static Point getBrushOrgEx(Hdc hdc) {
        int[] ret = NativesWindows.getBrushOrgEx(hdc.value());
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }

    public static Point setBrushOrgEx(Hdc hdc, Point p) {
        int[] ret = NativesWindows.setBrushOrgEx(hdc.value(), (int) p.x, (int) p.y);
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }

    public static boolean setRectRgn(HRgn hRgn, int left, int top, int right, int bottom) {
        return NativesWindows.setRectRgn(hRgn.value(), left, top, right, bottom);
    }

    public static boolean setRectRgn(HRgn hRgn, Rectangle rect) {
        return NativesWindows.setRectRgn(hRgn.value(), (int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom());
    }

    public static boolean setTextJustification(Hdc hdc, int extra, int count) {
        return NativesWindows.setTextJustification(hdc.value(), extra, count);
    }

    public static boolean strokeAndFillPath(Hdc hdc) {
        return NativesWindows.strokeAndFillPath(hdc.value());
    }

    public static boolean strokePath(Hdc hdc) {
        return NativesWindows.strokePath(hdc.value());
    }

    public static boolean textOut(Hdc hdc, Point p, String text) {
        return NativesWindows.textOut(hdc.value(), (int) p.x, (int) p.y, text);
    }

    public static boolean unrealizeObject(HGdiObj hGdiObj) {
        return NativesWindows.unrealizeObject(hGdiObj.value());
    }

    public static boolean updateColors(Hdc hdc) {
        return NativesWindows.updateColors(hdc.value());
    }

    public static HCursor loadCursorByStandardId(HInstance hInstance, int standardCursorId) {
        long ret = NativesWindows.loadCursorByStandardId(hInstance.value(), standardCursorId);
        return new HCursor(ret);
    }

    public static HCursor loadCursorArrow(HInstance hInstance) {
        long ret = NativesWindows.loadCursorByStandardId(hInstance.value(), WinStandardCursorId.IDC_ARROW);
        return new HCursor(ret);
    }

    public static HCursor loadCursor(HInstance hInstance, String cursorName) {
        long ret = NativesWindows.loadCursor(hInstance.value(), cursorName);
        return new HCursor(ret);
    }

    public static HBrush convertBrushBySystemColorIndex(int index) {
        long ret = NativesWindows.convertBrushBySystemColorIndex(index);
        return new HBrush(ret);
    }

    public static HBrush convertBrushBySystemWindow() {
        long ret = NativesWindows.convertBrushBySystemColorIndex(WinSystemColorIndex.COLOR_WINDOW);
        return new HBrush(ret);
    }

    public static HBrush convertBrushBySystemWindowFrame() {
        long ret = NativesWindows.convertBrushBySystemColorIndex(WinSystemColorIndex.COLOR_WINDOWFRAME);
        return new HBrush(ret);
    }

    public static int registerClassEx(
            int style,
            int cbClsExtra,
            int cbWndExtra,
            HInstance hInstance,
            HIcon hIcon,
            HCursor hCursor,
            HBrush hbrBackground,
            String lpszMenuName,
            String lpszClassName,
            HIcon hIconSm) {
        return NativesWindows.registerClassEx(style, cbClsExtra, cbWndExtra,
                hInstance.value(), hIcon.value(), hCursor.value(), hbrBackground.value(),
                lpszMenuName, lpszClassName,
                hIconSm.value());
    }

    public static int registerClassEx(
            HInstance hInstance,
            HIcon hIcon,
            String lpszClassName) {
        return registerClassEx(WinClassStyle.CS_HREDRAW | WinClassStyle.CS_VREDRAW,
                0, 0,
                hInstance,
                hIcon,
                loadCursorArrow(hInstance),
                convertBrushBySystemWindow(),
                null,
                lpszClassName,
                hIcon);
    }


    public static Hwnd createWindowEx(
            int dwExStyle,
            String lpClassName,
            String lpWindowName,
            int dwStyle,
            int x,
            int y,
            int nWidth,
            int nHeight,
            Hwnd hwndParent,
            HMenu hMenu,
            HInstance hInstance) {
        long ret = NativesWindows.createWindowEx(dwExStyle,
                lpClassName, lpWindowName,
                dwStyle,
                x, y,
                nWidth, nHeight,
                hwndParent.value(),
                hMenu.value(),
                hInstance.value());
        return new Hwnd(ret);
    }

    public static Hwnd createWindowEx(
            String lpClassName,
            String lpWindowName,
            HInstance hInstance) {
        return createWindowEx(0,
                lpClassName,
                lpWindowName,
                WinWindowStyle.WS_OVERLAPPEDWINDOW,
                WinConsts.CW_USEDEFAULT, 0,
                WinConsts.CW_USEDEFAULT, 0,
                new Hwnd(0),
                new HMenu(0),
                hInstance);
    }

    public static PaintStructPtr mallocPaintStruct() {
        long ret = NativesWindows.mallocPaintStruct();
        return new PaintStructPtr(ret);
    }

    public static Hdc beginPaint(Hwnd hwnd, PaintStructPtr pPaintStruct) {
        long ret = NativesWindows.beginPaint(hwnd.value(), pPaintStruct.value());
        return new Hdc(ret);
    }

    public static boolean endPaint(Hwnd hwnd, PaintStructPtr pPaintStruct) {
        return NativesWindows.endPaint(hwnd.value(), pPaintStruct.value());
    }

    public static void postQuitMessage(int exitCode) {
        NativesWindows.postQuitMessage(exitCode);
    }

    public static void bindMessageCallbacker(Hwnd hwnd, WinMessageCallbacker callbacker) {
        NativesWindows.bindMessageCallbacker(hwnd.value(), callbacker);
    }

    public static BitmapDcPtr mallocBitMapDc() {
        long ret = NativesWindows.mallocBitMapDc();
        return new BitmapDcPtr(ret);
    }

    public static BitmapDcInfo parseBitmapDcInfo(String str) {
        if (str == null) {
            return null;
        }
        Map<String, String> map = getJniStringMap(str);
        BitmapDcInfo ret = new BitmapDcInfo();
        ret.hdc = new Hdc(Converters.parseLong(map.get("hdc"), 0));
        ret.hBitmap = new HBitmap(Converters.parseInt(map.get("hBitmap"), 0));
        ret.width = Converters.parseInt(map.get("width"), 0);
        ret.height = Converters.parseInt(map.get("height"), 0);
        return ret;
    }

    public static BitmapDcInfo getBitmapDcInfo(BitmapDcPtr pBitmapDc) {
        String str = NativesWindows.getBitmapDcInfo(pBitmapDc.value());
        return parseBitmapDcInfo(str);
    }

    public static void freopenStdio() {
        NativesWindows.freopenStdio();
    }

    public static Handle createThread(
            int dwStackSize,
            int dwCreationFlags,
            Runnable runnable
    ) {
        long ret = NativesWindows.createThread(dwStackSize, dwCreationFlags, runnable);
        return new Handle(ret);
    }

    public static Handle createThread(
            Runnable runnable
    ) {
        return createThread(0, 0, runnable);
    }

    public static long waitForSingleObject(
            Handle hHandle,
            long dwMillSeconds
    ) {
        return NativesWindows.waitForSingleObject(hHandle.value(), dwMillSeconds);
    }

    public static long waitForSingleObject(
            Handle hHandle
    ) {
        return waitForSingleObject(hHandle, -1);
    }

    public static long waitForMultipleObjects(
            Handle[] hHandles,
            boolean waitAll,
            long dwMillSeconds) {
        long[] args = new long[hHandles.length];
        for (int i = 0; i < hHandles.length; i++) {
            args[i] = hHandles[i].value();
        }
        return NativesWindows.waitForMultipleObjects(args, waitAll, dwMillSeconds);
    }

    public static long waitForMultipleObjects(
            Handle[] hHandles) {
        return waitForMultipleObjects(hHandles, true, -1);
    }

    public static HBitmap winAppCreateBitmap(int width, int height) {
        long ret = NativesWindows.winAppCreateBitmap(width, height);
        return new HBitmap(ret);
    }

    public static void winAppResizeCompatibleDC(
            Hdc hdc,
            BitmapDcPtr pBDC,
            int newWidth,
            int newHeight,
            int resizeMode) {
        NativesWindows.winAppResizeCompatibleDC(hdc.value(),
                pBDC.value(),
                newWidth, newHeight,
                resizeMode);
    }

    public static int winAppCreateWin32App(
            String className,
            String windowTitle,
            String iconFileName,
            int nCmdShow,
            boolean showConsole,
            int mdcResizeMode,
            WinAppCallbacker callbacker) {
        return NativesWindows.winAppCreateWin32App(className, windowTitle, iconFileName, nCmdShow, showConsole, mdcResizeMode, callbacker);
    }
}
