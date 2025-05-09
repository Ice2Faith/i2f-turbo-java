package i2f.form.dialog.impl.web;

import i2f.form.dialog.IPreviewDialog;

import java.net.URL;

/**
 * @author Ice2Faith
 * @date 2025/5/9 18:01
 */
public class WebUrlPreviewDialog implements IPreviewDialog {
    public static final WebUrlPreviewDialog INSTANCE = new WebUrlPreviewDialog();

    @Override
    public boolean support(Object obj) {
        return obj instanceof URL;
    }

    @Override
    public void preview(Object obj) {
        URL url = (URL) obj;
        WebDialogs.previewWeb(url, null, null);
    }
}
