package i2f.form.dialog.impl.media;

import i2f.form.dialog.std.IUrlPreviewDialog;

import java.net.URL;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/5/9 17:33
 */
public class MediaUrlPreviewDialog implements IUrlPreviewDialog {
    public static final MediaUrlPreviewDialog INSTANCE = new MediaUrlPreviewDialog();

    @Override
    public boolean support(Object obj) {
        URL url = castAsUrl(obj);
        if (url == null) {
            return false;
        }
        String suffix = getUrlFileSuffix(url);
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
        URL url = castAsUrl(obj);
        MediaDialogs.previewMedia(url);
    }
}
