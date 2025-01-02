package i2f.image.filter.impl.image;

import i2f.image.filter.impl.rgba.WarmTonesRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 暖色调
 */
public class WarmTonesImageFilter extends RgbaFilterImageFilter {
    public WarmTonesImageFilter(double rate) {
        super(new WarmTonesRgbaFilter(rate));
    }
}
