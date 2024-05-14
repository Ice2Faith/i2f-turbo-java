package i2f.natives.windows.consts.service;

/**
 * @author Ice2Faith
 * @date 2024/5/11 15:10
 * @desc
 */
public interface WinCreateServiceErrorControl {
    int SERVICE_ERROR_IGNORE = 0x00000000;
    int SERVICE_ERROR_NORMAL = 0x00000001;
    int SERVICE_ERROR_SEVERE = 0x00000002;
    int SERVICE_ERROR_CRITICAL = 0x00000003;
}
