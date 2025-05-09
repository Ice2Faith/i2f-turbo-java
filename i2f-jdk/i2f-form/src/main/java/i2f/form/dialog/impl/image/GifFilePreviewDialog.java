package i2f.form.dialog.impl.image;

import i2f.form.dialog.IFilePreviewDialog;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/5/9 17:33
 */
public class GifFilePreviewDialog implements IFilePreviewDialog {
    public static final GifFilePreviewDialog INSTANCE = new GifFilePreviewDialog();

    @Override
    public boolean support(Object obj) {
        File file = tryConvertAsFile(obj);
        if (file == null) {
            return false;
        }
        String name = file.getName();
        int idx = name.lastIndexOf(".");
        if (idx < 0) {
            return false;
        }
        String suffix = name.substring(idx).toLowerCase();
        if (Arrays.asList(
                ".gif"
        ).contains(suffix)) {
            return true;
        }
        return false;
    }

    @Override
    public void preview(Object obj) {
        File file = tryConvertAsFile(obj);
        try {
            IconDialogs.previewGif(file, null, null);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
