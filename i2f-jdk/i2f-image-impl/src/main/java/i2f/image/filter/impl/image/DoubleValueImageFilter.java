package i2f.image.filter.impl.image;

import i2f.image.filter.impl.rgba.DoubleValueRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 二值化
 */
public class DoubleValueImageFilter extends RgbaFilterImageFilter {

    public DoubleValueImageFilter() {
        super(new DoubleValueRgbaFilter());
    }
}
