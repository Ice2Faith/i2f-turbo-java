package i2f.image.filter.impl.rgba;

import i2f.color.Hsl;
import i2f.color.Rgba;
import i2f.image.filter.std.RgbaFilter;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:02
 * @desc
 */
public class EnhanceHueRgbaFilter implements RgbaFilter {
    protected double rate = 0;

    public EnhanceHueRgbaFilter(double rate) {
        this.rate = rate;
    }

    @Override
    public Rgba pixel(Rgba color) {

        Hsl hsl = color.hsl();
        hsl.h = Hsl.stdHsl(hsl.h * rate);

        return hsl.rgba();
    }
}
