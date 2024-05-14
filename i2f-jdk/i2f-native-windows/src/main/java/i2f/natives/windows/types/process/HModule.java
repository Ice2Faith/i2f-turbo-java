package i2f.natives.windows.types.process;

import i2f.natives.core.Ptr;
import i2f.natives.windows.types.Handle;

/**
 * @author Ice2Faith
 * @date 2024/5/10 8:32
 * @desc
 */
public class HModule extends Handle {
    public HModule(long ptr) {
        super(ptr);
    }

    public HModule(Ptr ptr) {
        super(ptr);
    }
}
