package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/10 16:01
 * @desc
 */
public interface WinRegOpenKeyHkey {

    long HKEY_CLASSES_ROOT = 0x80000000L;
    long HKEY_CURRENT_USER = 0x80000001L;
    long HKEY_LOCAL_MACHINE = 0x80000002L;
    long HKEY_USERS = 0x80000003L;
    long HKEY_PERFORMANCE_DATA = 0x80000004L;
    long HKEY_PERFORMANCE_TEXT = 0x80000050L;
    long HKEY_PERFORMANCE_NLSTEXT = 0x80000060L;
    // #if(WINVER >= 0x0400)
    long HKEY_CURRENT_CONFIG = 0x80000005L;
    long HKEY_DYN_DATA = 0x80000006L;
    long HKEY_CURRENT_USER_LOCAL_SETTINGS = 0x80000007L;

}
