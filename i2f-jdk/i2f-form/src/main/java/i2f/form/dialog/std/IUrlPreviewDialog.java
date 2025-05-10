package i2f.form.dialog.std;

import i2f.form.dialog.IPreviewDialog;

import java.net.URL;

/**
 * @author Ice2Faith
 * @date 2025/5/10 13:07
 */
public interface IUrlPreviewDialog extends IPreviewDialog {
    default URL castAsUrl(Object obj) {
        if (obj instanceof URL) {
            return (URL) obj;
        }
        try {
            URL url = new URL(String.valueOf(obj));
            if (url != null) {
                return url;
            }
        } catch (Exception e) {

        }
        return null;
    }

    default String getUrlFileSuffix(URL url) {
        String path = url.getPath();
        int idx = path.lastIndexOf(".");
        if (idx < 0) {
            return "";
        }
        String suffix = path.substring(idx).toLowerCase();
        return suffix;
    }

    @Override
    default boolean support(Object obj) {
        URL url = castAsUrl(obj);
        return url != null;
    }
}
