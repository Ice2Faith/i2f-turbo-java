package i2f.image.filter.impl.image;

import i2f.image.filter.impl.rgba.ReverseColorRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 反色
 */
public class ReverseColorImageFilter extends RgbaFilterImageFilter {

    public ReverseColorImageFilter() {
        super(new ReverseColorRgbaFilter());
    }
}
