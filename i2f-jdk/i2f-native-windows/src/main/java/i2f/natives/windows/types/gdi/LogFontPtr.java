package i2f.natives.windows.types.gdi;

import i2f.natives.core.MallocPtr;
import i2f.natives.core.Ptr;

/**
 * @author Ice2Faith
 * @date 2024/5/10 8:32
 * @desc
 */
public class LogFontPtr extends MallocPtr {
    public LogFontPtr(long ptr) {
        super(ptr);
    }

    public LogFontPtr(Ptr ptr) {
        super(ptr);
    }
}
