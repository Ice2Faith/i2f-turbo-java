package i2f.image.filter.impl.image;

import i2f.image.filter.impl.rgba.StepRgbRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 阶化RGB
 */
public class StepRgbImageFilter extends RgbaFilterImageFilter {
    public StepRgbImageFilter(int steps) {
        super(new StepRgbRgbaFilter(steps));
    }
}
