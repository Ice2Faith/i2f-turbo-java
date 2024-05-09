package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/9 17:19
 * @desc
 */
public interface WinFileAttribute {
    int FILE_ATTRIBUTE_READONLY = 0x00000001;
    int FILE_ATTRIBUTE_HIDDEN = 0x00000002;
    int FILE_ATTRIBUTE_SYSTEM = 0x00000004;
    int FILE_ATTRIBUTE_DIRECTORY = 0x00000010;
    int FILE_ATTRIBUTE_ARCHIVE = 0x00000020;
    int FILE_ATTRIBUTE_DEVICE = 0x00000040;
    int FILE_ATTRIBUTE_NORMAL = 0x00000080;
    int FILE_ATTRIBUTE_TEMPORARY = 0x00000100;
    int FILE_ATTRIBUTE_SPARSE_FILE = 0x00000200;
    int FILE_ATTRIBUTE_REPARSE_POINT = 0x00000400;
    int FILE_ATTRIBUTE_COMPRESSED = 0x00000800;
    int FILE_ATTRIBUTE_OFFLINE = 0x00001000;
    int FILE_ATTRIBUTE_NOT_CONTENT_INDEXED = 0x00002000;
    int FILE_ATTRIBUTE_ENCRYPTED = 0x00004000;
    int FILE_ATTRIBUTE_VIRTUAL = 0x00010000;

    static boolean isFile(long attribute) {
        return attribute != 0 && (FILE_ATTRIBUTE_DIRECTORY & attribute) == 0;
    }

    static boolean isDirectory(long attribute) {
        return (FILE_ATTRIBUTE_DIRECTORY & attribute) != 0;
    }
}
