package i2f.image.filter.std.impl;

import i2f.color.Rgba;
import i2f.image.filter.std.PixelFilter;
import i2f.image.filter.std.RgbaFilter;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:17
 * @desc
 */
public class PixelFilterRgbaFilter implements RgbaFilter {
    protected PixelFilter filter;

    public PixelFilterRgbaFilter(PixelFilter filter) {
        this.filter = filter;
    }

    @Override
    public Rgba pixel(Rgba color) {
        int r = color.argb();
        r = filter.pixel(r);
        return Rgba.argb(r);
    }
}
