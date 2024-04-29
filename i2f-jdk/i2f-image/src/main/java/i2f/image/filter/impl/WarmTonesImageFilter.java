package i2f.image.filter.impl;

import i2f.color.Rgba;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 暖色调
 */
public class WarmTonesImageFilter extends ApproachColorImageFilter {
    public WarmTonesImageFilter(double rate) {
        super(Rgba.rgba(255, 128, 0, 255), rate);
    }
}
