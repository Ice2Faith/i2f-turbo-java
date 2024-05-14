package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/14 16:00
 * @desc
 */
public interface WinGetBinaryTypeReturn {
    int RETURN_ERROR = -1;
    int SCS_32BIT_BINARY = 0;
    int SCS_DOS_BINARY = 1;
    int SCS_WOW_BINARY = 2;
    int SCS_PIF_BINARY = 3;
    int SCS_POSIX_BINARY = 4;
    int SCS_OS216_BINARY = 5;
    int SCS_64BIT_BINARY = 6;
}
