package i2f.image.filter.impl.image;

import java.awt.image.BufferedImage;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 按比例缩放到指定的尺寸图片
 */
public class ContainImageFilter extends ResizeImageFilter {
    protected int maxWidth;
    protected int maxHeight;

    public ContainImageFilter(int maxWidth, int maxHeight) {
        super(0, 0);
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    @Override
    public BufferedImage filter(BufferedImage img) {
        double rateWidth = maxWidth * 1.0 / img.getWidth();
        double rateHeight = maxHeight * 1.0 / img.getHeight();

        double rate = rateWidth;

        double targetHeight = img.getHeight() / rateWidth;
        if (targetHeight <= maxHeight) {
            rate = rateWidth;
        } else {
            rate = rateHeight;
        }

        wid = (int) (img.getWidth() * rate);
        hei = (int) (img.getHeight() * rate);
        return super.filter(img);
    }

}
