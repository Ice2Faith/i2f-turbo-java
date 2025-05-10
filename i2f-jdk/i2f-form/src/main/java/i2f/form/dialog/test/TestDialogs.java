package i2f.form.dialog.test;

import i2f.form.dialog.DialogBoxes;

/**
 * @author Ice2Faith
 * @date 2025/5/10 13:33
 */
public class TestDialogs {

    // 示例用法
    public static void main(String[] args) throws Throwable {
        String title = "输入对话框";
        String defaultValue = "这是默认值";
        String userInput = DialogBoxes.input(title, defaultValue);
        System.out.println("用户输入：" + userInput);

        DialogBoxes.confirm("同意");

        DialogBoxes.message("已经同意");

    }
}
