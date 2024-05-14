package i2f.natives.windows.consts.device;

/**
 * @author Ice2Faith
 * @date 2024/5/10 15:02
 * @desc
 */
public interface WinMouseEventFlag {

    int MOUSEEVENTF_MOVE = 0x0001; /* mouse move */
    int MOUSEEVENTF_LEFTDOWN = 0x0002; /* left button down */
    int MOUSEEVENTF_LEFTUP = 0x0004; /* left button up */
    int MOUSEEVENTF_RIGHTDOWN = 0x0008; /* right button down */
    int MOUSEEVENTF_RIGHTUP = 0x0010; /* right button up */
    int MOUSEEVENTF_MIDDLEDOWN = 0x0020; /* middle button down */
    int MOUSEEVENTF_MIDDLEUP = 0x0040; /* middle button up */
    int MOUSEEVENTF_XDOWN = 0x0080; /* x button down */
    int MOUSEEVENTF_XUP = 0x0100; /* x button down */
    int MOUSEEVENTF_WHEEL = 0x0800; /* wheel button rolled */
    // #if (_WIN32_WINNT >= 0x0600)
    int MOUSEEVENTF_HWHEEL = 0x01000; /* hwheel button rolled */
    // #endif
// #if(WINVER >= 0x0600)
    int MOUSEEVENTF_MOVE_NOCOALESCE = 0x2000; /* do not coalesce mouse moves */
    //#endif /* WINVER >= 0x0600 */
    int MOUSEEVENTF_VIRTUALDESK = 0x4000; /* map to entire virtual desktop */
    int MOUSEEVENTF_ABSOLUTE = 0x8000; /* absolute move */

}
