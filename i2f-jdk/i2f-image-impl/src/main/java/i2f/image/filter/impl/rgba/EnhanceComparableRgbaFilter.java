package i2f.image.filter.impl.rgba;

import i2f.color.Rgba;
import i2f.image.filter.std.RgbaFilter;
import i2f.math.MathUtil;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:02
 * @desc
 */
public class EnhanceComparableRgbaFilter implements RgbaFilter {
    protected int half = 255 / 2;
    protected double rate = 0;

    public EnhanceComparableRgbaFilter(double rate) {
        this.rate = rate;
    }

    @Override
    public Rgba pixel(Rgba color) {
        double upRate = 1.0 + rate;
        double downRate = 1.0 - rate;

        int r = color.r;
        int g = color.g;
        int b = color.b;
        int a = color.a;

        r = (int) MathUtil.between((r > half) ? (r * upRate) : (r * downRate), 0, 255);
        g = (int) MathUtil.between((g > half) ? (g * upRate) : (g * downRate), 0, 255);
        b = (int) MathUtil.between((b > half) ? (b * upRate) : (b * downRate), 0, 255);
        a = (int) MathUtil.between((a > half) ? (a * upRate) : (a * downRate), 0, 255);

        return Rgba.rgba(r, g, b, a);
    }
}
