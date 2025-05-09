package i2f.form.dialog;

/**
 * @author Ice2Faith
 * @date 2025/4/30 10:11
 */

import i2f.jvm.JvmUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class DialogBoxes {
    static {
        if (JvmUtil.isDebug()) {
            System.setProperty("java.awt.headless", "false");
        }
    }

    // 示例用法
    public static void main(String[] args) throws Throwable {
//        String title = "输入对话框";
//        String defaultValue = "这是默认值";
//        String userInput = input(title, defaultValue);
//        System.out.println("用户输入：" + userInput);
//
//        confirm("同意");
//
//        message("已经同意");
//
//        previewImage(new File("./test.gif"));

//        previewGif(new File("./test.gif"),null,null);

//        previewMedia(new File("./test.mp4"), null, null);

//        previewMedia(new File("./test.mp3"), null, null);

//        previewUrl("https://www.baidu.com/", null, null);
    }

    public static enum ActionButton {
        OK(1),
        CANCEL(2);
        private int mask;

        private ActionButton(int mask) {
            this.mask = mask;
        }

        public int mask() {
            return this.mask;
        }
    }

    public static class ActionResult {
        ActionButton button = ActionButton.CANCEL;
        String text = null;

        public ActionResult(ActionButton button, String text) {
            this.button = button;
            this.text = text;
        }

        public static ActionResult ok(String text) {
            return new ActionResult(ActionButton.OK, text);
        }

        public static ActionResult cancel(String text) {
            return new ActionResult(ActionButton.CANCEL, text);
        }
    }

    public static final ActionButton[] DEFAULT_BUTTONS = new ActionButton[]{ActionButton.OK, ActionButton.CANCEL};

    /**
     * 显示一个消息对话框，允许用户查看通知消息
     *
     * @param defaultValue 显示的消息内容
     * @return 是否确认
     */
    public static void message(String defaultValue) {
        message(null, null, defaultValue);
    }

    /**
     * 显示一个消息对话框，允许用户查看通知消息
     *
     * @param title        窗口标题
     * @param defaultValue 显示的消息内容
     * @return 是否确认
     */
    public static void message(String title, String defaultValue) {
        message(title, null, defaultValue);
    }

    /**
     * 显示一个消息对话框，允许用户查看通知消息
     *
     * @param title        窗口标题
     * @param tips         输入的提示内容
     * @param defaultValue 显示的消息内容
     * @return 是否确认
     */
    public static void message(String title, String tips, String defaultValue) {
        if (title == null || title.isEmpty()) {
            title = "消息对话框";
        }
        if (tips == null || tips.isEmpty()) {
            tips = "消息通知：";
        }
        dialog(title, tips, defaultValue, ActionButton.OK);
    }

    /**
     * 显示一个确认对话框，允许用户确认或取消
     *
     * @param defaultValue 显示的具体的确认信息
     * @return 是否确认
     */
    public static boolean confirm(String defaultValue) {
        return confirm(null, null, defaultValue);
    }

    /**
     * 显示一个确认对话框，允许用户确认或取消
     *
     * @param title        窗口标题
     * @param defaultValue 显示的具体的确认信息
     * @return 是否确认
     */
    public static boolean confirm(String title, String defaultValue) {
        return confirm(title, null, defaultValue);
    }

    /**
     * 显示一个确认对话框，允许用户确认或取消
     *
     * @param title        窗口标题
     * @param tips         确认的简要提示
     * @param defaultValue 显示的具体的确认信息
     * @return 是否确认
     */
    public static boolean confirm(String title, String tips, String defaultValue) {
        if (title == null || title.isEmpty()) {
            title = "确认对话框";
        }
        if (tips == null || tips.isEmpty()) {
            tips = "是否确认？";
        }
        ActionResult result = dialog(title, tips, defaultValue);
        return result.button == ActionButton.OK;
    }

    /**
     * 显示一个输入框，允许用户输入多行文本。
     *
     * @param tips 输入的提示内容
     * @return 用户输入的文本或默认值
     */
    public static String input(String tips) {
        return input(null, tips, null);
    }

    /**
     * 显示一个输入框，允许用户输入多行文本。
     *
     * @param tips         输入的提示内容
     * @param defaultValue 默认值，如果用户点击取消，则返回此值
     * @return 用户输入的文本或默认值
     */
    public static String input(String tips, String defaultValue) {
        return input(null, tips, defaultValue);
    }

    /**
     * 显示一个输入框，允许用户输入多行文本。
     *
     * @param title        窗口标题
     * @param tips         输入的提示内容
     * @param defaultValue 默认值，如果用户点击取消，则返回此值
     * @return 用户输入的文本或默认值以及点击状态
     */
    public static String input(String title, String tips, String defaultValue) {
        if (title == null || title.isEmpty()) {
            title = "输入对话框";
        }
        if (tips == null || tips.isEmpty()) {
            tips = "请输入内容：";
        }
        ActionResult result = dialog(title, tips, defaultValue);
        return result.text;
    }


    /**
     * 显示一个输入框，允许用户输入多行文本。
     *
     * @param title        窗口标题
     * @param tips         输入的提示内容
     * @param defaultValue 默认值，如果用户点击取消，则返回此值
     * @param buttons      允许用户控制哪些按钮显示
     * @return 用户输入的文本或默认值以及点击状态
     */
    public static ActionResult dialog(String title, String tips, String defaultValue, ActionButton... buttons) {
        if (title == null || title.isEmpty()) {
            title = "输入对话框";
        }
        if (tips == null || tips.isEmpty()) {
            tips = "请输入内容：";
        }
        if (buttons == null || buttons.length == 0) {
            buttons = DEFAULT_BUTTONS;
        }

        // 创建一个模态对话框
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setSize(480, 320);
        dialog.setResizable(true);
        dialog.setLocationRelativeTo(null); // 居中显示
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        if (dialog.isAlwaysOnTopSupported()) {
            dialog.setAlwaysOnTop(true);
        }

        // 设置布局
        dialog.setLayout(new BorderLayout());

        // 创建标签
        JLabel label = new JLabel(tips);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialog.add(label, BorderLayout.NORTH);

        // 创建文本区域
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        if (defaultValue != null && !defaultValue.isEmpty()) {
            textArea.setText(defaultValue);
        }
        JScrollPane scrollPane = new JScrollPane(textArea);
        dialog.add(scrollPane, BorderLayout.CENTER);

        ActionResult result = new ActionResult(ActionButton.CANCEL, defaultValue);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        if (Arrays.asList(buttons).contains(ActionButton.OK)) {
            JButton okButton = new JButton("确定");
            buttonPanel.add(okButton);
            // 确定按钮的事件处理
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String text = textArea.getText();
                    result.button = ActionButton.OK;
                    result.text = text;
                    dialog.dispose();
                }
            });
        }

        if (Arrays.asList(buttons).contains(ActionButton.CANCEL)) {
            JButton cancelButton = new JButton("取消");
            buttonPanel.add(cancelButton);
            // 取消按钮的事件处理
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });
        }

        // 显示对话框并等待用户操作
        dialog.setVisible(true);

        dialog.dispose();

        return result;
    }

}
