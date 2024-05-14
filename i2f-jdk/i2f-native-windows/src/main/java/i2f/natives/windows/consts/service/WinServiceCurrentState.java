package i2f.natives.windows.consts.service;

/**
 * @author Ice2Faith
 * @date 2024/5/11 14:30
 * @desc
 */
public interface WinServiceCurrentState {
    int SERVICE_STOPPED = 0x00000001;
    int SERVICE_START_PENDING = 0x00000002;
    int SERVICE_STOP_PENDING = 0x00000003;
    int SERVICE_RUNNING = 0x00000004;
    int SERVICE_CONTINUE_PENDING = 0x00000005;
    int SERVICE_PAUSE_PENDING = 0x00000006;
    int SERVICE_PAUSED = 0x00000007;
}
