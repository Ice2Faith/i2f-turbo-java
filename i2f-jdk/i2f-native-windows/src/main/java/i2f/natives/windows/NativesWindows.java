package i2f.natives.windows;

import i2f.natives.core.NativeUtil;
import i2f.natives.windows.consts.winapp.WinAppCallbacker;
import i2f.natives.windows.consts.window.WinMessageCallbacker;

/**
 * @author Ice2Faith
 * @date 2024/5/7 11:41
 * @desc
 */
public class NativesWindows {

    static {
        NativeUtil.loadClasspathLib("lib/NativesWindows");
    }

    public static native String hello();

    public static native boolean kbHit();

    public static native int getCh();

    public static native void flushStdin();

    public static native void flushStdout();

    public static native int rgb(int r, int g, int b);

    public static native void sleep(int millSeconds);

    public static native void system(String cmd);

    public static native long envStringToWcharPtr(String str);

    public static native void envFreeWcharPtr(long ptr);

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

    public static native String getWindowInfo(long hwnd);

    public static native int[] clientToScreen(long hwnd, int x, int y);

    public static native int[] screenToClient(long hwnd, int x, int y);

    public static native void keyboardEvent(int bVk, int bScan, long dwFlags);

    public static native void mouseEvent(long dwFlags, int dx, int dy, int dwData);

    public static native boolean setCursorPos(int x, int y);

    public static native long regOpenKeyEx(long hkey, String subKey, long ulOptions, long samDesired);

    public static native String regEnumValue(long hkey, int index);

    public static native boolean regCloseKey(long hkey);

    public static native String regEnumKeyEx(long hkey, int index);

    public static native long regCreateKeyEx(long hkey, String subKey, String className, long dwOptions, long samDesired);

    public static native boolean regDeleteKey(long hkey, String subKey);

    public static native String regQueryValueEx(long hkey, String valueName);

    public static native boolean regSetValueEx(long hkey, String valueName, long type, String data);

    public static native boolean regDeleteValue(long hkey, String valueName);

    public static native long openSCManager(String machineName, String databaseName, long dwDesiredAccess);

    public static native String enumServicesStatus(long hScm, long serviceType, long serviceState);

    public static native boolean closeServiceHandle(long hScm);

    public static native long openService(long hScm, String serviceName, long dwDesiredAccess);

    public static native boolean startService(long hService, int dwNumServiceArgs, String serviceArgVectors);

    public static native boolean controlService(long hService, int dwControl);

    public static native boolean deleteService(long hService);

    public static native String queryServiceStatus(long hService);

    public static native long createService(long hScm,
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
                                            String password);

    public static native long createFile(String filePath,
                                         long dwDesiredAccess,
                                         long dwShareMode,
                                         long dwCreationDisposition,
                                         long dwFlagAndAttributes);

    public static native boolean flushFileBuffers(long hFile);

    public static native int writeFile(long hFile,
                                       byte[] buff,
                                       int offset,
                                       int length);

    public static native int readFile(long hFile,
                                      byte[] buff,
                                      int offset);

    public static native String getFileAttributesEx(String filePath);

    public static native String getFileInformationByHandle(long hFile);

    public static native boolean setFileTime(long hFile,
                                             long creationTime,
                                             long lastAccessTime,
                                             long lastWriteTime
    );

    public static native boolean moveFile(String fromFilePath, String toFilePath);

    public static native boolean copyFile(String fromFilePath, String toFilePath, boolean failIfExist);

    public static native boolean coInitialize();

    public static native boolean coInitializeEx(long dwCoInit);

    public static native void coUninitialize();

    public static native long shGetSpecialFolderLocation(int csidl);

    public static native String shGetPathFromIDList(long lpItemIdList);

    public static native void coTaskMemFree(long coPtr);

    public static native String shGetSpecialFolderPath(int csidl);

    public static native String getWindowsDirectory();

    public static native String getSystemDirectory();

    public static native String getTempPath();

    public static native long coCreateInstance(long clsid, long clsctx, long iid);

    public static native long coReleaseInstance(long ptrInstance);

    public static native long coInstanceQueryInterface(long ptrInstance, long iid);

    public static native boolean createFileShortcut(
            String srcFilePath,
            String lnkFilePath,
            String arguments,
            String workDirPath,
            String description,
            String iconPath,
            int iconIndex,
            int hotKey,
            int hotKeyVk,
            int showCmd);

    public static native String getDiskFreeSpaceEx(String filePath);

    public static native boolean shEmptyRecycleBin(long hwnd, String rootPath, long flags);

    public static native int shFileOperation(
            long hwnd,
            long wFunc,
            String pFrom,
            String pTo,
            long fFlags,
            boolean fAnyOperationsAborted,
            String lpszProgressTitle
    );

    public static native long monitorFromWindow(long hwnd, long dwFlags);

    public static native long monitorFromPoint(int x, int y, long dwFlags);

    public static native long monitorFromRect(int left, int top, int right, int bottom, long dwFlags);

    public static native int colorAdjustLuma(int color, int n, boolean fScale);

    public static native int colorHLSToRGB(
            int wHue,
            int wLuminance,
            int wSaturation);

    public static native int[] colorRGBToHLS(int color);

    public static native long[] getProcessAffinityMask(long hProcess);

    public static native boolean setProcessAffinityMask(long hProcess, long processAffinityMask);

    public static native int getPriorityClass(long hProcess);

    public static native boolean setPriorityClass(long hProcess, int dwPriorityClass);

    public static native int getThreadPriority(long hThread);

    public static native boolean setThreadPriority(long hThread, int priority);

    public static native long setThreadAffinityMask(long hThread, long threadAffinityMask);

    public static native boolean isWow64Process(long hProcess);

    public static native long getCurrentThread();

    public static native long getCurrentThreadId();

    public static native String getCurrentDirectory();

    public static native boolean setCurrentDirectory(String path);

    public static native boolean createHardLink(String newFileName, String existsFileName);

    public static native boolean createSymbolicLink(String symlinkFileName, String targetFileName, int dwFlags);

    public static native boolean encryptFile(String fileName);

    public static native boolean decryptFile(String fileName);

    public static native boolean createDirectory(String fileName);

    public static native boolean deleteFile(String fileName);

    public static native int fileEncryptionStatus(String fileName);

    public static native int getBinaryType(String fileName);

    public static native boolean removeDirectory(String fileName);

    public static native String getComputerNameEx(int format);

    public static native long getPhysicallyInstalledSystemMemory();

    public static native String globalMemoryStatusEx();

    public static native boolean exitWindowsEx(int flags, int reason);

    public static native String getVersionEx();

    public static native boolean swapMouseButton(boolean swap);

    public static native int getAsyncKeyState(int vk);

    public static native int mciSendString(String command);

    public static native boolean deleteObject(long hGdiObj);

    public static native boolean deleteDC(long hdc);

    public static native int getStretchBltMode(long hdc);

    public static native int setStretchBltMode(long hdc, int mode);

    public static native int getMapMode(long hdc);

    public static native int setMapMode(long hdc, int mode);

    public static native int[] getViewportOrgEx(long hdc);

    public static native int[] setViewportOrgEx(long hdc, int x, int y);

    public static native int[] getViewportExtEx(long hdc);

    public static native int[] setViewportExtEx(long hdc, int cx, int cy);

    public static native int[] getWindowOrgEx(long hdc);

    public static native int[] setWindowOrgEx(long hdc, int x, int y);

    public static native int[] getWindowExtEx(long hdc);

    public static native int[] setWindowExtEx(long hdc, int cx, int cy);

    public static native long selectObject(long hdc, long hGdiObj);

    public static native boolean bitBlt(
            long hdc,
            int x,
            int y,
            int cx,
            int cy,
            long hdcSrc,
            int x1,
            int y1,
            long rop
    );

    public static native long createSolidBrush(int color);

    public static native long createPen(int style, int width, int color);

    public static native long createCompatibleDC(long hdc);

    public static native long createCompatibleBitmap(long hdc, int cx, int cy);

    public static native long createDIBSection(
            long hdc,
            long pBitmapInfo,
            int usage,
            long hSection,
            int offset);

    public static native void freeMallocPtr(long ptr);

    public static native void deleteNewPtr(long ptr);

    public static native void deleteNewArrayPtr(long ptr);

    public static native long mallocBitmapInfoHeader(
            int width,
            int height,
            int planes,
            int bitCount,
            int compression,
            int clrUsed,
            int sizeImage
    );

    public static native long createPatternBrush(long hBitmap);

    public static native long mallocLogBrush(
            int color,
            int hatch,
            int style);

    public static native long createBrushIndirect(long pLogBrush);

    public static native long mallocLogPen(
            int style,
            long widthX,
            long widthY,
            int color);

    public static native long createPenIndirect(long pLogPen);

    public static native long mallocLogFont(
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
            String lfFaceName);

    public static native long createFontIndirect(long pLogFont);

    public static native boolean ellipse(long hdc, int left, int top, int right, int bottom);

    public static native boolean rectangle(long hdc, int left, int top, int right, int bottom);

    public static native int setBkMode(long hdc, int mode);

    public static native int getBkMode(long hdc);

    public static native int setBkColor(long hdc, int color);

    public static native int getBkColor(long hdc);

    public static native int setTextColor(long hdc, int color);

    public static native int getTextColor(long hdc);

    public static native int[] getClientRect(long hwnd);

    public static native long defWindowProc(long hwnd, long message, long wParam, long lParam);

    public static native long getModuleHandle(String moduleName);

    public static native long extractIcon(long hInstance, String exeFileName, int iconIndex);

    public static native boolean destroyIcon(long hIcon);

    public static native boolean freeConsole();

    public static native boolean allocConsole();

    public static native boolean attachConsole(long dwProcessId);

    public static native boolean updateWindow(long hwnd);

    public static native boolean invalidateRect(
            long hwnd,
            boolean nullRect,
            int left,
            int top,
            int right,
            int bottom,
            boolean erase);

    public static native boolean invalidateRgn(
            long hwnd,
            long hRgn,
            boolean erase
    );


    public static native boolean getMessage(
            long pMsg,
            long hwnd,
            long uMsgFilterMin,
            long uMsgFilterMax
    );

    public static native long dispatchMessage(long pMsg);

    public static native boolean translateMessage(long pMsg);

    public static native long mallocMsg();

    public static native long getConsoleWindow();

    public static native int getXLParam(long lParam);

    public static native int getYLParam(long lParam);

    public static native int makeWord(int a, int b);

    public static native int hiWord(long a);

    public static native int loWord(long a);

    public static native int getRValue(int color);

    public static native int getGValue(int color);

    public static native int getBValue(int color);

    public static native long getTickCount64();

    public static native long queryUnbiasedInterruptTime();

    public static native long queryProcessCycleTime(long hProcess);

    public static native long queryThreadCycleTime(long hThread);

    public static native boolean beginPath(long hdc);

    public static native boolean endPath(long hdc);

    public static native boolean abortPath(long hdc);

    public static native int addFontResource(String name);

    public static native boolean removeFontResource(String name);

    public static native boolean angleArc(
            long hdc,
            int x,
            int y,
            int r,
            double startAngle,
            double sweepAngle);

    public static native boolean arc(
            long hdc,
            int left,
            int top,
            int right,
            int bottom,
            int arcBeginX,
            int arcBeginY,
            int arcEndX,
            int arcEndY);

    public static native int getArcDirection(long hdc);

    public static native int setArcDirection(long hdc, int direction);

    public static native boolean arcTo(
            long hdc,
            int left,
            int top,
            int right,
            int bottom,
            int arcBeginX,
            int arcBeginY,
            int arcEndX,
            int arcEndY);

    public static native boolean cancelDC(long hdc);

    public static native boolean chord(
            long hdc,
            int left,
            int top,
            int right,
            int bottom,
            int arcBeginX,
            int arcBeginY,
            int arcEndX,
            int arcEndY);

    public static native boolean closeFigure(long hdc);


    public static native long createHalftonePalette(long hdc);

    public static native int realizePalette(long hdc);

    public static native boolean resizePalette(long pPalette, int n);

    public static native long selectPalette(long hdc, long pPalette, boolean forceBkgd);

    public static native long createHatchBrush(int hatch, int color);

    public static native boolean fillPath(long hdc);

    public static native boolean fillRgn(long hdc, long hRgn, long hBrush);

    public static native boolean flattenPath(long hdc);

    public static native boolean floodFill(long hdc, int x, int y, int color);

    public static native boolean extFloodFill(long hdc, int x, int y, int color, int type);

    public static native boolean frameRgn(long hdc, long hRgn, long hBrush, int w, int h);

    public static native boolean transparentBlt(
            long hdcDst,
            int x,
            int y,
            int width,
            int height,
            long hdcSrc,
            int srcX,
            int srcY,
            int srcWidth,
            int srcHeight,
            int transparentColor);

    public static native boolean stretchBlt(
            long hdcDst,
            int x,
            int y,
            int width,
            int height,
            long hdcSrc,
            int srcX,
            int srcY,
            int srcWidth,
            int srcHeight,
            long rop);

    public static native int[] getCurrentPositionEx(long hdc);

    public static native int getDCBrushColor(long hdc);

    public static native int setDCBrushColor(long hdc, int color);

    public static native int getDCPenColor(long hdc);

    public static native int setDCPenColor(long hdc, int color);

    public static native int getPixel(long hdc, int x, int y);

    public static native int setPixel(long hdc, int x, int y, int color);

    public static native boolean setPixelV(long hdc, int x, int y, int color);

    public static native int getPolyFillMode(long hdc);

    public static native int setPolyFillMode(long hdc, int mode);

    public static native int getROP2(long hdc);

    public static native int setROP2(long hdc, int rop2);

    public static native int getTextAlign(long hdc);

    public static native int setTextAlign(long hdc, int align);

    public static native String getTextFace(long hdc);

    public static native int[] moveToEx(long hdc, int x, int y);

    public static native boolean lineTo(long hdc, int x, int y);

    public static native int[] lpToDp(long hdc, int x, int y);

    public static native int[] dpToLp(long hdc, int x, int y);

    public static native boolean maskBlt(
            long hdcDst,
            int x,
            int y,
            int width,
            int height,
            long hdcSrc,
            int srcX,
            int srcY,
            long hBmMask,
            int xMask,
            int yMask,
            long rop);

    public static native int offsetClipRgn(long hdc, int x, int y);

    public static native int offsetRgn(long hRgn, int x, int y);

    public static native int[] offsetViewportOrgEx(long hdc, int x, int y);

    public static native int[] offsetWindowOrgEx(long hdc, int x, int y);

    public static native boolean paintRgn(long hdc, long hRgn);

    public static native boolean patBlt(long hdc, int x, int y, int width, int height, long rop);

    public static native long pathToRegion(long hdc);

    public static native boolean polyBezier(long hdc, int[] points);

    public static native boolean polyBezierTo(long hdc, int[] points);

    public static native boolean polygon(long hdc, int[] points);

    public static native boolean polyline(long hdc, int[] points);

    public static native boolean polylineTo(long hdc, int[] points);

    public static native boolean ptInRegion(long hRgn, int x, int y);

    public static native boolean ptVisible(long hdc, int x, int y);

    public static native boolean rectInRegion(long hRgn, int left, int top, int right, int bottom);

    public static native boolean rectVisible(long hdc, int left, int top, int right, int bottom);

    public static native int saveDC(long hdc);

    public static native boolean restoreDC(long hdc, int nSavedDC);

    public static native boolean roundRect(long hdc, int left, int top, int right, int bottom, int width, int height);

    public static native int selectClipRgn(long hdc, long hRgn);

    public static native boolean selectClipPath(long hdc, int mode);

    public static native int[] getBrushOrgEx(long hdc);

    public static native int[] setBrushOrgEx(long hdc, int x, int y);

    public static native boolean setRectRgn(long hRgn, int left, int top, int right, int bottom);

    public static native boolean setTextJustification(long hdc, int extra, int count);

    public static native boolean strokeAndFillPath(long hdc);

    public static native boolean strokePath(long hdc);

    public static native boolean textOut(long hdc, int x, int y, String text);

    public static native boolean unrealizeObject(long hGdiObj);

    public static native boolean updateColors(long hdc);

    public static native long loadCursorByStandardId(long hInstance, int standardCursorId);

    public static native long loadCursor(long hInstance, String cursorName);

    public static native long convertBrushBySystemColorIndex(int index);

    public static native int registerClassEx(
            int style,
            int cbClsExtra,
            int cbWndExtra,
            long hInstance,
            long hIcon,
            long hCursor,
            long hbrBackground,
            String lpszMenuName,
            String lpszClassName,
            long hIconSm);

    public static native long createWindowEx(
            int dwExStyle,
            String lpClassName,
            String lpWindowName,
            int dwStyle,
            int x,
            int y,
            int nWidth,
            int nHeight,
            long hwndParent,
            long hMenu,
            long hInstance);

    public static native long mallocPaintStruct();

    public static native long beginPaint(long hwnd, long pPaintStruct);

    public static native boolean endPaint(long hwnd, long pPaintStruct);

    public static native void postQuitMessage(int exitCode);

    public static native void bindMessageCallbacker(long hwnd, WinMessageCallbacker callbacker);

    public static native long mallocBitMapDc();

    public static native String getBitmapDcInfo(long pBitmapDc);

    public static native void freopenStdio();

    public static native long createThread(
            int dwStackSize,
            int dwCreationFlags,
            Runnable runnable
    );

    public static native long waitForSingleObject(
            long hHandle,
            long dwMillSeconds
    );

    public static native long waitForMultipleObjects(
            long[] hHandles,
            boolean waitAll,
            long dwMillSeconds);

    public static native long winAppCreateBitmap(int width, int height);

    public static native void winAppResizeCompatibleDC(
            long hdc,
            long pBDC,
            int newWidth,
            int newHeight,
            int resizeMode);

    public static native int winAppCreateWin32App(
            String className,
            String windowTitle,
            String iconFileName,
            int nCmdShow,
            boolean showConsole,
            int mdcResizeMode,
            WinAppCallbacker callbacker);

}

