package i2f.natives.windows.consts.register;

/**
 * @author Ice2Faith
 * @date 2024/5/11 8:38
 * @desc
 */
public interface WinRegValueType {

    int REG_NONE = (0);   // No value type
    int REG_SZ = (1);   // Unicode nul terminated string
    int REG_EXPAND_SZ = (2);   // Unicode nul terminated string
    // (with environment variable references)
    int REG_BINARY = (3);   // Free form binary
    int REG_DWORD = (4);   // 32-bit number
    int REG_DWORD_LITTLE_ENDIAN = (4);   // 32-bit number (same as REG_DWORD)
    int REG_DWORD_BIG_ENDIAN = (5);   // 32-bit number
    int REG_LINK = (6);   // Symbolic Link (unicode)
    int REG_MULTI_SZ = (7);   // Multiple Unicode strings
    int REG_RESOURCE_LIST = (8);   // Resource list in the resource map
    int REG_FULL_RESOURCE_DESCRIPTOR = (9);  // Resource list in the hardware description
    int REG_RESOURCE_REQUIREMENTS_LIST = (10);
    int REG_QWORD = (11);  // 64-bit number
    int REG_QWORD_LITTLE_ENDIAN = (11);  // 64-bit number (same as REG_QWORD)

}
