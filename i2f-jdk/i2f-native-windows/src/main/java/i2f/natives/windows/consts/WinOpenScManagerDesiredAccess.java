package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/11 11:06
 * @desc
 */
public interface WinOpenScManagerDesiredAccess {
    int SC_MANAGER_CONNECT = 0x0001;
    int SC_MANAGER_CREATE_SERVICE = 0x0002;
    int SC_MANAGER_ENUMERATE_SERVICE = 0x0004;
    int SC_MANAGER_LOCK = 0x0008;
    int SC_MANAGER_QUERY_LOCK_STATUS = 0x0010;
    int SC_MANAGER_MODIFY_BOOT_CONFIG = 0x0020;

    int SC_MANAGER_ALL_ACCESS = (WinStandardAccessType.STANDARD_RIGHTS_REQUIRED |
            SC_MANAGER_CONNECT |
            SC_MANAGER_CREATE_SERVICE |
            SC_MANAGER_ENUMERATE_SERVICE |
            SC_MANAGER_LOCK |
            SC_MANAGER_QUERY_LOCK_STATUS |
            SC_MANAGER_MODIFY_BOOT_CONFIG);


}
