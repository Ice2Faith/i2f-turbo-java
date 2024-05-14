package i2f.natives.windows.types.com;

import i2f.natives.core.Ptr;

/**
 * @author Ice2Faith
 * @date 2024/5/10 8:32
 * @desc
 */
public class CoIUnknownPtr extends Ptr {
    public CoIUnknownPtr(long ptr) {
        super(ptr);
    }

    public CoIUnknownPtr(Ptr ptr) {
        super(ptr);
    }
}
