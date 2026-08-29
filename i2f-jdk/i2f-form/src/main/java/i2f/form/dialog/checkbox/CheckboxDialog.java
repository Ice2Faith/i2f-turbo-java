package i2f.form.dialog.checkbox;

import i2f.form.dialog.common.DialogBase;
import i2f.form.dialog.common.ModernUi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 模态调研多选框窗口
 *
 * <p>弹出 720*480 的现代化多选框窗口：顶部为只读问题输入框（可复制），
 * 中部为可滚动的多选框列表（每个选项独占一行，可勾选多项），允许自定义输入时额外显示
 * 自定义复选框与输入框，底部右对齐【取消】【确认】按钮。</p>
 *
 * <p>使用 {@link CountDownLatch} 阻塞调用线程实现模态效果，窗口构建与事件
 * 处理都在 Swing 事件分发线程（EDT）中完成，因此不会卡死 GUI。</p>
 *
 * <p>键盘行为仅保留 Enter 确认、Esc 取消，勾选通过鼠标点击完成。</p>
 */
public final class CheckboxDialog {

    private static final int QUESTION_FIELD_HEIGHT = 40;
    private static final int ROW_HEIGHT = 40;
    private static final int ROW_GAP = 6;
    private static final String DEFAULT_TITLE = "多选框";

    private CheckboxDialog() {
    }

    /**
     * 弹出模态多选框窗口，等待用户勾选后返回
     */
    public static CheckboxResult checkbox(String question, List<String> options, boolean allowCustomInput, String title) {
        if (options == null) {
            throw new IllegalArgumentException("options cannot be null");
        }
        if (SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("cannot run in Swing EDT(event dispatch thread), because of it will block ui thread");
        }
        AtomicReference<CheckboxResult> result = new AtomicReference<>(CheckboxResult.ofCancel());
        CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> showWindow(question, options, allowCustomInput, title, result, latch));
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            result.set(CheckboxResult.ofCancel());
        }
        return result.get();
    }

    public static CheckboxResult checkbox(String question, List<String> options) {
        return checkbox(question, options, false, null);
    }

    public static CheckboxResult checkbox(String question, List<String> options, boolean allowCustomInput) {
        return checkbox(question, options, allowCustomInput, null);
    }

    private static void showWindow(String question, List<String> options, boolean allowCustomInput,
                                   String title, AtomicReference<CheckboxResult> result, CountDownLatch latch) {
        DialogBase.installLookAndFeel();
        JDialog dialog = DialogBase.createDialog(title, DEFAULT_TITLE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel(dialog, result, latch);
            }
        });

        OptionsPanel optionsPanel = buildOptionsPanel(options, allowCustomInput);
        JTextField questionField = buildQuestionField(question);

        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createEmptyBorder(20, 24, 0, 24));
        root.add(questionField, BorderLayout.NORTH);
        root.add(buildScrollPane(optionsPanel.panel), BorderLayout.CENTER);
        root.add(buildButtonPanel(dialog, options, optionsPanel, result, latch), BorderLayout.SOUTH);

        bindEnterToConfirm(dialog, questionField, optionsPanel.customField);
        if (optionsPanel.customField != null) {
            optionsPanel.customBox.addActionListener(e -> optionsPanel.customField.requestFocusInWindow());
        }

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    private static JTextField buildQuestionField(String question) {
        JTextField field = ModernUi.readOnlyField(question == null ? "" : question);
        field.setPreferredSize(new Dimension(0, QUESTION_FIELD_HEIGHT));
        return field;
    }

    private static OptionsPanel buildOptionsPanel(List<String> options, boolean allowCustomInput) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        List<JCheckBox> boxes = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            if (i > 0) {
                panel.add(Box.createVerticalStrut(ROW_GAP));
            }
            JCheckBox box = ModernUi.checkBox(null);
            JTextField field = ModernUi.readOnlyField(options.get(i));
            ModernUi.bindClickToToggle(field, box);
            boxes.add(box);
            panel.add(ModernUi.optionRow(box, field, ROW_HEIGHT));
        }

        JCheckBox customBox = null;
        JTextField customField = null;
        if (allowCustomInput) {
            if (!options.isEmpty()) {
                panel.add(Box.createVerticalStrut(ROW_GAP));
            }
            customBox = ModernUi.checkBox("自定义");
            customField = ModernUi.inputField();
            panel.add(ModernUi.optionRow(customBox, customField, ROW_HEIGHT));
        }
        panel.add(Box.createVerticalGlue());
        return new OptionsPanel(panel, boxes, customBox, customField);
    }

    private static JScrollPane buildScrollPane(JPanel optionsPanel) {
        JScrollPane scroll = new JScrollPane(optionsPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(Color.WHITE);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(DialogBase.SCROLL_UNIT);
        return scroll;
    }

    private static JPanel buildButtonPanel(JDialog dialog, List<String> options, OptionsPanel optionsPanel,
                                           AtomicReference<CheckboxResult> result, CountDownLatch latch) {
        JButton cancelButton = ModernUi.flatButton(DialogBase.CANCEL_TEXT, ModernUi.COLOR_BTN_GRAY,
                ModernUi.COLOR_BTN_GRAY_DARK, ModernUi.COLOR_TEXT_SECONDARY);
        JButton confirmButton = ModernUi.flatButton(DialogBase.CONFIRM_TEXT, ModernUi.COLOR_PRIMARY,
                ModernUi.COLOR_PRIMARY_DARK, Color.WHITE);
        cancelButton.addActionListener(e -> cancel(dialog, result, latch));
        confirmButton.addActionListener(e -> confirm(dialog, options, optionsPanel, result, latch));

        dialog.getRootPane().setDefaultButton(confirmButton);
        bindCancelKey(dialog, result, latch);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(buildSmallButton("全选", e -> setAllSelected(optionsPanel, true)));
        leftPanel.add(buildSmallButton("反选", e -> invertSelection(optionsPanel)));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(cancelButton);
        rightPanel.add(confirmButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    /**
     * 创建次要小按钮：与取消按钮同配色
     */
    private static JButton buildSmallButton(String text, ActionListener action) {
        JButton button = ModernUi.flatButton(text, ModernUi.COLOR_BTN_GRAY,
                ModernUi.COLOR_BTN_GRAY_DARK, ModernUi.COLOR_TEXT_SECONDARY);
        button.setPreferredSize(new Dimension(64, 34));
        button.addActionListener(action);
        return button;
    }

    /**
     * 全选/取消全选常规选项（自定义项除外）
     */
    private static void setAllSelected(OptionsPanel optionsPanel, boolean selected) {
        for (JCheckBox box : optionsPanel.boxes) {
            box.setSelected(selected);
        }
    }

    /**
     * 反选常规选项（自定义项除外）
     */
    private static void invertSelection(OptionsPanel optionsPanel) {
        for (JCheckBox box : optionsPanel.boxes) {
            box.setSelected(!box.isSelected());
        }
    }

    private static void bindCancelKey(JDialog dialog, AtomicReference<CheckboxResult> result, CountDownLatch latch) {
        String key = "choise-box-cancel";
        dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), key);
        dialog.getRootPane().getActionMap().put(key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel(dialog, result, latch);
            }
        });
    }

    private static void bindEnterToConfirm(JDialog dialog, JTextField... fields) {
        for (JTextField field : fields) {
            if (field != null) {
                field.addActionListener(e -> dialog.getRootPane().getDefaultButton().doClick());
            }
        }
    }

    private static void confirm(JDialog dialog, List<String> options, OptionsPanel optionsPanel,
                                AtomicReference<CheckboxResult> result, CountDownLatch latch) {
        finish(dialog, collectChoices(options, optionsPanel), result, latch);
    }

    private static void cancel(JDialog dialog, AtomicReference<CheckboxResult> result, CountDownLatch latch) {
        finish(dialog, CheckboxResult.ofCancel(), result, latch);
    }

    private static CheckboxResult collectChoices(List<String> options, OptionsPanel optionsPanel) {
        List<CheckboxResult.Choice> choices = new ArrayList<>();
        for (int i = 0; i < optionsPanel.boxes.size(); i++) {
            if (optionsPanel.boxes.get(i).isSelected()) {
                choices.add(CheckboxResult.Choice.ofIndex(i, options.get(i)));
            }
        }
        if (optionsPanel.customBox != null && optionsPanel.customBox.isSelected()) {
            choices.add(CheckboxResult.Choice.ofCustom(optionsPanel.customField.getText()));
        }
        if (choices.isEmpty()) {
            return CheckboxResult.ofCancel();
        }
        return CheckboxResult.ofConfirm(choices);
    }

    private static void finish(JDialog dialog, CheckboxResult option, AtomicReference<CheckboxResult> result, CountDownLatch latch) {
        result.set(option);
        latch.countDown();
        dialog.dispose();
    }

    private static class OptionsPanel {
        private final JPanel panel;
        private final List<JCheckBox> boxes;
        private final JCheckBox customBox;
        private final JTextField customField;

        OptionsPanel(JPanel panel, List<JCheckBox> boxes, JCheckBox customBox, JTextField customField) {
            this.panel = panel;
            this.boxes = boxes;
            this.customBox = customBox;
            this.customField = customField;
        }
    }
}
