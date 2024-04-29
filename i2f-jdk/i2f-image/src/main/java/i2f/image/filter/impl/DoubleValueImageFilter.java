package i2f.image.filter.impl;

import i2f.color.Rgba;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 二值化
 */
public class DoubleValueImageFilter extends AbstractImageFilter {
    public static final int half = 255 / 2;

    @Override
    public Rgba pixel(Rgba color) {
        int gy = color.gray();
        if (gy < half) {
            gy = 0;
        } else {
            gy = 255;
        }
        return Rgba.rgba(gy, gy, gy, color.a);
    }
}
