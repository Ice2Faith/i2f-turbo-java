package i2f.natives.windows.types.winapp;

import i2f.natives.core.MallocPtr;
import i2f.natives.core.Ptr;

/**
 * @author Ice2Faith
 * @date 2024/5/10 8:32
 * @desc
 */
public class Win32AppInstancePtr extends MallocPtr {
    public Win32AppInstancePtr(long ptr) {
        super(ptr);
    }

    public Win32AppInstancePtr(Ptr ptr) {
        super(ptr);
    }
}
