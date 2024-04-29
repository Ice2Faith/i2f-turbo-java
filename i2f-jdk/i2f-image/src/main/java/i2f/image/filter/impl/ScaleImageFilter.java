package i2f.image.filter.impl;

import java.awt.image.BufferedImage;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 缩放图片
 */
public class ScaleImageFilter extends ResizeImageFilter {
    protected double rate;

    public ScaleImageFilter(double rate) {
        super(0, 0);
        this.rate = rate;
    }

    @Override
    public BufferedImage filter(BufferedImage img) {
        wid = (int) (img.getWidth() * rate);
        hei = (int) (img.getHeight() * rate);
        return super.filter(img);
    }

}
