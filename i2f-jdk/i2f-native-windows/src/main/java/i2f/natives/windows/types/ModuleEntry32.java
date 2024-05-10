package i2f.natives.windows.types;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/10 10:06
 * @desc
 */
@Data
public class ModuleEntry32 {
    public int dwSize;
    public long th32ModuleID;
    public long th32ProcessID;
    public int GlblcntUsage;
    public int ProccntUsage;
    public long modBaseSize;
    public HModule hModule;
    public String szModule;
    public String szExePath;
}
