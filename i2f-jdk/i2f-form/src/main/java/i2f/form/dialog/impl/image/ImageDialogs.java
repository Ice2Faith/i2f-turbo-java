package i2f.form.dialog.impl.image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2025/5/9 17:46
 */
public class ImageDialogs {

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
        if (title == null) {
            title = img.getName();
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
        dialog.setResizable(true);
        // 将对话框居中显示
        dialog.setLocationRelativeTo(null);


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
