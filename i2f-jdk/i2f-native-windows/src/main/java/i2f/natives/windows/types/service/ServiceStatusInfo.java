package i2f.natives.windows.types.service;

import i2f.natives.windows.consts.service.WinServiceCurrentState;
import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/11 11:27
 * @desc
 */
@Data
public class ServiceStatusInfo {
    public String serviceName;
    public String displayName;
    public int currentState;
    public int serviceType;
    public long controlsAccepted;
    public long win32ExitCode;
    public long serviceSpecificExitCode;
    public long checkPoint;
    public long waitHint;

    public boolean isStopped() {
        return currentState == WinServiceCurrentState.SERVICE_STOPPED;
    }

    public boolean isRunning() {
        return currentState == WinServiceCurrentState.SERVICE_RUNNING;
    }

    public boolean isPaused() {
        return currentState == WinServiceCurrentState.SERVICE_PAUSED;
    }
}
