package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/11 15:09
 * @desc
 */
public interface WinCreateServiceStartType {
    int SERVICE_BOOT_START = 0x00000000;
    int SERVICE_SYSTEM_START = 0x00000001;
    int SERVICE_AUTO_START = 0x00000002;
    int SERVICE_DEMAND_START = 0x00000003;
    int SERVICE_DISABLED = 0x00000004;
}
