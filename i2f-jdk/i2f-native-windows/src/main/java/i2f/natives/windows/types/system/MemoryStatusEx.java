package i2f.natives.windows.types.system;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/14 16:27
 * @desc
 */
@Data
public class MemoryStatusEx {
    public long dwLength;
    public long dwMemoryLoad;
    public long ullTotalPhys;
    public long ullAvailPhys;
    public long ullTotalPageFile;
    public long ullAvailPageFile;
    public long ullTotalVirtual;
    public long ullAvailVirtual;
    public long ullAvailExtendedVirtual;
}
