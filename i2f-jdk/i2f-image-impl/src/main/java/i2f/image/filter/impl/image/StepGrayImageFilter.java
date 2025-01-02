package i2f.image.filter.impl.image;

import i2f.image.filter.impl.rgba.StepGrayRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 阶化灰度
 */
public class StepGrayImageFilter extends RgbaFilterImageFilter {
    public StepGrayImageFilter(int steps) {
        super(new StepGrayRgbaFilter(steps));
    }
}
