package i2f.image.filter.impl.image;

import i2f.image.filter.impl.rgba.TeaTonesRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 茶色调
 */
public class TeaTonesImageFilter extends RgbaFilterImageFilter {
    public TeaTonesImageFilter(double rate) {
        super(new TeaTonesRgbaFilter(rate));
    }
}
