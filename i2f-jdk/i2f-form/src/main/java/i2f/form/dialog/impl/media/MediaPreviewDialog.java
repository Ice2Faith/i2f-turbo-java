package i2f.form.dialog.impl.media;

import i2f.form.dialog.IPreviewDialog;
import javafx.scene.media.Media;

/**
 * @author Ice2Faith
 * @date 2025/5/10 13:01
 */
public class MediaPreviewDialog implements IPreviewDialog {
    public static final MediaPreviewDialog INSTANCE = new MediaPreviewDialog();

    @Override
    public boolean support(Object obj) {
        return obj instanceof Media;
    }

    @Override
    public void preview(Object obj) {
        Media media = (Media) obj;
        MediaDialogs.previewMedia(media);
    }
}
