package i2f.form.dialog.impl.text;

import i2f.form.dialog.DialogBoxes;
import i2f.form.dialog.impl.image.ImageDialogs;
import i2f.form.dialog.std.IFilePreviewDialog;
import i2f.io.stream.StreamUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/5/9 17:33
 */
public class TextFilePreviewDialog implements IFilePreviewDialog {
    public static final TextFilePreviewDialog INSTANCE = new TextFilePreviewDialog();

    @Override
    public boolean support(Object obj) {
        File file = tryConvertAsFile(obj);
        if (file == null) {
            return false;
        }
        String suffix = getFileSuffix(file);
        if (Arrays.asList(
                ".txt", ".text", ".html", ".md",
                ".log",
                ".html","htm",".css",".js",".sass",".less",".ts",
                ".json",".xml",".yml",".yaml",
                ".properties",".conf",
                ".sql",".java",".py",".c",".cpp",".h",".hpp"
        ).contains(suffix)) {
            return true;
        }
        return false;
    }

    @Override
    public void preview(Object obj) {
        File file = tryConvertAsFile(obj);
        try {
            String str = StreamUtil.readString(file);
            DialogBoxes.message(str);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
