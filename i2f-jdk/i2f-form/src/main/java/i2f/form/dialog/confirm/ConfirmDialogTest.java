package i2f.form.dialog.confirm;

/**
 * ConfirmDialog 的演示与自测入口：直接运行 main 方法即可查看窗口效果，
 * 确认/消息结果会输出到控制台。
 *
 * <p>confirm 模式：Enter 确认、Esc 取消；message 模式：仅确认按钮，Esc/关窗均视为确认。</p>
 */
public class ConfirmDialogTest {

    public static void main(String[] args) {
        // 确认对话框：双按钮
        String tips = "是否确认执行以下操作？\n\n"
                + "1. 删除选中的 3 条记录\n"
                + "2. 清理关联缓存数据\n"
                + "3. 发送操作日志到审计系统\n\n"
                + "该操作不可撤销，请谨慎选择。";
        boolean confirmed = ConfirmDialog.confirm(tips, "操作确认");
        System.out.println("confirm 结果：" + (confirmed ? "确认" : "取消"));

        // 消息对话框：仅确认按钮
        String msg = "操作已成功完成！\n\n"
                + "3 条记录已删除，关联缓存已清理，\n"
                + "审计日志已发送。";
        ConfirmDialog.message(msg, "操作完成");
        System.out.println("message 已确认，继续执行后续逻辑...");
    }
}