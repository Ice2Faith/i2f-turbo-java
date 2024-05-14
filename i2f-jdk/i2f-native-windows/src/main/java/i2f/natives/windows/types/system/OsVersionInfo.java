package i2f.natives.windows.types.system;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/14 16:43
 * @desc
 */
@Data
public class OsVersionInfo {
    public int dwOSVersionInfoSize;
    public int dwMajorVersion;
    public int dwMinorVersion;
    public int dwBuildNumber;
    public int dwPlatformId;
    public String szCSDVersion;
}
