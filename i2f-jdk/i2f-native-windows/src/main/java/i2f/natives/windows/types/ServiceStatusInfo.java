package i2f.natives.windows.types;

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
}
