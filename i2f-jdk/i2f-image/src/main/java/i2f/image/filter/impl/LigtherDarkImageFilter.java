package i2f.image.filter.impl;

import i2f.color.Hsl;
import i2f.color.Rgba;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 提亮暗部
 */
public class LigtherDarkImageFilter extends AbstractImageFilter {
    protected double rate = 0;

    public LigtherDarkImageFilter(double rate) {
        this.rate = rate;
    }

    @Override
    public Rgba pixel(Rgba color) {

        Hsl hsl = color.hsl();
        hsl.l = Hsl.stdHsl(hsl.l * (1.0 + (rate * (1.0 - hsl.l))));

        return hsl.rgba();
    }
}
