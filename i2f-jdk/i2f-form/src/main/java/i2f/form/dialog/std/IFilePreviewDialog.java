package i2f.form.dialog.std;

import i2f.form.dialog.IPreviewDialog;

import java.io.File;

/**
 * @author Ice2Faith
 * @date 2025/5/9 14:05
 */
public interface IFilePreviewDialog extends IPreviewDialog {
    default File tryConvertAsFile(Object obj) {
        if (obj instanceof File) {
            return (File) obj;
        }
        if (obj instanceof CharSequence) {
            String str = (String) obj;
            File file = new File(str);
            if (file.isFile()) {
                return file;
            }
        }
        return null;
    }

    default String getFileSuffix(File file) {
        String name = file.getName();
        int idx = name.lastIndexOf(".");
        if (idx < 0) {
            return "";
        }
        String suffix = name.substring(idx).toLowerCase();
        return suffix;
    }

    @Override
    default boolean support(Object obj) {
        File file = tryConvertAsFile(obj);
        return file != null;
    }

}
