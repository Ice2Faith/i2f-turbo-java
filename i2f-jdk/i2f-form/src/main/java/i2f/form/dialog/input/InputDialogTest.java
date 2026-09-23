package i2f.form.dialog.input;

/**
 * InputDialog 的演示与自测入口：直接运行 main 方法即可查看窗口效果，
 * 输入结果会输出到控制台。窗口内支持 Enter 确认、Esc 取消。
 */
public class InputDialogTest {

    public static void main(String[] args) {
        String tips = "请输入审批意见：\n\n"
                + "1. 请简要说明审批理由\n"
                + "2. 如需退回，请注明退回原因\n"
                + "3. 意见将记录到审批流程中";

        InputResult option = InputDialog.input(tips, "同意，无异议。", "审批意见");

        if (option.isConfirm()) {
            System.out.println("用户确认，输入内容：\n" + option.getContent());
        } else {
            System.out.println("用户取消");
        }
    }
}