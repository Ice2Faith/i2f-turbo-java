package i2f.natives.windows.test;

import i2f.graphics.d2.Point;
import i2f.graphics.d2.shape.Rectangle;
import i2f.natives.windows.WinApi;
import i2f.natives.windows.consts.device.WinKeyboardEventVk;
import i2f.natives.windows.consts.gdi.WinBitBltRop;
import i2f.natives.windows.consts.winapp.WinAppResizeMode;
import i2f.natives.windows.consts.window.WinMessageCallbacker;
import i2f.natives.windows.consts.window.WinSendMessageMsg;
import i2f.natives.windows.consts.window.WinShowWindowCmdShow;
import i2f.natives.windows.types.gdi.HBrush;
import i2f.natives.windows.types.gdi.HGdiObj;
import i2f.natives.windows.types.gdi.HIcon;
import i2f.natives.windows.types.gdi.Hdc;
import i2f.natives.windows.types.process.HInstance;
import i2f.natives.windows.types.process.HModule;
import i2f.natives.windows.types.winapp.BitmapDcPtr;
import i2f.natives.windows.types.window.BitmapDcInfo;
import i2f.natives.windows.types.window.Hwnd;
import i2f.natives.windows.types.window.MsgPtr;
import i2f.natives.windows.types.window.PaintStructPtr;

import java.io.File;

/**
 * @author Ice2Faith
 * @date 2024/5/16 21:24
 * @desc
 */
public class TestWinRawWindow {
    public static void main(String[] args) throws InterruptedException {
        app();
    }

    public static void app() {
        HModule hModule = WinApi.getModuleHandle(null);
        HInstance hInstance = new HInstance(hModule);

        HIcon hIcon = new HIcon(0);
        File iconFile = new File("win32.ico");
        if (iconFile.exists()) {
            hIcon = WinApi.extractIcon(hInstance, iconFile.getAbsolutePath(), 0);
        }

//        WinApi.freeConsole();

        String className = "JniJavaWin32Application";
        WinApi.registerClassEx(hInstance, hIcon, className);

        String windowTitle = "JNI-JAVA-Win32应用程序";
        Hwnd hWnd = WinApi.createWindowEx(className, windowTitle, hInstance);

        BitmapDcPtr bitmapDcPtr = WinApi.mallocBitMapDc();

        int resizeMode = WinAppResizeMode.RESIZE_MODE_ADAPT;
        if (!bitmapDcPtr.isZero()) {
            Rectangle rect = WinApi.getClientRect(hWnd);

            int newWidth = (int) rect.size.dx;
            int newHeight = (int) rect.size.dy;

            Hdc hdc = WinApi.getDC(hWnd);
            WinApi.winAppResizeCompatibleDC(hdc, bitmapDcPtr, newWidth, newHeight, resizeMode);
            WinApi.releaseDC(hWnd, hdc);
        }

        MsgPtr msgPtr = WinApi.mallocMsg();

        WinApi.bindMessageCallbacker(hWnd, new WinMessageCallbacker() {
            private boolean showConsole = false;
            private boolean isLeftDown = false;
            private Point lastPoint = new Point(0, 0);

            @Override
            public long handle(Hwnd hWnd, long message, long wParam, long lParam) {
                BitmapDcInfo mdc = WinApi.getBitmapDcInfo(bitmapDcPtr);
                switch ((int) message) {
                    case WinSendMessageMsg.WM_CREATE:

                        break;
                    case WinSendMessageMsg.WM_COMMAND:

                        break;
                    case WinSendMessageMsg.WM_PAINT: {
                        PaintStructPtr ps = WinApi.mallocPaintStruct();
                        Rectangle rect = WinApi.getClientRect(hWnd);
                        Hdc hdc = WinApi.beginPaint(hWnd, ps);
                        WinApi.bitBlt(hdc, 0, 0, (int) rect.size.dx, (int) rect.size.dy, mdc.hdc, 0, 0, WinBitBltRop.SRCCOPY);
                        WinApi.endPaint(hWnd, ps);
                        WinApi.freeMallocPtr(ps);
                    }

                    break;
                    case WinSendMessageMsg.WM_DESTROY:
                        WinApi.postQuitMessage(0);
                        WinApi.deleteDC(mdc.hdc);
                        WinApi.deleteObject(mdc.hBitmap);
                        WinApi.freeMallocPtr(bitmapDcPtr);
                        WinApi.freeMallocPtr(msgPtr);
                        break;
                    case WinSendMessageMsg.WM_KEYUP: {
                        if (wParam == WinKeyboardEventVk.VK_F12) {
                            showConsole = !showConsole;
                            if (showConsole) {
                                WinApi.allocConsole();

                                // 重定向标准输入输出
                                WinApi.freopenStdio();

                                Hwnd h = WinApi.getConsoleWindow();
                                String title = WinApi.getWindowText(hWnd);
                                title += " - Console";
                                WinApi.setWindowText(h, title);
                                WinApi.setWindowTransparentAlpha(h, 0.25);
//                                WinApi.postMessage(h, WinSendMessageMsg.WM_SETICON, ICON_BIG, (LPARAM)p_instance->icon);
                            } else {
                                Hwnd h = WinApi.getConsoleWindow();
                                WinApi.freeConsole();
                                WinApi.postMessage(h, WinSendMessageMsg.WM_DESTROY, 0, 0);
                                WinApi.postMessage(h, WinSendMessageMsg.WM_QUIT, 0, 0);
                            }
                        }
                    }
                    break;
                    case WinSendMessageMsg.WM_LBUTTONDOWN: {
                        isLeftDown = true;
                        lastPoint.x = WinApi.getXLParam(lParam);
                        lastPoint.y = WinApi.getYLParam(lParam);

                    }
                    WinApi.invalidateRect(hWnd, false);
                    break;
                    case WinSendMessageMsg.WM_LBUTTONUP: {
                        isLeftDown = false;
                        lastPoint.x = WinApi.getXLParam(lParam);
                        lastPoint.y = WinApi.getYLParam(lParam);
                    }
                    WinApi.invalidateRect(hWnd, false);
                    break;
                    case WinSendMessageMsg.WM_RBUTTONUP: {
                        HBrush brush = WinApi.createSolidBrush(0xffffff);
                        Hdc pdc = mdc.hdc;
                        HGdiObj oldBrush = WinApi.selectObject(pdc, brush);
                        WinApi.rectangle(pdc, -1, -1, mdc.width + 1, mdc.height + 1);
                        WinApi.selectObject(pdc, oldBrush);
                        WinApi.deleteObject(brush);
//                        WinApi.releaseDC(hWnd,pdc);
                        WinApi.invalidateRect(hWnd, true);
                    }
                    WinApi.invalidateRect(hWnd, false);
                    break;
                    case WinSendMessageMsg.WM_MOUSEMOVE: {
                        int x = WinApi.getXLParam(lParam);
                        int y = WinApi.getYLParam(lParam);

                        if (isLeftDown) {
                            Hdc pdc = mdc.hdc;
                            WinApi.moveToEx(pdc, new Point(lastPoint.x, lastPoint.y));
                            WinApi.lineTo(pdc, new Point(x, y));
//                            WinApi.releaseDC(hWnd,pdc);
                        }

                        lastPoint.x = x;
                        lastPoint.y = y;
                        System.out.println(x + "," + y);
                    }
                    WinApi.invalidateRect(hWnd, false);
                    break;
                    case WinSendMessageMsg.WM_SIZE: {
                        Rectangle rect = WinApi.getClientRect(hWnd);
                        int newWidth = (int) rect.size.dx;
                        int newHeight = (int) rect.size.dy;
                        Hdc dc = WinApi.getDC(hWnd);
                        WinApi.winAppResizeCompatibleDC(dc, bitmapDcPtr, newWidth, newHeight, resizeMode);
                        WinApi.releaseDC(hWnd, dc);

                    }
                    break;
                    default:
                        return WinApi.defWindowProc(hWnd, message, wParam, lParam);
                }

                return 0;
            }
        });

        WinApi.showWindow(hWnd, WinShowWindowCmdShow.SW_SHOW);
        WinApi.updateWindow(hWnd);


        while (WinApi.getMessage(msgPtr, new Hwnd(0), 0, 0)) {
            WinApi.translateMessage(msgPtr);
            WinApi.dispatchMessage(msgPtr);
        }

        System.out.println("done");
    }
}
