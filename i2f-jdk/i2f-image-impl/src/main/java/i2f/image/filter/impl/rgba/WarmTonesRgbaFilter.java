package i2f.image.filter.impl.rgba;

import i2f.color.Rgba;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:02
 * @desc
 */
public class WarmTonesRgbaFilter extends ApproachColorRgbaFilter {

    public WarmTonesRgbaFilter(double rate) {
        super(Rgba.rgba(255, 128, 0, 255), rate);
    }

}
