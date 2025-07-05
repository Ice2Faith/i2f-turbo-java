package i2f.form.dialog;

import i2f.form.dialog.impl.image.*;
import i2f.form.dialog.impl.media.MediaFilePreviewDialog;
import i2f.form.dialog.impl.media.MediaPreviewDialog;
import i2f.form.dialog.impl.media.MediaUriPreviewDialog;
import i2f.form.dialog.impl.media.MediaUrlPreviewDialog;
import i2f.form.dialog.impl.text.StringPreviewDialog;
import i2f.form.dialog.impl.text.TextFilePreviewDialog;
import i2f.form.dialog.impl.text.TextUrlPreviewDialog;
import i2f.form.dialog.impl.web.WebUrlPreviewDialog;

import java.util.ServiceLoader;

/**
 * @author Ice2Faith
 * @date 2025/5/9 17:53
 */
public class PreviewDialogs {
    public static final IPreviewDialog[] DEFAULTS = {
            BufferedImagePreviewDialog.INSTANCE,
            ImageIconPreviewDialog.INSTANCE,
            ImageInputStreamPreviewDialog.INSTANCE,
            MediaPreviewDialog.INSTANCE,

            GifFilePreviewDialog.INSTANCE,
            ImageFilePreviewDialog.INSTANCE,
            MediaFilePreviewDialog.INSTANCE,
            TextFilePreviewDialog.INSTANCE,

            ImageIconUrlPreviewDialog.INSTANCE,
            ImageUrlPreviewDialog.INSTANCE,
            MediaUrlPreviewDialog.INSTANCE,
            TextUrlPreviewDialog.INSTANCE,

            MediaUriPreviewDialog.INSTANCE,

            WebUrlPreviewDialog.INSTANCE,
            StringPreviewDialog.INSTANCE,

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
