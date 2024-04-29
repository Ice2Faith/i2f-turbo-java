package i2f.image.filter.impl;

import i2f.color.Rgba;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 冷色调
 */
public class CoolTonesImageFilter extends ApproachColorImageFilter {
    public CoolTonesImageFilter(double rate) {
        super(Rgba.rgba(0, 128, 255, 255), rate);
    }
}
