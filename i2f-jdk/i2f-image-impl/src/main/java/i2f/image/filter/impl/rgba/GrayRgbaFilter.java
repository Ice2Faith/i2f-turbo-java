package i2f.image.filter.impl.rgba;

import i2f.color.Rgba;
import i2f.image.filter.std.RgbaFilter;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:02
 * @desc
 */
public class GrayRgbaFilter implements RgbaFilter {
    @Override
    public Rgba pixel(Rgba color) {
        int gy = color.gray();
        return Rgba.rgba(gy, gy, gy, color.a);
    }
}
