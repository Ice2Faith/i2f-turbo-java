package i2f.image.filter.impl.image;

import i2f.image.filter.impl.rgba.EnhanceSaturationRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 增强饱和度
 */
public class EnhanceSaturationImageFilter extends RgbaFilterImageFilter {

    public EnhanceSaturationImageFilter(double rate) {
        super(new EnhanceSaturationRgbaFilter(rate));
    }
}
