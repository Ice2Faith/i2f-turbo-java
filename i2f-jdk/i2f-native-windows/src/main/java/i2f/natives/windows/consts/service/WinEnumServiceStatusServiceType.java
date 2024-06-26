package i2f.natives.windows.consts.service;

/**
 * @author Ice2Faith
 * @date 2024/5/11 11:40
 * @desc
 */
public interface WinEnumServiceStatusServiceType {
    int SERVICE_KERNEL_DRIVER = 0x00000001;
    int SERVICE_FILE_SYSTEM_DRIVER = 0x00000002;
    int SERVICE_ADAPTER = 0x00000004;
    int SERVICE_RECOGNIZER_DRIVER = 0x00000008;

    int SERVICE_DRIVER = (SERVICE_KERNEL_DRIVER |
            SERVICE_FILE_SYSTEM_DRIVER |
            SERVICE_RECOGNIZER_DRIVER);

    int SERVICE_WIN32_OWN_PROCESS = 0x00000010;
    int SERVICE_WIN32_SHARE_PROCESS = 0x00000020;
    int SERVICE_WIN32 = (SERVICE_WIN32_OWN_PROCESS |
            SERVICE_WIN32_SHARE_PROCESS);

    int SERVICE_INTERACTIVE_PROCESS = 0x00000100;

    int SERVICE_TYPE_ALL = (SERVICE_WIN32 |
            SERVICE_ADAPTER |
            SERVICE_DRIVER |
            SERVICE_INTERACTIVE_PROCESS);

}
