package i2f.form.dialog.impl.image;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2025/5/9 17:47
 */
public class IconDialogs {

    public static void previewGif(File imgFile, String title, Color bgColor) throws IOException {
        ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
        previewIcon(icon, title == null ? imgFile.getName() : title, bgColor);
    }

    public static void previewIcon(ImageIcon icon, String title, Color bgColor) throws IOException {
        // 如果标题为空，使用默认值
        if (title == null || title.isEmpty()) {
            title = "预览图标";
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

        dialog.setBackground(bgColor);

        // 获取屏幕尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // 计算图片的初始大小，保持等比例且不超过屏幕尺寸
        int imgWidth = icon.getIconWidth();
        int imgHeight = icon.getIconHeight();
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
        // 创建标签来显示GIF
        JLabel label = new JLabel() {

            @Override
            protected void paintComponent(Graphics g) {
                setOpaque(false);

                // 将 Graphics 转换为 Graphics2D 以进行高级操作
                Graphics2D g2d = (Graphics2D) g.create();

                // 设置抗锯齿以提高图像质量
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 设置背景为黑色
                g2d.setColor(imgBgColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());


                // 释放资源
                g2d.dispose();

                super.paintComponent(g);

            }
        };
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        // 使窗口大小可调整，并让图片自适应
        label.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        label.setBackground(bgColor);

        // 使用ImageIcon来显示GIF
        label.setIcon(icon);
        // 添加标签到对话框
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(label, BorderLayout.CENTER);

        // 添加窗口监听器以处理窗口关闭事件
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dialog.dispose();
            }
        });

        // 显示对话框
        dialog.setVisible(true);

        dialog.dispose();
    }
}
