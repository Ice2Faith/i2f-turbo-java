package i2f.natives.windows.types.winapp;

import i2f.natives.core.MallocPtr;
import i2f.natives.core.Ptr;

/**
 * @author Ice2Faith
 * @date 2024/5/10 8:32
 * @desc
 */
public class BitmapDcPtr extends MallocPtr {
    public BitmapDcPtr(long ptr) {
        super(ptr);
    }

    public BitmapDcPtr(Ptr ptr) {
        super(ptr);
    }
}
