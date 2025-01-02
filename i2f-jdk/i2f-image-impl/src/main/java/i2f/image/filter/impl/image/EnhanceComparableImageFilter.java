package i2f.image.filter.impl.image;

import i2f.image.filter.impl.rgba.EnhanceComparableRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 增强对比度
 */
public class EnhanceComparableImageFilter extends RgbaFilterImageFilter {

    public EnhanceComparableImageFilter(double rate) {
        super(new EnhanceComparableRgbaFilter(rate));
    }
}
