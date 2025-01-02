package i2f.image.filter.impl.image;

import i2f.image.filter.impl.rgba.GrayRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 灰度
 */
public class GrayImageFilter extends RgbaFilterImageFilter {

    public GrayImageFilter() {
        super(new GrayRgbaFilter());
    }
}
