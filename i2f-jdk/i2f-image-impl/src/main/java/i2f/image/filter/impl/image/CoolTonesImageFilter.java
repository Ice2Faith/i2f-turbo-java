package i2f.image.filter.impl.image;

import i2f.image.filter.impl.rgba.CoolTonesRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 冷色调
 */
public class CoolTonesImageFilter extends RgbaFilterImageFilter {
    public CoolTonesImageFilter(double rate) {
        super(new CoolTonesRgbaFilter(rate));
    }
}
