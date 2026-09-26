package i2f.form.dialog.radio;

import java.util.ArrayList;
import java.util.List;

/**
 * selectBox 的演示与自测入口：直接运行 main 方法即可查看窗口效果，
 * 选择结果会输出到控制台。窗口内支持 Enter 确认、Esc 取消，
 * UP/DOWN 上下切换、Home/End 选择首尾项。
 */
public class RadioDialogTest {

    public static void main(String[] args) {
        List<String> options = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            options.add("选项内容-" + i + "：这是第 " + i + " 个可选内容");
        }
        RadioResult selected = RadioDialog.radio(
                "请从以下选项中选择一项（本行文字可选中复制）", options, true, "请选择一项");
        if (selected.isCancel()) {
            System.out.println("已取消选择：" + selected);
        } else if (selected.isCustom()) {
            System.out.println("自定义输入：" + selected.getContent());
        } else {
            System.out.println("选择第 " + selected.getIndex() + " 项：" + selected.getContent());
        }
    }
}
