package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/11 13:56
 * @desc
 */
public interface WinOpenServiceDesiredAccess {
    int SERVICE_QUERY_CONFIG = 0x0001;
    int SERVICE_CHANGE_CONFIG = 0x0002;
    int SERVICE_QUERY_STATUS = 0x0004;
    int SERVICE_ENUMERATE_DEPENDENTS = 0x0008;
    int SERVICE_START = 0x0010;
    int SERVICE_STOP = 0x0020;
    int SERVICE_PAUSE_CONTINUE = 0x0040;
    int SERVICE_INTERROGATE = 0x0080;
    int SERVICE_USER_DEFINED_CONTROL = 0x0100;

    int SERVICE_ALL_ACCESS = (WinStandardAccessType.STANDARD_RIGHTS_REQUIRED |
            SERVICE_QUERY_CONFIG |
            SERVICE_CHANGE_CONFIG |
            SERVICE_QUERY_STATUS |
            SERVICE_ENUMERATE_DEPENDENTS |
            SERVICE_START |
            SERVICE_STOP |
            SERVICE_PAUSE_CONTINUE |
            SERVICE_INTERROGATE |
            SERVICE_USER_DEFINED_CONTROL);
}
