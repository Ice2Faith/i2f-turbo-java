package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/9 9:08
 * @desc
 */
public interface WinMessageBoxType {
    int MB_OK = 0x00000000;
    int MB_OKCANCEL = 0x00000001;
    int MB_ABORTRETRYIGNORE = 0x00000002;
    int MB_YESNOCANCEL = 0x00000003;
    int MB_YESNO = 0x00000004;
    int MB_RETRYCANCEL = 0x00000005;
    // #if(WINVER >= 0x0500)
    int MB_CANCELTRYCONTINUE = 0x00000006;
// #endif /* WINVER >= 0x0500 */


    int MB_ICONHAND = 0x00000010;
    int MB_ICONQUESTION = 0x00000020;
    int MB_ICONEXCLAMATION = 0x00000030;
    int MB_ICONASTERISK = 0x00000040;

    // #if(WINVER >= 0x0400)
    int MB_USERICON = 0x00000080;
    int MB_ICONWARNING = MB_ICONEXCLAMATION;
    int MB_ICONERROR = MB_ICONHAND;
// #endif /* WINVER >= 0x0400 */

    int MB_ICONINFORMATION = MB_ICONASTERISK;
    int MB_ICONSTOP = MB_ICONHAND;

    int MB_DEFBUTTON1 = 0x00000000;
    int MB_DEFBUTTON2 = 0x00000100;
    int MB_DEFBUTTON3 = 0x00000200;
    // #if(WINVER >= 0x0400)
    int MB_DEFBUTTON4 = 0x00000300;
// #endif /* WINVER >= 0x0400 */

    int MB_APPLMODAL = 0x00000000;
    int MB_SYSTEMMODAL = 0x00001000;
    int MB_TASKMODAL = 0x00002000;
    // #if(WINVER >= 0x0400)
    int MB_HELP = 0x00004000; // Help Button
// #endif /* WINVER >= 0x0400 */

    int MB_NOFOCUS = 0x00008000;
    int MB_SETFOREGROUND = 0x00010000;
    int MB_DEFAULT_DESKTOP_ONLY = 0x00020000;

    // #if(WINVER >= 0x0400)
    int MB_TOPMOST = 0x00040000;
    int MB_RIGHT = 0x00080000;
    int MB_RTLREADING = 0x00100000;
// #endif /* WINVER >= 0x0400 */

    // #ifdef _WIN32_WINNT
// #if (_WIN32_WINNT >= 0x0400)
    int MB_SERVICE_NOTIFICATION = 0x00200000;
    // #else
// int MB_SERVICE_NOTIFICATION          =0x00040000;
//#endif
    int MB_SERVICE_NOTIFICATION_NT3X = 0x00040000;
// #endif

    int MB_TYPEMASK = 0x0000000F;
    int MB_ICONMASK = 0x000000F0;
    int MB_DEFMASK = 0x00000F00;
    int MB_MODEMASK = 0x00003000;
    int MB_MISCMASK = 0x0000C000;

}
