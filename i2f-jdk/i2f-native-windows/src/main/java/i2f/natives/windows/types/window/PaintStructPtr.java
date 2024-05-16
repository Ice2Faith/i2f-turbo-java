package i2f.natives.windows.types.window;

import i2f.natives.core.MallocPtr;
import i2f.natives.core.Ptr;

/**
 * @author Ice2Faith
 * @date 2024/5/10 8:32
 * @desc
 */
public class PaintStructPtr extends MallocPtr {
    public PaintStructPtr(long ptr) {
        super(ptr);
    }

    public PaintStructPtr(Ptr ptr) {
        super(ptr);
    }
}
