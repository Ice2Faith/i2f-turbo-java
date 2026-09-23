package i2f.form.dialog.common;

import javax.swing.*;
import java.awt.*;

/**
 * 对话框共享基础设施：统一窗口尺寸、按钮文案、字符串工具、外观设置。
 * 所有 dialog 类均通过此基类复用常量与方法，避免重复。
 */
public final class DialogBase {

    /**
     * 窗口尺寸
     */
    public static final int WINDOW_WIDTH = 720;
    public static final int WINDOW_HEIGHT = 480;

    /**
     * 滚动条单步滚动像素
     */
    public static final int SCROLL_UNIT = 16;

    /**
     * 按钮文案
     */
    public static final String CANCEL_TEXT = "取消";
    public static final String CONFIRM_TEXT = "确认";

    private static volatile boolean lookAndFeelInstalled = false;

    private DialogBase() {
    }

    /**
     * 判断字符串是否为 null 或空白
     */
    public static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * 创建窗口骨架：720×480、可缩放、居中、置顶、白色背景、应用图标
     *
     * @param title        窗口标题，为 null 或空白时使用 defaultTitle
     * @param defaultTitle 默认标题
     */
    public static JDialog createDialog(String title, String defaultTitle) {
        JDialog dialog = new JDialog((Frame) null, isBlank(title) ? defaultTitle : title);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        dialog.setResizable(true);
        dialog.setLocationRelativeTo(null);
        dialog.setBackground(Color.WHITE);
        dialog.setIconImage(ModernUi.appIcon());
        dialog.setAlwaysOnTop(true);
        return dialog;
    }

    /**
     * 使用系统外观，保证整体观感与操作系统一致（全局仅设置一次）
     */
    public static void installLookAndFeel() {
        if (lookAndFeelInstalled) {
            return;
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            lookAndFeelInstalled = true;
        } catch (Exception e) {
            // 设置失败时使用默认外观，不影响功能
        }
    }
}