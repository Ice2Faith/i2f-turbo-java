package i2f.natives.windows.consts.window;

/**
 * @author Ice2Faith
 * @date 2024/5/9 9:25
 * @desc
 */
public interface WinSetWindowPosFlag {
    int SWP_NOSIZE = 0x0001;
    int SWP_NOMOVE = 0x0002;
    int SWP_NOZORDER = 0x0004;
    int SWP_NOREDRAW = 0x0008;
    int SWP_NOACTIVATE = 0x0010;
    int SWP_FRAMECHANGED = 0x0020;  /* The frame changed: send WM_NCCALCSIZE */
    int SWP_SHOWWINDOW = 0x0040;
    int SWP_HIDEWINDOW = 0x0080;
    int SWP_NOCOPYBITS = 0x0100;
    int SWP_NOOWNERZORDER = 0x0200;  /* Don't do owner Z ordering */
    int SWP_NOSENDCHANGING = 0x0400;  /* Don't send WM_WINDOWPOSCHANGING */

    int SWP_DRAWFRAME = SWP_FRAMECHANGED;
    int SWP_NOREPOSITION = SWP_NOOWNERZORDER;

    // #if(WINVER >= 0x0400)
    int SWP_DEFERERASE = 0x2000;
    int SWP_ASYNCWINDOWPOS = 0x4000;
// #endif /* WINVER >= 0x0400 */

}
