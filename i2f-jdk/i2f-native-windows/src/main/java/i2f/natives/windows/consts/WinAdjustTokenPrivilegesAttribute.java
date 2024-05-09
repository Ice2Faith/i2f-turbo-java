package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/9 11:51
 * @desc
 */
public interface WinAdjustTokenPrivilegesAttribute {
    int SE_PRIVILEGE_ENABLED_BY_DEFAULT = 0x00000001;
    int SE_PRIVILEGE_ENABLED = 0x00000002;
    int SE_PRIVILEGE_REMOVED = 0X00000004;
    int SE_PRIVILEGE_USED_FOR_ACCESS = 0x80000000;

    int SE_PRIVILEGE_VALID_ATTRIBUTES = (SE_PRIVILEGE_ENABLED_BY_DEFAULT |
            SE_PRIVILEGE_ENABLED |
            SE_PRIVILEGE_REMOVED |
            SE_PRIVILEGE_USED_FOR_ACCESS);

}
