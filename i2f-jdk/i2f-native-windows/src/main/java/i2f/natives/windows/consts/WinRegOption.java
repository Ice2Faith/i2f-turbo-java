package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/11 9:18
 * @desc
 */
public interface WinRegOption {

    int REG_OPTION_RESERVED = (0x00000000);   // Parameter is reserved

    int REG_OPTION_NON_VOLATILE = (0x00000000);   // Key is preserved
// when system is rebooted

    int REG_OPTION_VOLATILE = (0x00000001);   // Key is not preserved
// when system is rebooted

    int REG_OPTION_CREATE_LINK = (0x00000002);   // Created key is a
// symbolic link

    int REG_OPTION_BACKUP_RESTORE = (0x00000004);   // open for backup or restore
// special access rules
// privilege required

    int REG_OPTION_OPEN_LINK = (0x00000008);   // Open symbolic link

    int REG_LEGAL_OPTION =
            (REG_OPTION_RESERVED |
                    REG_OPTION_NON_VOLATILE |
                    REG_OPTION_VOLATILE |
                    REG_OPTION_CREATE_LINK |
                    REG_OPTION_BACKUP_RESTORE |
                    REG_OPTION_OPEN_LINK);

    int REG_OPEN_LEGAL_OPTION =
            (REG_OPTION_RESERVED |
                    REG_OPTION_BACKUP_RESTORE |
                    REG_OPTION_OPEN_LINK);

}
