package i2f.natives.windows.consts.window;

import i2f.natives.windows.types.window.Hwnd;

/**
 * @author Ice2Faith
 * @date 2024/5/15 21:13
 * @desc
 */
public interface WinMessageCallbacker {
    default long callback(long hWnd, long message, long wParam, long lParam) {
        return handle(new Hwnd(hWnd), message, wParam, lParam);
    }

    long handle(Hwnd hwnd, long message, long wParam, long lParam);
}
