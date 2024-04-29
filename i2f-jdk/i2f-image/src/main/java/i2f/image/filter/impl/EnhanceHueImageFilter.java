package i2f.image.filter.impl;

import i2f.color.Hsl;
import i2f.color.Rgba;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 增强色相
 */
public class EnhanceHueImageFilter extends AbstractImageFilter {
    protected double rate = 0;

    public EnhanceHueImageFilter(double rate) {
        this.rate = rate;
    }

    @Override
    public Rgba pixel(Rgba color) {

        Hsl hsl = color.hsl();
        hsl.h = Hsl.stdHsl(hsl.h * rate);

        return hsl.rgba();
    }
}
