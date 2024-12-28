package i2f.image.filter.impl.image;

import i2f.image.filter.impl.rgba.EnhanceLightnessRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 增强亮度
 */
public class EnhanceLightnessImageFilter extends RgbaFilterImageFilter {

    public EnhanceLightnessImageFilter(double rate) {
        super(new EnhanceLightnessRgbaFilter(rate));
    }
}
