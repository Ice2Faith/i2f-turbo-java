package i2f.image.filter.impl.rgba;

import i2f.color.Rgba;
import i2f.image.filter.std.RgbaFilter;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:02
 * @desc
 */
public class ApproachColorRgbaFilter implements RgbaFilter {
    protected Rgba target;
    protected double rate = 0;

    public ApproachColorRgbaFilter(Rgba target, double rate) {
        this.target = target;
        this.rate = rate;
    }

    @Override
    public Rgba pixel(Rgba color) {
        double srate = 1.0 - rate;
        double nrate = rate;

        int a = (int) (color.a * srate + target.a * nrate);
        int r = (int) (color.r * srate + target.r * nrate);
        int g = (int) (color.g * srate + target.g * nrate);
        int b = (int) (color.b * srate + target.b * nrate);

        return Rgba.rgba(r, g, b, a);
    }
}
