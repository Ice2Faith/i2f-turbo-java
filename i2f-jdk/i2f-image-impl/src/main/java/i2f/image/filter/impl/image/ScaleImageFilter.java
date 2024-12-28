package i2f.image.filter.impl.image;

import java.awt.image.BufferedImage;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 缩放图片
 */
public class ScaleImageFilter extends ResizeImageFilter {
    protected double rateWid;
    protected double rateHei;

    public ScaleImageFilter(double rate) {
        this(rate, rate);
    }

    public ScaleImageFilter(double rateWid, double rateHei) {
        super(0, 0);
        this.rateWid = rateWid;
        this.rateHei = rateHei;
    }

    @Override
    public BufferedImage filter(BufferedImage img) {
        wid = (int) (img.getWidth() * rateWid);
        hei = (int) (img.getHeight() * rateWid);
        return super.filter(img);
    }

}
