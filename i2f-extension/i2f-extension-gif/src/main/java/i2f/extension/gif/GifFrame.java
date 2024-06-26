package i2f.extension.gif;

import java.awt.image.BufferedImage;

/**
 * @author Ice2Faith
 * @date 2022/6/24 17:34
 * @desc
 */
public class GifFrame {
    public int delayMs = 300;
    public BufferedImage image;

    public GifFrame(BufferedImage image) {
        this.image = image;
    }

    public GifFrame(int delayMs, BufferedImage image) {
        this.delayMs = delayMs;
        this.image = image;
    }
}
