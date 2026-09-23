package i2f.form.dialog.checkbox;

import i2f.form.dialog.common.DialogStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 模态多选框窗口的返回结果
 *
 * <p>通过 {@link CheckboxDialog#checkbox(String, List, boolean, String)} 返回，
 * 可借助 {@link #isCancel()}、{@link #isConfirm()} 判断结果类型，
 * 通过 {@link #getChoices()} 获取勾选的选项集合。</p>
 */
@Data
@NoArgsConstructor
public class CheckboxResult {

    /**
     * 自定义项的下标值
     */
    public static final int INDEX_CUSTOM = -1;

    /**
     * 结果类型，默认 {@link DialogStatus#CANCEL}
     */
    private DialogStatus result = DialogStatus.CANCEL;

    /**
     * 勾选的选项集合，取消时为空集合；自定义项的下标为 {@link #INDEX_CUSTOM}
     */
    private List<Choice> choices = new ArrayList<>();


    /**
     * 单个勾选项：对应输入的 options 的下标与文本内容
     */
    @Data
    @NoArgsConstructor
    public static class Choice {

        /**
         * 对应输入的 options 的下标；{@link CheckboxResult#INDEX_CUSTOM} 表示自定义输入内容
         */
        private int index;

        /**
         * 常规选择时，为对应选项的文本内容；
         * 自定义输入时，为用户自定义输入的内容
         */
        private String content;

        /**
         * 创建常规选项
         *
         * @param index   选项下标
         * @param content 选项文本内容
         */
        public static Choice ofIndex(int index, String content) {
            Choice choice = new Choice();
            choice.index = index;
            choice.content = content;
            return choice;
        }

        /**
         * 创建自定义输入项（index 为 {@link CheckboxResult#INDEX_CUSTOM}）
         *
         * @param content 用户输入内容
         */
        public static Choice ofCustom(String content) {
            Choice choice = new Choice();
            choice.index = INDEX_CUSTOM;
            choice.content = content;
            return choice;
        }
    }

    /**
     * 创建取消结果（choices 为空集合）
     */
    public static CheckboxResult ofCancel() {
        return new CheckboxResult();
    }

    /**
     * 创建确认结果
     *
     * @param choices 勾选的选项集合，为 null 时按空集合处理
     */
    public static CheckboxResult ofConfirm(List<Choice> choices) {
        CheckboxResult option = new CheckboxResult();
        option.result = DialogStatus.CONFIRM;
        option.choices = choices == null ? new ArrayList<>() : choices;
        return option;
    }

    /**
     * 是否取消（未勾选任何项）
     */
    public boolean isCancel() {
        return result == null || result == DialogStatus.CANCEL;
    }

    /**
     * 是否确认
     */
    public boolean isConfirm() {
        return result == DialogStatus.CONFIRM;
    }
}
