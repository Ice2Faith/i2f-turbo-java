package i2f.image.filter.impl.image;

import i2f.image.filter.impl.rgba.LighterDarkRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 提亮暗部
 */
public class LighterDarkImageFilter extends RgbaFilterImageFilter {

    public LighterDarkImageFilter(double rate) {
        super(new LighterDarkRgbaFilter(rate));
    }
}
