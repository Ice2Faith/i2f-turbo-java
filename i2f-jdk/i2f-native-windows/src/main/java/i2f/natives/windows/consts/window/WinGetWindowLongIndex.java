package i2f.natives.windows.consts.window;

/**
 * @author Ice2Faith
 * @date 2024/5/9 13:57
 * @desc
 */
public interface WinGetWindowLongIndex {
    int GWL_WNDPROC = -4;
    int GWL_HINSTANCE = -6;
    int GWL_HWNDPARENT = -8;
    int GWL_STYLE = -16;
    int GWL_EXSTYLE = -20;
    int GWL_USERDATA = -21;
    int GWL_ID = -12;

// #ifdef _WIN64

// #undef GWL_WNDPROC
// #undef GWL_HINSTANCE
// #undef GWL_HWNDPARENT
// #undef GWL_USERDATA

// #endif /* _WIN64 */

    int GWLP_WNDPROC = -4;
    int GWLP_HINSTANCE = -6;
    int GWLP_HWNDPARENT = -8;
    int GWLP_USERDATA = -21;
    int GWLP_ID = -12;
}
