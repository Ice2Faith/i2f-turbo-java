package i2f.image.filter.impl;

import i2f.color.Hsl;
import i2f.color.Rgba;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 增强亮度
 */
public class EnhanceLightnessImageFilter extends AbstractImageFilter {
    protected double rate = 0;

    public EnhanceLightnessImageFilter(double rate) {
        this.rate = rate;
    }

    @Override
    public Rgba pixel(Rgba color) {

        Hsl hsl = color.hsl();
        hsl.l = Hsl.stdHsl(hsl.l * rate);

        return hsl.rgba();
    }
}
