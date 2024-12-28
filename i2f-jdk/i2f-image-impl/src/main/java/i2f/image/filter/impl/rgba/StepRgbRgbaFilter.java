package i2f.image.filter.impl.rgba;

import i2f.color.Rgba;
import i2f.image.filter.std.RgbaFilter;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:02
 * @desc
 */
public class StepRgbRgbaFilter implements RgbaFilter {
    protected int steps = 2;

    public StepRgbRgbaFilter(int steps) {
        this.steps = steps;
    }

    @Override
    public Rgba pixel(Rgba color) {
        int r = color.r;
        int g = color.g;
        int b = color.b;
        // 计算每一阶多大
        double eve = 255.0 / steps;

        // 计算此像素使用了多少阶（四舍五入）
        double rcnt = (int) (r / eve + 0.5);
        double gcnt = (int) (g / eve + 0.5);
        double bcnt = (int) (b / eve + 0.5);

        // 计算这一阶对应的像素值
        r = (int) (rcnt * eve);
        g = (int) (gcnt * eve);
        b = (int) (bcnt * eve);

        return Rgba.rgba(r, g, b, color.a);
    }
}
