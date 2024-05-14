package i2f.natives.windows.types.gdi;

import i2f.natives.core.Ptr;
import i2f.natives.windows.types.Handle;

/**
 * @author Ice2Faith
 * @date 2024/5/10 8:32
 * @desc
 */
public class Hdc extends Handle {
    public Hdc(long ptr) {
        super(ptr);
    }

    public Hdc(Ptr ptr) {
        super(ptr);
    }
}
