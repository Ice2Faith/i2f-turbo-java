package i2f.natives.windows.consts.file;

/**
 * @author Ice2Faith
 * @date 2024/5/14 15:57
 * @desc
 */
public interface WinFileEncryptionStatusReturn {
    int RETURN_ERROR = -1;
    int FILE_ENCRYPTABLE = 0;
    int FILE_IS_ENCRYPTED = 1;
    int FILE_SYSTEM_ATTR = 2;
    int FILE_ROOT_DIR = 3;
    int FILE_SYSTEM_DIR = 4;
    int FILE_UNKNOWN = 5;
    int FILE_SYSTEM_NOT_SUPPORT = 6;
    int FILE_USER_DISALLOWED = 7;
    int FILE_READ_ONLY = 8;
    int FILE_DIR_DISALLOWED = 9;
}
