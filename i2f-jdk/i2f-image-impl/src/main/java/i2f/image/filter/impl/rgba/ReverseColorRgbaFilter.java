package i2f.image.filter.impl.rgba;

import i2f.color.Rgba;
import i2f.image.filter.std.RgbaFilter;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:02
 * @desc
 */
public class ReverseColorRgbaFilter implements RgbaFilter {
    @Override
    public Rgba pixel(Rgba color) {
        return Rgba.rgba(255 - color.r, 255 - color.g, 255 - color.b, 255 - color.a);
    }

}
