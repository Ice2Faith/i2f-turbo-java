package i2f.natives.windows.consts.window;

/**
 * @author Ice2Faith
 * @date 2024/5/9 9:50
 * @desc
 */
public interface WinGetWindowCmd {
    int GW_HWNDFIRST = 0;
    int GW_HWNDLAST = 1;
    int GW_HWNDNEXT = 2;
    int GW_HWNDPREV = 3;
    int GW_OWNER = 4;
    int GW_CHILD = 5;
    // #if(WINVER <= 0x0400)
    int GW_MAX = 5;
    // #else
    int GW_ENABLEDPOPUP = 6;
// int GW_MAX              =6;
// #endif
}
