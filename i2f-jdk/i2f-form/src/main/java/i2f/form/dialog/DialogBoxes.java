package i2f.form.dialog;

import i2f.form.dialog.checkbox.CheckboxDialog;
import i2f.form.dialog.checkbox.CheckboxResult;
import i2f.form.dialog.confirm.ConfirmDialog;
import i2f.form.dialog.countdown.CountDownDialog;
import i2f.form.dialog.input.InputDialog;
import i2f.form.dialog.input.InputResult;
import i2f.form.dialog.preview.PreviewDialogs;
import i2f.form.dialog.radio.RadioDialog;
import i2f.form.dialog.radio.RadioResult;
import i2f.jvm.JvmUtil;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Ice2Faith
 * @date 2025/4/30 10:11
 */
public class DialogBoxes {
    static {
        if (JvmUtil.isDebug()) {
            enableHeadless(false);
        }
    }

    public static void enableHeadless(boolean enable) {
        if (enable) {
            System.setProperty("java.awt.headless", "true");
        } else {
            System.setProperty("java.awt.headless", "false");
        }
    }

    // ==================== message（消息通知） ====================

    public static void message(String tips, String title) {
        ConfirmDialog.message(tips, title);
    }

    public static void message(String tips) {
        ConfirmDialog.message(tips);
    }

    // ==================== confirm（消息确认） ====================

    public static boolean confirm(String tips, String title) {
        return ConfirmDialog.confirm(tips, title);
    }

    public static boolean confirm(String tips) {
        return ConfirmDialog.confirm(tips);
    }

    // ==================== input（输入框） ====================

    public static InputResult input(String tips, String defaultValue, String title) {
        return InputDialog.input(tips, defaultValue, title);
    }

    public static InputResult input(String tips, String defaultValue) {
        return InputDialog.input(tips, defaultValue);
    }

    public static InputResult input(String tips) {
        return InputDialog.input(tips);
    }

    // ==================== radio（单选框） ====================

    public static RadioResult radio(String question, List<String> options, boolean allowCustomInput, String title) {
        return RadioDialog.radio(question, options, allowCustomInput, title);
    }

    public static RadioResult radio(String question, List<String> options) {
        return RadioDialog.radio(question, options);
    }

    public static RadioResult radio(String question, List<String> options, boolean allowCustomInput) {
        return RadioDialog.radio(question, options, allowCustomInput);
    }

    // ==================== checkbox（多选框） ====================

    public static CheckboxResult checkbox(String question, List<String> options, boolean allowCustomInput, String title) {
        return CheckboxDialog.checkbox(question, options, allowCustomInput, title);
    }

    public static CheckboxResult checkbox(String question, List<String> options) {
        return CheckboxDialog.checkbox(question, options);
    }

    public static CheckboxResult checkbox(String question, List<String> options, boolean allowCustomInput) {
        return CheckboxDialog.checkbox(question, options, allowCustomInput);
    }

    // ==================== countdown（倒计时） ====================

    public static CountDownLatch countDownTimer(int countdownSeconds) {
        return CountDownDialog.countDownTimer(countdownSeconds);
    }

    // ==================== preview（预览） ====================

    public static void preview(Object obj) {
        PreviewDialogs.preview(obj);
    }
}
