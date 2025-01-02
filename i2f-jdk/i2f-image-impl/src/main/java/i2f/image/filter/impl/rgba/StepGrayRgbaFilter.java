package i2f.image.filter.impl.rgba;

import i2f.color.Rgba;
import i2f.image.filter.std.RgbaFilter;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:02
 * @desc
 */
public class StepGrayRgbaFilter implements RgbaFilter {
    protected int steps = 2;

    public StepGrayRgbaFilter(int steps) {
        this.steps = steps;
    }

    @Override
    public Rgba pixel(Rgba color) {
        int gy = color.gray();
        // 计算每一阶多大
        double eve = 255.0 / steps;

        // 计算此像素使用了多少阶（四舍五入）
        double cnt = (int) (gy / eve + 0.5);

        // 计算这一阶对应的像素值
        gy = (int) (cnt * eve);

        return Rgba.rgba(gy, gy, gy, color.a);
    }
}
