package i2f.natives.windows.types.window;

import i2f.natives.core.Ptr;
import i2f.natives.windows.types.Handle;

/**
 * @author Ice2Faith
 * @date 2024/5/10 8:32
 * @desc
 */
public class Hwnd extends Handle {
    public Hwnd(long ptr) {
        super(ptr);
    }

    public Hwnd(Ptr ptr) {
        super(ptr);
    }

    public static final Hwnd ZERO = new Hwnd(0);
}
