package i2f.form.dialog.impl.image;

import i2f.form.dialog.IPreviewDialog;

import java.awt.image.BufferedImage;

/**
 * @author Ice2Faith
 * @date 2025/5/10 12:52
 */
public class BufferedImagePreviewDialog implements IPreviewDialog {
    public static final BufferedImagePreviewDialog INSTANCE=new BufferedImagePreviewDialog();
    @Override
    public boolean support(Object obj) {
        return obj instanceof BufferedImage;
    }

    @Override
    public void preview(Object obj) {
        BufferedImage img = (BufferedImage) obj;
        ImageDialogs.previewImage(img);
    }
}
