package i2f.natives.windows.types.window;

import i2f.natives.core.Ptr;
import i2f.natives.windows.types.Handle;

/**
 * @author Ice2Faith
 * @date 2024/5/10 8:32
 * @desc
 */
public class HCursor extends Handle {
    public HCursor(long ptr) {
        super(ptr);
    }

    public HCursor(Ptr ptr) {
        super(ptr);
    }
}
