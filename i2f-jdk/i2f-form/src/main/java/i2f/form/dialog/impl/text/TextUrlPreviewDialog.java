package i2f.form.dialog.impl.text;

import i2f.form.dialog.DialogBoxes;
import i2f.form.dialog.std.IUrlPreviewDialog;
import i2f.io.stream.StreamUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/5/9 17:33
 */
public class TextUrlPreviewDialog implements IUrlPreviewDialog {
    public static final TextUrlPreviewDialog INSTANCE = new TextUrlPreviewDialog();

    @Override
    public boolean support(Object obj) {
        URL url = castAsUrl(obj);
        if (url == null) {
            return false;
        }
        String suffix = getUrlFileSuffix(url);
        if (Arrays.asList(
                ".txt", ".text", ".html", ".md",
                ".log",
                ".html", "htm", ".css", ".js", ".sass", ".less", ".ts",
                ".json", ".xml", ".yml", ".yaml",
                ".properties", ".conf",
                ".sql", ".java", ".py", ".c", ".cpp", ".h", ".hpp"
        ).contains(suffix)) {
            return true;
        }
        return false;
    }

    @Override
    public void preview(Object obj) {
        URL url = castAsUrl(obj);
        try {
            String str = StreamUtil.readString(url);
            DialogBoxes.message(str);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
