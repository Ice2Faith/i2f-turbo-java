package i2f.image.filter.impl;

import i2f.color.Rgba;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 灰度
 */
public class GrayImageFilter extends AbstractImageFilter {
    @Override
    public Rgba pixel(Rgba color) {
        int gy = color.gray();
        return Rgba.rgba(gy, gy, gy, color.a);
    }
}
