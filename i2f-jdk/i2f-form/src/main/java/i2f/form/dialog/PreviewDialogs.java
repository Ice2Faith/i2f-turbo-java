package i2f.form.dialog;

import i2f.form.dialog.impl.image.GifFilePreviewDialog;
import i2f.form.dialog.impl.image.ImageFilePreviewDialog;
import i2f.form.dialog.impl.media.MediaFilePreviewDialog;
import i2f.form.dialog.impl.text.StringPreviewDialog;
import i2f.form.dialog.impl.web.WebUrlPreviewDialog;

import java.util.ServiceLoader;

/**
 * @author Ice2Faith
 * @date 2025/5/9 17:53
 */
public class PreviewDialogs {
    public static final IPreviewDialog[] DEFAULTS = {
            ImageFilePreviewDialog.INSTANCE,
            GifFilePreviewDialog.INSTANCE,
            MediaFilePreviewDialog.INSTANCE,
            StringPreviewDialog.INSTANCE,
            WebUrlPreviewDialog.INSTANCE
    };

    public static void preview(Object obj) {
        ServiceLoader<IPreviewDialog> list = ServiceLoader.load(IPreviewDialog.class);
        IPreviewDialog dialog = null;
        for (IPreviewDialog item : list) {
            if (item.support(obj)) {
                dialog = item;
                break;
            }
        }
        if (dialog == null) {

        }
        if (dialog == null) {
            // preview as string
            obj = String.valueOf(obj);
            dialog = StringPreviewDialog.INSTANCE;
        }
        dialog.preview(obj);
    }
}
