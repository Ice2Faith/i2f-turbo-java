package i2f.natives.windows.types;

import i2f.natives.core.Ptr;

/**
 * @author Ice2Faith
 * @date 2024/5/10 8:32
 * @desc
 */
public class ScHandle extends Ptr {
    public ScHandle(long ptr) {
        super(ptr);
    }

    public ScHandle(Ptr ptr) {
        super(ptr);
    }
}
