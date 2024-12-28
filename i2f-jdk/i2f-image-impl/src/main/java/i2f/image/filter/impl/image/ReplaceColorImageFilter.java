package i2f.image.filter.impl.image;

import i2f.color.Rgba;
import i2f.image.filter.impl.rgba.ReplaceColorRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 颜色替换
 */
public class ReplaceColorImageFilter extends RgbaFilterImageFilter {
    public ReplaceColorImageFilter(Rgba src, Rgba dst, double wrongRate) {
        super(new ReplaceColorRgbaFilter(src, dst, wrongRate));
    }
}
