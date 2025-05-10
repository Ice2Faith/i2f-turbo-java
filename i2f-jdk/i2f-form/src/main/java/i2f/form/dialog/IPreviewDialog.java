package i2f.form.dialog;

/**
 * @author Ice2Faith
 * @date 2025/5/9 14:04
 */
public interface IPreviewDialog {

    boolean support(Object obj);

    void preview(Object obj);
}
