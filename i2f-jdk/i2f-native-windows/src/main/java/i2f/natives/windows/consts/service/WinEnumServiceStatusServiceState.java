package i2f.natives.windows.consts.service;

/**
 * @author Ice2Faith
 * @date 2024/5/11 11:38
 * @desc
 */
public interface WinEnumServiceStatusServiceState {
    int SERVICE_ACTIVE = 0x00000001;
    int SERVICE_INACTIVE = 0x00000002;
    int SERVICE_STATE_ALL = (SERVICE_ACTIVE |
            SERVICE_INACTIVE);
}
