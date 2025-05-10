package i2f.form.dialog.impl.media;

import i2f.form.dialog.std.IUriPreviewDialog;
import i2f.form.dialog.std.IUrlPreviewDialog;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/5/9 17:33
 */
public class MediaUriPreviewDialog implements IUriPreviewDialog {
    public static final MediaUriPreviewDialog INSTANCE = new MediaUriPreviewDialog();

    @Override
    public boolean support(Object obj) {
        URI uri = castAsUri(obj);
        if (uri == null) {
            return false;
        }
        String suffix = getUriFileSuffix(uri);
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
        URI uri = castAsUri(obj);
        MediaDialogs.previewMedia(uri);
    }
}
