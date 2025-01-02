package i2f.image.filter.impl.test;

import i2f.image.ImageUtil;
import i2f.image.filter.impl.image.ContainImageFilter;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Ice2Faith
 * @date 2024/12/28 14:13
 * @desc
 */
public class TestImage {
    public static void main(String[] args) throws Exception {
        BufferedImage img = ImageUtil.load(new File("./tmp.jpeg"));

        BufferedImage wh = new ContainImageFilter(720, 480).filter(img);
        ImageUtil.save(wh, new File("./w-h.png"));

        BufferedImage hw = new ContainImageFilter(480, 720).filter(img);
        ImageUtil.save(hw, new File("./h-w.png"));
    }
}
