package i2f.image.filter.std.impl;

import i2f.color.Rgba;
import i2f.image.filter.std.IImageFilter;
import i2f.image.filter.std.RgbaFilter;

import java.awt.image.BufferedImage;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 抽象单像素操作类
 */
public class RgbaFilterImageFilter implements IImageFilter {
    protected RgbaFilter filter;

    public RgbaFilterImageFilter(RgbaFilter filter) {
        this.filter = filter;
    }

    protected boolean returnNew() {
        return true;
    }

    @Override
    public BufferedImage filter(BufferedImage img) {
        BufferedImage simg = img;
        BufferedImage dimg = img;
        if (returnNew()) {
            dimg = new BufferedImage(simg.getWidth(), simg.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        }

        for (int x = 0; x < simg.getWidth(); x++) {
            for (int y = 0; y < simg.getHeight(); y++) {
                Rgba src = Rgba.argb(simg.getRGB(x, y));
                Rgba dst = filter.pixel(src);
                dimg.setRGB(x, y, dst.argb());
            }
        }

        return dimg;
    }

}
