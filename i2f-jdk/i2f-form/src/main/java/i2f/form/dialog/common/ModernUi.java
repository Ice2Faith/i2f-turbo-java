package i2f.form.dialog.common;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 现代化 Swing 组件工具：统一配色、字体、圆角输入框、扁平圆角按钮、现代单选框图标。
 * 全部基于 JDK 自带功能实现，无第三方依赖。
 */
public final class ModernUi {

    /**
     * 主题色
     */
    public static final Color COLOR_PRIMARY = new Color(0x2F, 0x80, 0xED);
    /**
     * 主题色（悬停加深）
     */
    public static final Color COLOR_PRIMARY_DARK = new Color(0x24, 0x6B, 0xD2);
    /**
     * 主文字色
     */
    public static final Color COLOR_TEXT = new Color(0x33, 0x33, 0x33);
    /**
     * 次要文字色
     */
    public static final Color COLOR_TEXT_SECONDARY = new Color(0x60, 0x62, 0x66);
    /**
     * 边框色
     */
    public static final Color COLOR_BORDER = new Color(0xDC, 0xDF, 0xE6);
    /**
     * 只读输入框背景色
     */
    public static final Color COLOR_FIELD_BG = new Color(0xF5, 0xF7, 0xFA);
    /**
     * 次要按钮背景色
     */
    public static final Color COLOR_BTN_GRAY = new Color(0xF0, 0xF2, 0xF5);
    /**
     * 次要按钮悬停背景色
     */
    public static final Color COLOR_BTN_GRAY_DARK = new Color(0xE4, 0xE7, 0xED);

    private static final String FONT_FAMILY = pickFontFamily();

    private ModernUi() {
    }

    /**
     * 优先选择现代感更强的系统字体
     */
    private static String pickFontFamily() {
        Set<String> available = new HashSet<>(Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        String[] candidates = {"Microsoft YaHei UI", "PingFang SC", "Segoe UI", "Noto Sans CJK SC"};
        for (String name : candidates) {
            if (available.contains(name)) {
                return name;
            }
        }
        return Font.SANS_SERIF;
    }

    /**
     * 统一字体（常规字重）
     */
    public static Font font(int size) {
        return new Font(FONT_FAMILY, Font.PLAIN, size);
    }

    /**
     * 只读文本输入框：禁止输入，但可选中复制
     */
    public static JTextField readOnlyField(String text) {
        return createField(text, false, COLOR_FIELD_BG);
    }

    /**
     * 可编辑文本输入框
     */
    public static JTextField inputField() {
        return createField("", true, Color.WHITE);
    }

    /**
     * 只读多行文本区域：禁止输入，自动换行，可选中复制
     */
    public static JTextArea readOnlyTextArea(String text) {
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(true);
        area.setBackground(COLOR_FIELD_BG);
        area.setForeground(COLOR_TEXT);
        area.setFont(font(14));
        area.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        area.setCaretPosition(0);
        return area;
    }

    /**
     * 可编辑多行文本区域：自动换行，白底
     */
    public static JTextArea editableTextArea(String text) {
        JTextArea area = new JTextArea(text);
        area.setEditable(true);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(true);
        area.setBackground(Color.WHITE);
        area.setForeground(COLOR_TEXT);
        area.setFont(font(14));
        area.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        return area;
    }

    /**
     * 创建应用图标（主题色圆角方块 + 白色勾）
     */
    public static Image appIcon() {
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(COLOR_PRIMARY);
        g2.fillRoundRect(0, 0, 32, 32, 8, 8);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawPolyline(new int[]{9, 15, 24}, new int[]{16, 22, 11}, 3);
        g2.dispose();
        return image;
    }

    /**
     * 创建统一样式的文本输入框
     */
    private static JTextField createField(String text, boolean editable, Color bg) {
        JTextField field = new JTextField(text);
        field.setEditable(editable);
        field.setOpaque(true);
        field.setBackground(bg);
        field.setForeground(COLOR_TEXT);
        field.setFont(font(14));
        field.setBorder(new RoundedBorder(COLOR_BORDER, 8));
        return field;
    }

    /**
     * 现代化单选框
     */
    public static JRadioButton radioButton(String text) {
        JRadioButton radio = new JRadioButton(text);
        radio.setFont(font(14));
        radio.setForeground(COLOR_TEXT);
        radio.setOpaque(false);
        radio.setFocusPainted(false);
        radio.setRolloverEnabled(true);
        radio.setIcon(new ModernRadioIcon(radio));
        radio.setIconTextGap(8);
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return radio;
    }

    /**
     * 现代化复选框
     */
    public static JCheckBox checkBox(String text) {
        JCheckBox box = new JCheckBox(text);
        box.setFont(font(14));
        box.setForeground(COLOR_TEXT);
        box.setOpaque(false);
        box.setFocusPainted(false);
        box.setRolloverEnabled(true);
        box.setIcon(new ModernCheckIcon(box));
        box.setIconTextGap(8);
        box.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return box;
    }

    /**
     * 扁平圆角按钮
     */
    public static JButton flatButton(String text, Color normalBg, Color hoverBg, Color fg) {
        JButton button = new JButton(text);
        button.setUI(new FlatButtonUI(normalBg, hoverBg, fg));
        button.setFont(font(14));
        button.setPreferredSize(new Dimension(96, 34));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * 点击只读输入框时，联动选中对应的单选框
     */
    public static void bindClickToSelect(JTextField field, JRadioButton radio) {
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                radio.setSelected(true);
                field.requestFocusInWindow();
            }
        });
    }

    /**
     * 点击只读输入框时，联动切换对应的复选框
     */
    public static void bindClickToToggle(JTextField field, JCheckBox box) {
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                box.setSelected(!box.isSelected());
                field.requestFocusInWindow();
            }
        });
    }

    /**
     * 单选框与输入框组成的一行：单选框居左，输入框填充剩余宽度
     */
    public static JPanel optionRow(JRadioButton radio, JTextField field, int height) {
        return createOptionRow(radio, field, height);
    }

    /**
     * 复选框与输入框组成的一行：复选框居左，输入框填充剩余宽度
     */
    public static JPanel optionRow(JCheckBox checkBox, JTextField field, int height) {
        return createOptionRow(checkBox, field, height);
    }

    /**
     * 选择控件与输入框组成的一行：选择控件居左，输入框填充剩余宽度
     */
    private static JPanel createOptionRow(JComponent leading, JTextField field, int height) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        row.setPreferredSize(new Dimension(0, height));
        row.add(leading, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    /**
     * 圆角边框
     */
    public static class RoundedBorder implements Border {
        private final Color color;
        private final int radius;
        private final Insets insets;

        public RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
            this.insets = new Insets(4, 10, 4, 10);
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(x + 1, y + 1, width - 3, height - 3, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return insets;
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    /**
     * 扁平圆角按钮绘制器
     */
    private static class FlatButtonUI extends BasicButtonUI {
        /**
         * 按钮圆角
         */
        private static final int ARC = 12;
        private final Color normalBg;
        private final Color hoverBg;
        private final Color fg;

        FlatButtonUI(Color normalBg, Color hoverBg, Color fg) {
            this.normalBg = normalBg;
            this.hoverBg = hoverBg;
            this.fg = fg;
        }

        @Override
        protected void installDefaults(AbstractButton b) {
            super.installDefaults(b);
            b.setOpaque(false);
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton button = (AbstractButton) c;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ButtonModel model = button.getModel();
            g2.setColor(model.isRollover() || model.isPressed() ? hoverBg : normalBg);
            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), ARC, ARC);
            g2.dispose();
            super.paint(g, c);
        }

        @Override
        protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
            AbstractButton button = (AbstractButton) c;
            g.setFont(button.getFont());
            FontMetrics fm = g.getFontMetrics();
            g.setColor(fg);
            g.drawString(text, (c.getWidth() - fm.stringWidth(text)) / 2, textRect.y + fm.getAscent());
        }
    }

    /**
     * 现代化复选框图标：空心圆角方块，选中填充主题色并显示白色勾，悬停时边框高亮
     */
    private static class ModernCheckIcon implements Icon {
        private static final int SIZE = 18;
        private static final int BOX = SIZE - 2;
        private final AbstractButton button;

        ModernCheckIcon(AbstractButton button) {
            this.button = button;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (button.isSelected()) {
                g2.setColor(COLOR_PRIMARY);
                g2.fillRoundRect(x, y, BOX, BOX, 4, 4);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawPolyline(new int[]{x + 5, x + 8, x + 13}, new int[]{y + 9, y + 12, y + 6}, 3);
            } else {
                g2.setColor(button.getModel().isRollover() ? COLOR_PRIMARY : COLOR_BORDER);
                g2.setStroke(new BasicStroke(1.8f));
                g2.drawRoundRect(x + 1, y + 1, BOX - 2, BOX - 2, 4, 4);
            }
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }
    }

    /**
     * 现代化单选框图标：空心圆 + 选中实心点，悬停时边框高亮
     */
    private static class ModernRadioIcon implements Icon {
        private static final int SIZE = 18;
        private static final int DIAMETER = SIZE - 2;
        private static final int DOT_SIZE = DIAMETER - 10;
        private static final int DOT_OFFSET = (DIAMETER - DOT_SIZE) / 2;
        private final AbstractButton button;

        ModernRadioIcon(AbstractButton button) {
            this.button = button;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(button.getModel().isRollover() ? COLOR_PRIMARY : COLOR_BORDER);
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawOval(x, y, DIAMETER, DIAMETER);
            if (button.isSelected()) {
                g2.setColor(COLOR_PRIMARY);
                g2.fillOval(x + DOT_OFFSET, y + DOT_OFFSET, DOT_SIZE, DOT_SIZE);
            }
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }
    }
}
