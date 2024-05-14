package i2f.natives.windows.consts.window;

/**
 * @author Ice2Faith
 * @date 2024/5/9 14:01
 * @desc
 */
public interface WinGetClassLongIndex {
    int GCL_MENUNAME = -8;
    int GCL_HBRBACKGROUND = -10;
    int GCL_HCURSOR = -12;
    int GCL_HICON = -14;
    int GCL_HMODULE = -16;
    int GCL_CBWNDEXTRA = -18;
    int GCL_CBCLSEXTRA = -20;
    int GCL_WNDPROC = -24;
    int GCL_STYLE = -26;
    int GCW_ATOM = -32;

    // #if(WINVER >= 0x0400)
    int GCL_HICONSM = -34;
// #endif /* WINVER >= 0x0400 */

// #ifdef _WIN64

// #undef GCL_MENUNAME
// #undef GCL_HBRBACKGROUND
// #undef GCL_HCURSOR
// #undef GCL_HICON
// #undef GCL_HMODULE
// #undef GCL_WNDPROC
// #undef GCL_HICONSM

// #endif /* _WIN64 */

    int GCLP_MENUNAME = -8;
    int GCLP_HBRBACKGROUND = -10;
    int GCLP_HCURSOR = -12;
    int GCLP_HICON = -14;
    int GCLP_HMODULE = -16;
    int GCLP_WNDPROC = -24;
    int GCLP_HICONSM = -34;

}
