package i2f.image.filter.impl.image;

import i2f.image.filter.impl.rgba.EnhanceHueRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 增强色相
 */
public class EnhanceHueImageFilter extends RgbaFilterImageFilter {

    public EnhanceHueImageFilter(double rate) {
        super(new EnhanceHueRgbaFilter(rate));
    }
}
