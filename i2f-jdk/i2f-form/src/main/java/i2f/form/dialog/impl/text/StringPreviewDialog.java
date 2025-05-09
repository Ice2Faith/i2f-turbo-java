package i2f.form.dialog.impl.text;

import i2f.form.dialog.DialogBoxes;
import i2f.form.dialog.IPreviewDialog;

/**
 * @author Ice2Faith
 * @date 2025/5/9 17:56
 */
public class StringPreviewDialog implements IPreviewDialog {
    public static final StringPreviewDialog INSTANCE = new StringPreviewDialog();

    @Override
    public boolean support(Object obj) {
        return obj == null || obj instanceof CharSequence;
    }

    @Override
    public void preview(Object obj) {
        String str = String.valueOf(obj);
        DialogBoxes.message(str);
    }
}
