package i2f.image.filter.impl.test;

import i2f.image.ImageUtil;
import i2f.image.filter.impl.image.ContainImageFilter;
import i2f.image.filter.impl.rgba.ChannelKeepRgbaFilter;
import i2f.image.filter.impl.rgba.LighterDarkRgbaFilter;
import i2f.image.filter.std.impl.MergeRgbaFilter;
import i2f.image.filter.std.impl.RgbaFilterImageFilter;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Ice2Faith
 * @date 2024/12/28 14:13
 * @desc
 */
public class TestImage {
    public static void main(String[] args) throws Exception {
        BufferedImage img = ImageUtil.load(new File("./test1.jpg"));

        BufferedImage kp = new RgbaFilterImageFilter(new MergeRgbaFilter(
                new LighterDarkRgbaFilter(0.85),
                new ChannelKeepRgbaFilter()
        )).filter(img);
        ImageUtil.save(kp, new File("./kp.png"));

        BufferedImage wh = new ContainImageFilter(720, 480).filter(img);
        ImageUtil.save(wh, new File("./w-h.png"));

        BufferedImage hw = new ContainImageFilter(480, 720).filter(img);
        ImageUtil.save(hw, new File("./h-w.png"));
    }
}
