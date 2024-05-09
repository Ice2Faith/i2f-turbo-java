package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/9 11:19
 * @desc
 */
public interface WinOpenProcessTokenDesiredAccess {


    int TOKEN_ASSIGN_PRIMARY = 0x0001;
    int TOKEN_DUPLICATE = 0x0002;
    int TOKEN_IMPERSONATE = 0x0004;
    int TOKEN_QUERY = 0x0008;
    int TOKEN_QUERY_SOURCE = 0x0010;
    int TOKEN_ADJUST_PRIVILEGES = 0x0020;
    int TOKEN_ADJUST_GROUPS = 0x0040;
    int TOKEN_ADJUST_DEFAULT = 0x0080;
    int TOKEN_ADJUST_SESSIONID = 0x0100;

    int TOKEN_ALL_ACCESS_P = (WinStandardAccessType.STANDARD_RIGHTS_REQUIRED |
            TOKEN_ASSIGN_PRIMARY |
            TOKEN_DUPLICATE |
            TOKEN_IMPERSONATE |
            TOKEN_QUERY |
            TOKEN_QUERY_SOURCE |
            TOKEN_ADJUST_PRIVILEGES |
            TOKEN_ADJUST_GROUPS |
            TOKEN_ADJUST_DEFAULT);

    //#if ((defined(_WIN32_WINNT) && (_WIN32_WINNT > 0x0400)) || (!defined(_WIN32_WINNT)))
    int TOKEN_ALL_ACCESS = (TOKEN_ALL_ACCESS_P |
            TOKEN_ADJUST_SESSIONID);
// #else
// int TOKEN_ALL_ACCESS  =(TOKEN_ALL_ACCESS_P);
// #endif

    int TOKEN_READ = (WinStandardAccessType.STANDARD_RIGHTS_READ |
            TOKEN_QUERY);


    int TOKEN_WRITE = (WinStandardAccessType.STANDARD_RIGHTS_WRITE |
            TOKEN_ADJUST_PRIVILEGES |
            TOKEN_ADJUST_GROUPS |
            TOKEN_ADJUST_DEFAULT);

    int TOKEN_EXECUTE = (WinStandardAccessType.STANDARD_RIGHTS_EXECUTE);


}
