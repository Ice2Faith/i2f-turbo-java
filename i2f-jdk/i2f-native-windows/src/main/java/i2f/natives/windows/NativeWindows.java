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

    public static native boolean setWindowText(long hwnd, String title);

    public static native long windowFromPoint(int x, int y);

    public static native int[] getCursorPos();

    public static native int messageBox(long hwnd, String content, String title, int type);

    public static native boolean messageBeep(int type);

    public static native int[] getWindowRect(long hwnd);

    public static native boolean setWindowPos(long hwnd, long hwndInsertAfter, int x, int y, int cx, int cy, int flag);

    public static native boolean showWindow(long hwnd, int cmdShow);

    public static native long setParent(long hwndChild, long hwndNewParent);

    public static native long findWindow(String className, String windowName);

    public static native long getWindow(long hwnd, int cmd);

    public static native long sendMessage(long hwnd, int msg, long wParamPtr, long lParamPtr);

    public static native boolean postMessage(long hwnd, int msg, long wParamPtr, long lParamPtr);

    public static native long getWindowThreadProcessId(long hwnd);

    public static native long openProcess(long dwDesiredAccess, boolean bInheritHandle, long dwProcessId);

    public static native boolean isInvalidHandle(long handle);

    public static native boolean terminateProcess(long hProcess, int uExitCode);

    public static native boolean closeHandle(long hObject);

    public static native int winExec(String cmdLine, int uCmdShow);

    public static native long getNextWindow(long hwnd, int cmd);

    public static native String getClassName(long hwnd);

    public static native long getCurrentProcess();

    public static native long getCurrentProcessId();

    public static native long openProcessToken(long hProcess, long dwDesiredAccess);

    public static native long[] lookupPrivilegeValue(String pSystemName, String pName);

    public static native boolean adjustTokenPrivileges(long tokenHandle, boolean disableAllPrivileges,
                                                       int attributes,
                                                       long luidLowPart,
                                                       long luidHighPart);

    public static native boolean adjustProcessPrivileges(long processHandle,
                                                         String seName,
                                                         boolean enable);

    public static native long getWindowLong(long hwnd, int index);

    public static native long getClassLong(long hwnd, int index);

    public static native long setWindowLong(long hwnd, int index, long dwNewLong);

    public static native long setClassLong(long hwnd, int index, long dwNewLong);

    public static native long[] getLayeredWindowAttributes(long hwnd);

    public static native boolean setLayeredWindowAttributes(long hwnd, int color, byte alpha, long flags);

    public static native long[] enumWindows();

    public static native long[] enumChildWindows(long hwndParent);

    public static native long[] enumThreadWindows(long threadId);

    public static native long[] enumDesktopWindows(long hDesktop);

    public static native boolean setForegroundWindow(long hwnd);

    public static native long getActiveWindow();

    public static native long setActiveWindow(long hwnd);

    public static native boolean enableWindow(long hwnd, boolean enable);

    public static native boolean isWindowEnabled(long hwnd);

    public static native long getWindowDC(long hwnd);

    public static native boolean isWindowVisible(long hwnd);

    public static native boolean isWindowUnicode(long hwnd);

    public static native boolean isChild(long hwndParent, long hwnd);

    public static native boolean isIconic(long hwnd);

    public static native boolean openIcon(long hwnd);

    public static native boolean isZoomed(long hwnd);

    public static native long getParent(long hwnd);

    public static native long findWindowEx(long hwndParent, long hwndChildAfter, String className, String windowText);

    public static native long windowFromDC(long hdc);

    public static native boolean moveWindow(long hwnd, int x, int y, int width, int height, boolean repaint);

    public static native long createToolhelp32Snapshot(long dwFlags, long processId);

    public static native String process32First(long hSnapshot);

    public static native String process32Next(long hSnapshot);

    public static native String module32First(long hSnapshot);

    public static native String module32Next(long hSnapshot);

    public static native String thread32First(long hSnapshot);

    public static native String thread32Next(long hSnapshot);

    public static native long getExitCodeProcess(long hProcess);

    public static native long getExitCodeThread(long hThread);

    public static native long getLastError();

    public static native void setLastError(long errCode);

    public static native void setLastErrorEx(long errCode, long type);

    public static native long openThread(long dwDesiredAccess, boolean inheritHandle, long dwThreadId);

    public static native long suspendThread(long hThread);

    public static native long resumeThread(long hThread);

    public static native String getLogicalDriveStrings();

    public static native long getDriveType(String rootPathName);

    public static native long getFileAttributes(String fileName);

    public static native boolean setFileAttributes(String fileName, long attribute);

    public static native int deleteFileToRecycleBin(String fileName);
}
