package i2f.form.dialog.preview.impl.media;

import i2f.form.dialog.preview.std.IFilePreviewDialog;

import java.io.File;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/5/9 17:33
 */
public class MediaFilePreviewDialog implements IFilePreviewDialog {
    public static final MediaFilePreviewDialog INSTANCE = new MediaFilePreviewDialog();

    @Override
    public boolean support(Object obj) {
        File file = tryConvertAsFile(obj);
        if (file == null) {
            return false;
        }
        String suffix = getFileSuffix(file);
        if (Arrays.asList(
                ".mp3", ".aac", ".ogg",
                ".mp4", ".avi", ".wmv", ".mpeg", ".flv", ".mkv", ".rmvb"
        ).contains(suffix)) {
            return true;
        }
        return false;
    }

    @Override
    public void preview(Object obj) {
        File file = tryConvertAsFile(obj);
        MediaDialogs.previewMedia(file);
    }
}
