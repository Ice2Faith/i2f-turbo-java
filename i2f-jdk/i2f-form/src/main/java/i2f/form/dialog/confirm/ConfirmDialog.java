package i2f.form.dialog.confirm;

import i2f.form.dialog.common.DialogBase;
import i2f.form.dialog.common.ModernUi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 模态确认/消息对话框
 *
 * <p>弹出 720*480 的现代化窗口：中部为不可编辑的多行文本区域（可选中复制），底部按钮区。</p>
 *
 * <ul>
 *   <li>{@link #confirm(String, String)} — 确认对话框：【取消】【确认】双按钮，取消/关窗/Esc 返回 false</li>
 *   <li>{@link #message(String, String)} — 消息对话框：仅【确认】按钮，关窗/Esc 均视为确认</li>
 * </ul>
 *
 * <p>使用 {@link CountDownLatch} 阻塞调用线程实现模态效果，窗口构建与事件
 * 处理都在 Swing 事件分发线程（EDT）中完成，因此不会卡死 GUI。</p>
 *
 * <p>支持键盘操作：Enter 确认、Esc（confirm 模式取消，message 模式确认）。</p>
 */
public final class ConfirmDialog {

    private static final String DEFAULT_TITLE_CONFIRM = "确认";
    private static final String DEFAULT_TITLE_MESSAGE = "提示";

    private ConfirmDialog() {
    }

    // ==================== confirm（双按钮） ====================

    public static boolean confirm(String tips, String title) {
        return show(tips, title, false);
    }

    public static boolean confirm(String tips) {
        return confirm(tips, null);
    }

    // ==================== message（仅确认按钮） ====================

    public static void message(String tips, String title) {
        show(tips, title, true);
    }

    public static void message(String tips) {
        message(tips, null);
    }

    // ==================== 内部实现 ====================

    private static boolean show(String tips, String title, boolean messageMode) {
        if (SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("cannot run in Swing EDT(event dispatch thread), because of it will block ui thread");
        }
        AtomicBoolean result = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> showWindow(tips, title, result, latch, messageMode));
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            result.set(false);
        }
        return result.get();
    }

    private static void showWindow(String tips, String title, AtomicBoolean result, CountDownLatch latch, boolean messageMode) {
        DialogBase.installLookAndFeel();
        String defaultTitle = messageMode ? DEFAULT_TITLE_MESSAGE : DEFAULT_TITLE_CONFIRM;
        JDialog dialog = DialogBase.createDialog(title, defaultTitle);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                finish(dialog, messageMode, result, latch);
            }
        });

        JScrollPane scroll = buildScrollPane(tips);

        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createEmptyBorder(20, 24, 0, 24));
        root.add(scroll, BorderLayout.CENTER);
        root.add(buildButtonPanel(dialog, result, latch, messageMode), BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    private static JScrollPane buildScrollPane(String tips) {
        JTextArea textArea = ModernUi.readOnlyTextArea(tips == null ? "" : tips);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(new ModernUi.RoundedBorder(ModernUi.COLOR_BORDER, 8));
        scroll.setViewportBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(Color.WHITE);
        scroll.getViewport().setBackground(ModernUi.COLOR_FIELD_BG);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(DialogBase.SCROLL_UNIT);
        return scroll;
    }

    private static JPanel buildButtonPanel(JDialog dialog, AtomicBoolean result, CountDownLatch latch, boolean messageMode) {
        JButton confirmButton = ModernUi.flatButton(DialogBase.CONFIRM_TEXT, ModernUi.COLOR_PRIMARY,
                ModernUi.COLOR_PRIMARY_DARK, Color.WHITE);
        confirmButton.addActionListener(e -> finish(dialog, true, result, latch));
        dialog.getRootPane().setDefaultButton(confirmButton);

        int align = messageMode ? FlowLayout.CENTER : FlowLayout.RIGHT;
        JPanel panel = new JPanel(new FlowLayout(align, 12, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        if (messageMode) {
            bindEscKey(dialog, true, result, latch);
            panel.add(confirmButton);
        } else {
            JButton cancelButton = ModernUi.flatButton(DialogBase.CANCEL_TEXT, ModernUi.COLOR_BTN_GRAY,
                    ModernUi.COLOR_BTN_GRAY_DARK, ModernUi.COLOR_TEXT_SECONDARY);
            cancelButton.addActionListener(e -> finish(dialog, false, result, latch));
            bindEscKey(dialog, false, result, latch);
            panel.add(cancelButton);
            panel.add(confirmButton);
        }
        return panel;
    }

    private static void bindEscKey(JDialog dialog, boolean escIsConfirm, AtomicBoolean result, CountDownLatch latch) {
        String key = "confirm-dialog-esc";
        dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), key);
        dialog.getRootPane().getActionMap().put(key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finish(dialog, escIsConfirm, result, latch);
            }
        });
    }

    private static void finish(JDialog dialog, boolean confirmed, AtomicBoolean result, CountDownLatch latch) {
        result.set(confirmed);
        latch.countDown();
        dialog.dispose();
    }
}