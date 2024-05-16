package i2f.natives.windows.types.window;

import i2f.natives.windows.types.gdi.HBitmap;
import i2f.natives.windows.types.gdi.Hdc;
import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/16 22:33
 * @desc
 */
@Data
public class BitmapDcInfo {
    public Hdc hdc;
    public HBitmap hBitmap;
    public int width;
    public int height;
}
