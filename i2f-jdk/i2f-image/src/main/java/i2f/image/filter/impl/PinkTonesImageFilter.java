package i2f.image.filter.impl;

import i2f.color.Rgba;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 桃红色调
 */
public class PinkTonesImageFilter extends ApproachColorImageFilter {
    public PinkTonesImageFilter(double rate) {
        super(Rgba.rgba(255, 0, 128, 255), rate);
    }
}
