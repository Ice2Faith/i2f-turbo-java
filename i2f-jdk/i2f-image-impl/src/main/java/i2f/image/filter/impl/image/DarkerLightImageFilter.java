package i2f.image.filter.impl.image;

import i2f.image.filter.impl.rgba.DarkerLightRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 降暗亮部
 */
public class DarkerLightImageFilter extends RgbaFilterImageFilter {

    public DarkerLightImageFilter(double rate) {
        super(new DarkerLightRgbaFilter(rate));
    }
}
