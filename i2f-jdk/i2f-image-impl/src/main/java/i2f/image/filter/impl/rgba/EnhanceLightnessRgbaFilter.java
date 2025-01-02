package i2f.image.filter.impl.rgba;

import i2f.color.Hsl;
import i2f.color.Rgba;
import i2f.image.filter.std.RgbaFilter;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:02
 * @desc
 */
public class EnhanceLightnessRgbaFilter implements RgbaFilter {
    protected double rate = 0;

    public EnhanceLightnessRgbaFilter(double rate) {
        this.rate = rate;
    }

    @Override
    public Rgba pixel(Rgba color) {

        Hsl hsl = color.hsl();
        hsl.l = Hsl.stdHsl(hsl.l * rate);

        return hsl.rgba();
    }
}
