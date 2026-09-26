package i2f.form.dialog.radio;

import i2f.form.dialog.common.DialogBase;
import i2f.form.dialog.common.ModernUi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 模态调研单选框窗口
 *
 * <p>弹出 720*480 的现代化单选框窗口：顶部为只读问题输入框（可复制），
 * 中部为可滚动的单选框列表（每个选项独占一行），允许自定义输入时额外显示
 * 自定义单选框与输入框，底部右对齐【取消】【确认】按钮。</p>
 *
 * <p>使用 {@link CountDownLatch} 阻塞调用线程实现模态效果，窗口构建与事件
 * 处理都在 Swing 事件分发线程（EDT）中完成，因此不会卡死 GUI。</p>
 *
 * <p>支持键盘操作：Enter 确认、Esc 取消；UP/DOWN 上下切换单选框，
 * Home/End 选择第一项/最后一项，选中项自动滚动到可视区内。</p>
 */
public final class RadioDialog {

    private static final int QUESTION_FIELD_HEIGHT = 40;
    private static final int ROW_HEIGHT = 40;
    private static final int ROW_GAP = 6;
    private static final String DEFAULT_TITLE = "单选框";

    private RadioDialog() {
    }

    /**
     * 弹出模态单选框窗口，等待用户选择后返回
     */
    public static RadioResult radio(String question, List<String> options, boolean allowCustomInput, String title) {
        if (options == null) {
            throw new IllegalArgumentException("options cannot be null");
        }
        if (SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("cannot run in Swing EDT(event dispatch thread), because of it will block ui thread");
        }
        AtomicReference<RadioResult> result = new AtomicReference<>(RadioResult.ofCancel());
        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> showWindow(question, options, allowCustomInput, title, result, latch));
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            result.set(RadioResult.ofCancel());
        }
        return result.get();
    }

    public static RadioResult radio(String question, List<String> options) {
        return radio(question, options, false, null);
    }

    public static RadioResult radio(String question, List<String> options, boolean allowCustomInput) {
        return radio(question, options, allowCustomInput, null);
    }

    private static void showWindow(String question, List<String> options, boolean allowCustomInput,
                                   String title, AtomicReference<RadioResult> result, CountDownLatch latch) {
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
            optionsPanel.customRadio.addActionListener(e -> optionsPanel.customField.requestFocusInWindow());
        }
        bindKeyboardNavigation(dialog, questionField, optionsPanel);

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

        ButtonGroup group = new ButtonGroup();
        List<JRadioButton> radios = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            if (i > 0) {
                panel.add(Box.createVerticalStrut(ROW_GAP));
            }
            JRadioButton radio = ModernUi.radioButton(null);
            JTextField field = ModernUi.readOnlyField(options.get(i));
            ModernUi.bindClickToSelect(field, radio);
            radios.add(radio);
            group.add(radio);
            panel.add(ModernUi.optionRow(radio, field, ROW_HEIGHT));
        }

        JRadioButton customRadio = null;
        JTextField customField = null;
        if (allowCustomInput) {
            if (!options.isEmpty()) {
                panel.add(Box.createVerticalStrut(ROW_GAP));
            }
            customRadio = ModernUi.radioButton("自定义");
            customField = ModernUi.inputField();
            group.add(customRadio);
            panel.add(ModernUi.optionRow(customRadio, customField, ROW_HEIGHT));
        }
        panel.add(Box.createVerticalGlue());
        return new OptionsPanel(panel, radios, customRadio, customField);
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
                                           AtomicReference<RadioResult> result, CountDownLatch latch) {
        JButton cancelButton = ModernUi.flatButton(DialogBase.CANCEL_TEXT, ModernUi.COLOR_BTN_GRAY,
                ModernUi.COLOR_BTN_GRAY_DARK, ModernUi.COLOR_TEXT_SECONDARY);
        JButton confirmButton = ModernUi.flatButton(DialogBase.CONFIRM_TEXT, ModernUi.COLOR_PRIMARY,
                ModernUi.COLOR_PRIMARY_DARK, Color.WHITE);
        cancelButton.addActionListener(e -> cancel(dialog, result, latch));
        confirmButton.addActionListener(e -> confirm(dialog, options, optionsPanel, result, latch));

        dialog.getRootPane().setDefaultButton(confirmButton);
        bindCancelKey(dialog, result, latch);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(cancelButton);
        panel.add(confirmButton);
        return panel;
    }

    private static void bindCancelKey(JDialog dialog, AtomicReference<RadioResult> result, CountDownLatch latch) {
        String key = "select-box-cancel";
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

    private static void bindKeyboardNavigation(JDialog dialog, JTextField questionField, OptionsPanel optionsPanel) {
        List<JRadioButton> radios = new ArrayList<>(optionsPanel.radios);
        if (optionsPanel.customRadio != null) {
            radios.add(optionsPanel.customRadio);
        }
        for (JRadioButton radio : radios) {
            bindNavigationKeys(radio, JComponent.WHEN_FOCUSED, radios);
        }
        bindNavigationKeys(questionField, JComponent.WHEN_FOCUSED, radios);
        bindNavigationKeys(dialog.getRootPane(), JComponent.WHEN_IN_FOCUSED_WINDOW, radios);
    }

    private static void bindNavigationKeys(JComponent component, int condition, List<JRadioButton> radios) {
        bindNavKey(component, condition, KeyEvent.VK_UP, () -> moveSelection(radios, -1));
        bindNavKey(component, condition, KeyEvent.VK_DOWN, () -> moveSelection(radios, 1));
        bindNavKey(component, condition, KeyEvent.VK_HOME, () -> selectEdge(radios, true));
        bindNavKey(component, condition, KeyEvent.VK_END, () -> selectEdge(radios, false));
    }

    private static void bindNavKey(JComponent component, int condition, int keyCode, Runnable action) {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, 0);
        String key = "nav-" + keyCode;
        component.getInputMap(condition).put(keyStroke, key);
        component.getActionMap().put(key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    private static void moveSelection(List<JRadioButton> radios, int delta) {
        int current = selectedIndex(radios);
        int target = current < 0 ? 0 : current + delta;
        if (target >= 0 && target < radios.size()) {
            selectRadio(radios.get(target));
        }
    }

    private static void selectEdge(List<JRadioButton> radios, boolean first) {
        if (!radios.isEmpty()) {
            selectRadio(first ? radios.get(0) : radios.get(radios.size() - 1));
        }
    }

    private static int selectedIndex(List<JRadioButton> radios) {
        for (int i = 0; i < radios.size(); i++) {
            if (radios.get(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    private static void selectRadio(JRadioButton radio) {
        radio.setSelected(true);
        radio.requestFocusInWindow();
        JComponent row = (JComponent) radio.getParent();
        row.scrollRectToVisible(new Rectangle(0, 0, row.getWidth(), row.getHeight()));
    }

    private static void confirm(JDialog dialog, List<String> options, OptionsPanel optionsPanel,
                                AtomicReference<RadioResult> result, CountDownLatch latch) {
        finish(dialog, collectSelection(options, optionsPanel), result, latch);
    }

    private static void cancel(JDialog dialog, AtomicReference<RadioResult> result, CountDownLatch latch) {
        finish(dialog, RadioResult.ofCancel(), result, latch);
    }

    private static RadioResult collectSelection(List<String> options, OptionsPanel optionsPanel) {
        if (optionsPanel.customRadio != null && optionsPanel.customRadio.isSelected()) {
            return RadioResult.ofCustom(optionsPanel.customField.getText());
        }
        for (int i = 0; i < optionsPanel.radios.size(); i++) {
            if (optionsPanel.radios.get(i).isSelected()) {
                return RadioResult.ofChoice(i, options.get(i));
            }
        }
        return RadioResult.ofCancel();
    }

    private static void finish(JDialog dialog, RadioResult option, AtomicReference<RadioResult> result, CountDownLatch latch) {
        result.set(option);
        latch.countDown();
        dialog.dispose();
    }

    private static class OptionsPanel {
        private final JPanel panel;
        private final List<JRadioButton> radios;
        private final JRadioButton customRadio;
        private final JTextField customField;

        OptionsPanel(JPanel panel, List<JRadioButton> radios, JRadioButton customRadio, JTextField customField) {
            this.panel = panel;
            this.radios = radios;
            this.customRadio = customRadio;
            this.customField = customField;
        }
    }
}