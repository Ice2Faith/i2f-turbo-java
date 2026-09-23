package i2f.form.dialog.preview.impl.image;

import i2f.form.dialog.preview.std.IFilePreviewDialog;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/5/9 17:33
 */
public class ImageFilePreviewDialog implements IFilePreviewDialog {
    public static final ImageFilePreviewDialog INSTANCE = new ImageFilePreviewDialog();

    @Override
    public boolean support(Object obj) {
        File file = tryConvertAsFile(obj);
        if (file == null) {
            return false;
        }
        String suffix = getFileSuffix(file);
        if (Arrays.asList(
                ".jpg", ".png", ".jpeg", ".webp", ".bmp", ".tiff"
        ).contains(suffix)) {
            return true;
        }
        return false;
    }

    @Override
    public void preview(Object obj) {
        File file = tryConvertAsFile(obj);
        try {
            BufferedImage img = ImageIO.read(file);
            ImageDialogs.previewImage(img);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
