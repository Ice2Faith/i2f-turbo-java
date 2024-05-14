package i2f.natives.windows.types.process;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/10 9:54
 * @desc
 */
@Data
public class ProcessEntry32 {
    public int dwSize;
    public int cntUsage;
    public long th32ProcessID;
    public long th32DefaultHeapID;
    public long th32ModuleID;
    public int cntThreads;
    public long th32ParentProcessID;
    public long pcPriClassBase;
    public long dwFlags;
    public String szExeFile;
}
