package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/14 16:52
 * @desc
 */
public interface WinExitWindowsFlag {

    int EWX_LOGOFF = 0;
    int EWX_SHUTDOWN = 0x00000001;
    int EWX_REBOOT = 0x00000002;
    int EWX_FORCE = 0x00000004;
    int EWX_POWEROFF = 0x00000008;
    // #if(_WIN32_WINNT >= 0x0500)
    int EWX_FORCEIFHUNG = 0x00000010;
    // #endif /* _WIN32_WINNT >= 0x0500 */
    int EWX_QUICKRESOLVE = 0x00000020;
    // #if(_WIN32_WINNT >= 0x0600)
    int EWX_RESTARTAPPS = 0x00000040;
// #endif /* _WIN32_WINNT >= 0x0600 */
}
