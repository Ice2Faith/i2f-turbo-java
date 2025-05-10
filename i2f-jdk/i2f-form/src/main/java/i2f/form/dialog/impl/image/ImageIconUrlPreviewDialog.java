package i2f.form.dialog.impl.image;

import i2f.form.dialog.std.IUrlPreviewDialog;

import javax.swing.*;
import java.net.URL;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/5/10 13:14
 */
public class ImageIconUrlPreviewDialog implements IUrlPreviewDialog {
    public static final ImageIconUrlPreviewDialog INSTANCE = new ImageIconUrlPreviewDialog();

    @Override
    public boolean support(Object obj) {
        URL url = castAsUrl(obj);
        if (url == null) {
            return false;
        }
        String suffix = getUrlFileSuffix(url);
        if (Arrays.asList(
                ".gif"
        ).contains(suffix)) {
            return true;
        }
        return false;
    }

    @Override
    public void preview(Object obj) {
        try {
            URL url = castAsUrl(obj);
            ImageIcon icon = new ImageIcon(url);
            IconDialogs.previewIcon(icon);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


}
