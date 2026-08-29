package i2f.form.dialog.input;

import i2f.form.dialog.common.DialogBase;
import i2f.form.dialog.common.ModernUi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 模态输入对话框
 *
 * <p>弹出 720*480 的现代化输入窗口，上下两部分：上半部分为不可编辑的提示文本，
 * 下半部分为可编辑的多行输入区域，底部右对齐【取消】【确认】按钮。</p>
 *
 * <p>使用 {@link CountDownLatch} 阻塞调用线程实现模态效果，窗口构建与事件
 * 处理都在 Swing 事件分发线程（EDT）中完成，因此不会卡死 GUI。</p>
 *
 * <p>支持键盘操作：Enter 确认、Esc 取消。</p>
 */
public final class InputDialog {

    private static final double TIPS_HEIGHT_RATIO = 0.25;
    private static final String DEFAULT_TITLE = "输入";

    private InputDialog() {
    }

    public static InputResult input(String tips, String defaultValue, String title) {
        if (SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("cannot run in Swing EDT(event dispatch thread), because of it will block ui thread");
        }
        AtomicReference<InputResult> result = new AtomicReference<>(InputResult.ofCancel());
        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> showWindow(tips, defaultValue, title, result, latch));
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            result.set(InputResult.ofCancel());
        }
        return result.get();
    }

    public static InputResult input(String tips, String defaultValue) {
        return input(tips, defaultValue, null);
    }

    public static InputResult input(String tips) {
        return input(tips, null, null);
    }

    private static void showWindow(String tips, String defaultValue, String title,
                                   AtomicReference<InputResult> result, CountDownLatch latch) {
        DialogBase.installLookAndFeel();
        JDialog dialog = DialogBase.createDialog(title, DEFAULT_TITLE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                finish(dialog, InputResult.ofCancel(), result, latch);
            }
        });

        JTextArea tipsArea = ModernUi.readOnlyTextArea(tips == null ? "" : tips);
        tipsArea.setRows(1);
        JScrollPane tipsScroll = wrapScrollPane(tipsArea);
        JTextArea inputArea = ModernUi.editableTextArea(defaultValue == null ? "" : defaultValue);
        inputArea.setRows(1);
        JScrollPane inputScroll = wrapScrollPane(inputArea);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        GridBagConstraints tipsGbc = new GridBagConstraints();
        tipsGbc.fill = GridBagConstraints.BOTH;
        tipsGbc.gridx = 0;
        tipsGbc.gridy = 0;
        tipsGbc.weightx = 1.0;
        tipsGbc.weighty = TIPS_HEIGHT_RATIO;
        tipsGbc.insets = new Insets(0, 0, 5, 0);
        center.add(tipsScroll, tipsGbc);

        GridBagConstraints inputGbc = new GridBagConstraints();
        inputGbc.fill = GridBagConstraints.BOTH;
        inputGbc.gridx = 0;
        inputGbc.gridy = 1;
        inputGbc.weightx = 1.0;
        inputGbc.weighty = 1.0 - TIPS_HEIGHT_RATIO;
        inputGbc.insets = new Insets(5, 0, 0, 0);
        center.add(inputScroll, inputGbc);

        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createEmptyBorder(20, 24, 0, 24));
        root.add(center, BorderLayout.CENTER);
        root.add(buildButtonPanel(dialog, inputArea, result, latch), BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
        inputArea.requestFocusInWindow();
    }

    private static JScrollPane wrapScrollPane(JTextArea textArea) {
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(new ModernUi.RoundedBorder(ModernUi.COLOR_BORDER, 8));
        scroll.setViewportBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(Color.WHITE);
        scroll.getViewport().setBackground(textArea.getBackground());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(DialogBase.SCROLL_UNIT);
        return scroll;
    }

    private static JPanel buildButtonPanel(JDialog dialog, JTextArea inputArea,
                                           AtomicReference<InputResult> result, CountDownLatch latch) {
        JButton cancelButton = ModernUi.flatButton(DialogBase.CANCEL_TEXT, ModernUi.COLOR_BTN_GRAY,
                ModernUi.COLOR_BTN_GRAY_DARK, ModernUi.COLOR_TEXT_SECONDARY);
        JButton confirmButton = ModernUi.flatButton(DialogBase.CONFIRM_TEXT, ModernUi.COLOR_PRIMARY,
                ModernUi.COLOR_PRIMARY_DARK, Color.WHITE);
        cancelButton.addActionListener(e -> finish(dialog, InputResult.ofCancel(), result, latch));
        confirmButton.addActionListener(e -> finish(dialog, InputResult.ofConfirm(inputArea.getText()), result, latch));

        dialog.getRootPane().setDefaultButton(confirmButton);
        bindEscKey(dialog, result, latch);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(cancelButton);
        panel.add(confirmButton);
        return panel;
    }

    private static void bindEscKey(JDialog dialog, AtomicReference<InputResult> result, CountDownLatch latch) {
        String key = "input-dialog-esc";
        dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), key);
        dialog.getRootPane().getActionMap().put(key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finish(dialog, InputResult.ofCancel(), result, latch);
            }
        });
    }

    private static void finish(JDialog dialog, InputResult option, AtomicReference<InputResult> result, CountDownLatch latch) {
        result.set(option);
        latch.countDown();
        dialog.dispose();
    }
}