package i2f.form.dialog.impl.image;

import i2f.form.dialog.IPreviewDialog;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2025/5/10 13:04
 */
public class ImageInputStreamPreviewDialog implements IPreviewDialog {
    public static final ImageInputStreamPreviewDialog INSTANCE = new ImageInputStreamPreviewDialog();

    @Override
    public boolean support(Object obj) {
        return obj instanceof ImageInputStream;
    }

    @Override
    public void preview(Object obj) {
        try {
            ImageInputStream is = (ImageInputStream) obj;
            BufferedImage img = ImageIO.read(is);
            ImageDialogs.previewImage(img);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
