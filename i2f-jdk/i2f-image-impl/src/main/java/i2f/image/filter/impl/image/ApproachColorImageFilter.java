package i2f.image.filter.impl.image;

import i2f.color.Rgba;
import i2f.image.filter.impl.rgba.ApproachColorRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 颜色趋近，适用于实现冷暖色调
 */
public class ApproachColorImageFilter extends RgbaFilterImageFilter {

    public ApproachColorImageFilter(Rgba target, double rate) {
        super(new ApproachColorRgbaFilter(target, rate));
    }

}
