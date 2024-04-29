package i2f.image.filter.impl;

import i2f.color.Hsl;
import i2f.color.Rgba;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 增强饱和度
 */
public class EnhanceSaturationImageFilter extends AbstractImageFilter {
    protected double rate = 0;

    public EnhanceSaturationImageFilter(double rate) {
        this.rate = rate;
    }

    @Override
    public Rgba pixel(Rgba color) {

        Hsl hsl = color.hsl();
        hsl.s = Hsl.stdHsl(hsl.s * rate);

        return hsl.rgba();
    }
}
