package i2f.form.dialog.impl.image;

import i2f.form.dialog.IPreviewDialog;

import javax.swing.*;

/**
 * @author Ice2Faith
 * @date 2025/5/10 12:57
 */
public class ImageIconPreviewDialog implements IPreviewDialog {
    public static final ImageIconPreviewDialog INSTANCE=new ImageIconPreviewDialog();
    @Override
    public boolean support(Object obj) {
        return obj instanceof ImageIcon;
    }

    @Override
    public void preview(Object obj) {
        ImageIcon icon = (ImageIcon) obj;
        IconDialogs.previewIcon(icon);
    }
}
