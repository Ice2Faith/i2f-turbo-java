package i2f.form.dialog.input;

import i2f.form.dialog.common.DialogStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模态输入对话框的返回结果
 *
 * <p>通过 {@link InputDialog#input(String, String, String)} 返回，
 * 可借助 {@link #isConfirm()}、{@link #isCancel()} 判断结果类型。</p>
 *
 * <p>取消时（关窗、Esc、点击取消按钮）content 为 null。</p>
 */
@Data
@NoArgsConstructor
public class InputResult {


    /**
     * 结果类型，默认 {@link DialogStatus#CANCEL}
     */
    private DialogStatus result = DialogStatus.CANCEL;

    /**
     * 确认时为用户输入的内容；取消时为 null
     */
    private String content;

    /**
     * 创建确认结果
     *
     * @param content 用户输入内容
     */
    public static InputResult ofConfirm(String content) {
        InputResult option = new InputResult();
        option.result = DialogStatus.CONFIRM;
        option.content = content;
        return option;
    }

    /**
     * 创建取消结果（content 为 null）
     */
    public static InputResult ofCancel() {
        return new InputResult();
    }

    /**
     * 是否确认
     */
    public boolean isConfirm() {
        return result == DialogStatus.CONFIRM;
    }

    /**
     * 是否取消（未输入任何内容）
     */
    public boolean isCancel() {
        return result == null || result == DialogStatus.CANCEL;
    }

    public boolean isEmpty() {
        return this.content == null || this.content.isEmpty();
    }

    public int getInteger() {
        return Integer.parseInt(content.trim());
    }

    public long getLong() {
        return Long.parseLong(content.trim());
    }

    public float getFloat() {
        return Float.parseFloat(content.trim());
    }

    public double getDouble() {
        return Double.parseDouble(content.trim());
    }

}