package i2f.natives.windows.types.gdi;

import i2f.natives.core.Ptr;

/**
 * @author Ice2Faith
 * @date 2024/5/10 8:32
 * @desc
 */
public class HBitmap extends HGdiObj {
    public HBitmap(long ptr) {
        super(ptr);
    }

    public HBitmap(Ptr ptr) {
        super(ptr);
    }
}
