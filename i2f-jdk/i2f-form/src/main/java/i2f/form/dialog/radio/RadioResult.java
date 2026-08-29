package i2f.form.dialog.radio;

import i2f.form.dialog.common.DialogStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 模态单选框窗口的返回结果
 *
 * <p>通过 {@link RadioDialog#radio(String, List, boolean, String)} 返回，
 * 可借助 {@link #isCancel()}、{@link #isChoice()}、{@link #isCustom()} 判断结果类型。</p>
 */
@Data
@NoArgsConstructor
public class RadioResult {

    /**
     * 未选择的下标值
     */
    public static final int INDEX_UNSELECTED = -2;
    /**
     * 自定义输入的下标值
     */
    public static final int INDEX_CUSTOM = -1;

    /**
     * 对应输入的 options 的下标；{@link #INDEX_UNSELECTED} 表示未选择，
     * {@link #INDEX_CUSTOM} 表示自定义输入内容
     */
    private int index = INDEX_UNSELECTED;

    /**
     * 常规选择时，为对应选项的文本内容；
     * 自定义输入时，为用户自定义输入的内容
     */
    private String content;


    /**
     * 结果类型，默认 {@link DialogStatus#CANCEL}
     */
    private DialogStatus result = DialogStatus.CANCEL;

    /**
     * 创建取消结果（index 为 {@link #INDEX_UNSELECTED}，content 为 null）
     */
    public static RadioResult ofCancel() {
        return new RadioResult();
    }

    /**
     * 创建常规选项结果
     *
     * @param index   选项下标
     * @param content 选项文本内容
     */
    public static RadioResult ofChoice(int index, String content) {
        RadioResult option = new RadioResult();
        option.result = DialogStatus.CONFIRM;
        option.index = index;
        option.content = content;
        return option;
    }

    /**
     * 创建自定义输入结果（index 为 {@link #INDEX_CUSTOM}）
     *
     * @param content 用户输入内容
     */
    public static RadioResult ofCustom(String content) {
        RadioResult option = new RadioResult();
        option.result = DialogStatus.CONFIRM;
        option.index = INDEX_CUSTOM;
        option.content = content;
        return option;
    }

    /**
     * 是否取消（未选择任何内容）
     */
    public boolean isCancel() {
        return result == null || result == DialogStatus.CANCEL;
    }

    /**
     * 是否为常规选项
     */
    public boolean isChoice() {
        return !isCancel() && index >= 0;
    }

    /**
     * 是否为自定义输入
     */
    public boolean isCustom() {
        return !isCancel() && index == INDEX_CUSTOM;
    }
}
