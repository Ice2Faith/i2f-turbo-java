package i2f.image.filter.std.impl;

import i2f.color.Rgba;
import i2f.image.filter.std.PixelFilter;
import i2f.image.filter.std.RgbaFilter;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:17
 * @desc
 */
public class RgbaFilterPixelFilter implements PixelFilter {
    protected RgbaFilter filter;

    public RgbaFilterPixelFilter(RgbaFilter filter) {
        this.filter = filter;
    }

    @Override
    public int pixel(int color) {
        Rgba r = Rgba.argb(color);
        r = filter.pixel(r);
        return r.argb();
    }
}
