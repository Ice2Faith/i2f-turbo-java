package i2f.form.dialog;

/**
 * @author Ice2Faith
 * @date 2025/4/30 10:11
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class DialogBoxes {
    // 示例用法
    public static void main(String[] args) throws Throwable {
        String title = "输入对话框";
        String defaultValue = "这是默认值";
        String userInput = input(title, defaultValue);
        System.out.println("用户输入：" + userInput);

        confirm("同意");

        message("已经同意");

        previewImage(new File("./clip.png"));
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
        action(title, tips, defaultValue, ActionButton.OK);
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
        ActionResult result = action(title, tips, defaultValue);
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
        ActionResult result = action(title, tips, defaultValue);
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
    public static ActionResult action(String title, String tips, String defaultValue, ActionButton... buttons) {
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

    /**
     * 预览图片的函数
     *
     * @param img 要展示的图片
     */
    public static void previewImage(File img) throws IOException {
        previewImage(img, null, null);
    }

    /**
     * 预览图片的函数
     *
     * @param img   要展示的图片
     * @param title 窗口标题，如果为空则使用默认值
     */
    public static void previewImage(File img, String title) throws IOException {
        previewImage(img, title, null);
    }

    /**
     * 预览图片的函数
     *
     * @param img     要展示的图片
     * @param title   窗口标题，如果为空则使用默认值
     * @param bgColor 对话框的背景颜色，默认白色，当图片存在透明通道时，透明部分就显示这个背景颜色
     */
    public static void previewImage(File img, String title, Color bgColor) throws IOException {
        if(title==null){
            title=img.getName();
        }
        BufferedImage bufferedImage = ImageIO.read(img);
        previewImage(bufferedImage, title, bgColor);
    }

    /**
     * 预览图片的函数
     *
     * @param img 要展示的图片
     */
    public static void previewImage(BufferedImage img) {
        previewImage(img, null, null);
    }

    /**
     * 预览图片的函数
     *
     * @param img   要展示的图片
     * @param title 窗口标题，如果为空则使用默认值
     */
    public static void previewImage(BufferedImage img, String title) {
        previewImage(img, title, null);
    }

    /**
     * 预览图片的函数
     *
     * @param img     要展示的图片
     * @param title   窗口标题，如果为空则使用默认值
     * @param bgColor 对话框的背景颜色，默认白色，当图片存在透明通道时，透明部分就显示这个背景颜色
     */
    public static void previewImage(BufferedImage img, String title, Color bgColor) {
        // 如果标题为空，使用默认值
        if (title == null || title.isEmpty()) {
            title = "预览图片";
        }
        if (bgColor == null) {
            bgColor = Color.WHITE;
        }

        // 创建模态对话框
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setModal(true); // 设置为模态，阻塞其他窗口操作
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setUndecorated(false);

        // 获取屏幕尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // 计算图片的初始大小，保持等比例且不超过屏幕尺寸
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        double imgAspect = (double) imgWidth / imgHeight;

        // 设定最大宽度和高度不超过屏幕尺寸减去一些边距
        int maxWidth = (int) (screenSize.width * 0.9);
        int maxHeight = (int) (screenSize.height * 0.9);

        // 计算适合的初始对话框大小
        int dialogWidth = Math.min(imgWidth, maxWidth);
        int dialogHeight = Math.min(imgHeight, maxHeight);

        // 如果图片宽高比大于对话框宽高比，调整宽度；否则调整高度
        if ((double) dialogWidth / dialogHeight > imgAspect) {
            dialogWidth = (int) (dialogHeight * imgAspect);
        } else {
            dialogHeight = (int) (dialogWidth / imgAspect);
        }

        // 设置对话框大小
        dialog.setSize(dialogWidth, dialogHeight);

        // 将对话框居中显示
        dialog.setLocationRelativeTo(null);

        Color imgBgColor = bgColor;
        // 创建自定义的绘图面板
        JPanel imagePanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                setOpaque(false);
                super.paintComponent(g);

                // 将 Graphics 转换为 Graphics2D 以进行高级操作
                Graphics2D g2d = (Graphics2D) g.create();

                // 设置抗锯齿以提高图像质量
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 设置背景为黑色
                g2d.setColor(imgBgColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // 设置透明度合成规则，确保透明部分使用背景色
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

                // 计算缩放比例以适应当前面板大小，保持等比例
                double scale = Math.min((double) getWidth() / imgWidth, (double) getHeight() / imgHeight);
                int scaledWidth = (int) (imgWidth * scale);
                int scaledHeight = (int) (imgHeight * scale);

                // 计算居中位置
                int x = (getWidth() - scaledWidth) / 2;
                int y = (getHeight() - scaledHeight) / 2;

                // 绘制图片
                g2d.drawImage(img, x, y, scaledWidth, scaledHeight, this);

                // 释放资源
                g2d.dispose();
            }
        };

        // 使用滚动面板以支持用户调整大小时的滚动条（可选）
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // 添加滚动面板到对话框内容面板
        dialog.getContentPane().add(scrollPane);

        // 添加组件监听器以动态调整图片大小
        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                imagePanel.revalidate();
                imagePanel.repaint();
            }
        });

        // 显示对话框
        dialog.setVisible(true);

        dialog.dispose();
    }


}
