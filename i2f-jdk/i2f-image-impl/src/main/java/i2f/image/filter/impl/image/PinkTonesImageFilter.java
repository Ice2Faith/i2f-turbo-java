package i2f.image.filter.impl.image;

import i2f.image.filter.impl.rgba.PinkTonesRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 桃红色调
 */
public class PinkTonesImageFilter extends RgbaFilterImageFilter {
    public PinkTonesImageFilter(double rate) {
        super(new PinkTonesRgbaFilter(rate));
    }
}
