package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/12 17:49
 * @desc
 */
public interface WinHotKey {

    int HOTKEYF_SHIFT = 0x01;
    int HOTKEYF_CONTROL = 0x02;
    int HOTKEYF_ALT = 0x04;
    //#ifdef _MAC
    int HOTKEYF_EXT = 0x80;
// #else
// int HOTKEYF_EXT             =0x08;
// #endif

    int HKCOMB_NONE = 0x0001;
    int HKCOMB_S = 0x0002;
    int HKCOMB_C = 0x0004;
    int HKCOMB_A = 0x0008;
    int HKCOMB_SC = 0x0010;
    int HKCOMB_SA = 0x0020;
    int HKCOMB_CA = 0x0040;
    int HKCOMB_SCA = 0x0080;


    int HKM_SETHOTKEY = (WinSendMessageMsg.WM_USER + 1);
    int HKM_GETHOTKEY = (WinSendMessageMsg.WM_USER + 2);
    int HKM_SETRULES = (WinSendMessageMsg.WM_USER + 3);

// #ifdef _WIN32

    String HOTKEY_CLASS_32 = "msctls_hotkey32";


    String HOTKEY_CLASS = "msctls_hotkey";

}
