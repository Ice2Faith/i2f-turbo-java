package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/10 15:00
 * @desc
 */
public interface WinKeyboardEventFlag {
    int KEYEVENTF_KEYDOWN = 0x0000; // default is key down

    int KEYEVENTF_EXTENDEDKEY = 0x0001;
    int KEYEVENTF_KEYUP = 0x0002;
    // #if(_WIN32_WINNT >= 0x0500)
    int KEYEVENTF_UNICODE = 0x0004;
    int KEYEVENTF_SCANCODE = 0x0008;
// #endif /* _WIN32_WINNT >= 0x0500 */
}
