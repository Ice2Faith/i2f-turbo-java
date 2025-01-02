package i2f.image.filter.impl.rgba;

import i2f.color.Rgba;
import i2f.image.filter.std.RgbaFilter;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:02
 * @desc
 */
public class ReplaceColorRgbaFilter implements RgbaFilter {
    protected Rgba src;
    protected Rgba dst;
    protected double wrongRate = -1;

    public ReplaceColorRgbaFilter(Rgba src, Rgba dst, double wrongRate) {
        this.src = src;
        this.dst = dst;
        this.wrongRate = wrongRate;
    }

    @Override
    public Rgba pixel(Rgba color) {
        boolean isTarget = false;
        if (wrongRate <= 0) {
            if (src.rgba() == color.rgba()) {
                isTarget = true;

            }
        } else {
            double rate = Rgba.diff(src, color);
            if (rate <= wrongRate) {
                isTarget = true;

            }
        }

        Rgba dc = color;
        if (isTarget) {
            dc = dst;
        }

        return dc;
    }
}
