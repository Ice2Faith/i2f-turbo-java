package i2f.image.filter.impl.rgba;

import i2f.color.Rgba;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:02
 * @desc
 */
public class PinkTonesRgbaFilter extends ApproachColorRgbaFilter {

    public PinkTonesRgbaFilter(double rate) {
        super(Rgba.rgba(255, 0, 128, 255), rate);
    }

}
