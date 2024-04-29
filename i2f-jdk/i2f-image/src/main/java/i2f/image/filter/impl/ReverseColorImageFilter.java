package i2f.image.filter.impl;

import i2f.color.Rgba;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 反色
 */
public class ReverseColorImageFilter extends AbstractImageFilter {

    @Override
    public Rgba pixel(Rgba color) {
        return Rgba.rgba(255 - color.r, 255 - color.g, 255 - color.b, 255 - color.a);
    }
}
