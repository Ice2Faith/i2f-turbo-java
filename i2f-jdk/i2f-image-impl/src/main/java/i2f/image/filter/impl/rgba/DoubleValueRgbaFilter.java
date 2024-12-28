package i2f.image.filter.impl.rgba;

import i2f.color.Rgba;
import i2f.image.filter.std.RgbaFilter;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:02
 * @desc
 */
public class DoubleValueRgbaFilter implements RgbaFilter {
    public static final int half = 255 / 2;

    @Override
    public Rgba pixel(Rgba color) {
        int gy = color.gray();
        if (gy < half) {
            gy = 0;
        } else {
            gy = 255;
        }
        return Rgba.rgba(gy, gy, gy, color.a);
    }
}
