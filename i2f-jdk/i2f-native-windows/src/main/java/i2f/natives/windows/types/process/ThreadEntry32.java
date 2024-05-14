package i2f.natives.windows.types.process;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/10 10:56
 * @desc
 */
@Data
public class ThreadEntry32 {
    public int dwSize;
    public int cntUsage;
    public long th32ThreadID;
    public long th32OwnerProcessID;
    public long tpBasePri;
    public long tpDeltaPri;
    public long dwFlags;
}
