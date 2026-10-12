package i2f.form.dialog.checkbox;

import java.util.ArrayList;
import java.util.List;

/**
 * choiceBox 的演示与自测入口：直接运行 main 方法即可查看窗口效果，
 * 选择结果会输出到控制台。窗口内仅支持 Enter 确认、Esc 取消，
 * 勾选通过鼠标点击完成，可勾选多项。
 */
public class CheckboxDialogTest {

    public static void main(String[] args) {
        List<String> options = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            options.add("选项内容-" + i + "：这是第 " + i + " 个可选内容");
        }
        CheckboxResult selected = CheckboxDialog.checkbox(
                "请从以下选项中选择多项（本行文字可选中复制）", options, true, "请选择多项");
        if (selected.isCancel()) {
            System.out.println("已取消选择：" + selected);
        } else {
            for (CheckboxResult.Choice choice : selected.getChoices()) {
                if (choice.getIndex() == CheckboxResult.INDEX_CUSTOM) {
                    System.out.println("自定义输入：" + choice.getContent());
                } else {
                    System.out.println("选择第 " + choice.getIndex() + " 项：" + choice.getContent());
                }
            }
        }
    }
}
