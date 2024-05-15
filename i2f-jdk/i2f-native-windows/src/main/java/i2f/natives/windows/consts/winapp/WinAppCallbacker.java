package i2f.natives.windows.consts.winapp;

import i2f.natives.windows.types.winapp.Win32AppInstancePtr;
import i2f.natives.windows.types.window.Hwnd;

/**
 * @author Ice2Faith
 * @date 2024/5/15 21:13
 * @desc
 */
public interface WinAppCallbacker {
    default long callback(long p_instance, long hWnd, long message, long wParam, long lParam){
        return handle(new Win32AppInstancePtr(p_instance),new Hwnd(hWnd),message,wParam,lParam);
    }

    long handle(Win32AppInstancePtr pInstance, Hwnd hwnd,long message,long wParam,long lParam);
}
