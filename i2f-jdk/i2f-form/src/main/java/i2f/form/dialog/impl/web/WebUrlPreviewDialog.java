package i2f.form.dialog.impl.web;

import i2f.form.dialog.std.IUrlPreviewDialog;

import java.net.URL;

/**
 * @author Ice2Faith
 * @date 2025/5/9 18:01
 */
public class WebUrlPreviewDialog implements IUrlPreviewDialog {
    public static final WebUrlPreviewDialog INSTANCE = new WebUrlPreviewDialog();


    @Override
    public void preview(Object obj) {
        URL url = castAsUrl(obj);
        WebDialogs.previewWeb(url, null, null);
    }
}
