package i2f.tools.tools;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.Tools;
import i2f.form.dialog.DialogBoxes;
import i2f.io.file.FileUtil;
import i2f.os.OsUtil;
import i2f.springboot.ops.openai.tool.impl.TmpFileTools;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ice2Faith
 * @date 2026/8/7 18:42
 * @desc
 */
@ConditionalOnExpression("${ai.tools.robot.enable:false}")
@Data
@NoArgsConstructor
@Component
@Tools(tags = {
        "robot"
})
public class RobotTools {
    private static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    @Autowired(required = false)
    protected TmpFileTools tmpFileTools;

    public static CountDownLatch startCountdown(int countdownSeconds) {
        CountDownLatch latch = new CountDownLatch(1);
        // 确保在 EDT 线程中创建和显示 UI
        SwingUtilities.invokeLater(() -> {
            JFrame countdownFrame = new JFrame("count down timer");
            countdownFrame.setAlwaysOnTop(true);
            countdownFrame.setUndecorated(true);
            countdownFrame.setFocusableWindowState(false);
            countdownFrame.setSize(200, 100);
            countdownFrame.setLocationRelativeTo(null);
            countdownFrame.setBackground(new Color(0, 0, 0, 128));
            countdownFrame.setOpacity(0.7f);
            // 窗口关闭时直接退出程序，防止后台进程残留
            countdownFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JLabel countdownLabel = new JLabel("", SwingConstants.CENTER);
            countdownLabel.setFont(new Font("Arial", Font.BOLD, 48));
            countdownLabel.setForeground(Color.WHITE);
            countdownFrame.add(countdownLabel);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle rect = new Rectangle(screenSize);

            AtomicLong lastMoveTs = new AtomicLong(0);
            // 核心：添加鼠标事件监听，进入时移开
            countdownFrame.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    randomPosition();
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    if (System.currentTimeMillis() - lastMoveTs.get() < 200) {
                        return;
                    }
                    lastMoveTs.set(System.currentTimeMillis());
                    randomPosition();
                }

                public void randomPosition() {

                    countdownFrame.setLocation((int) (Math.random() * (rect.getWidth() - countdownFrame.getWidth())),
                            (int) (Math.random() * (rect.getHeight() - countdownFrame.getHeight())));
                }
            });

            // 先显示窗口，再置顶
            countdownFrame.setVisible(true);
            countdownFrame.toFront();

            // 窗口被意外关闭时，也要释放 latch，防止业务线程永久阻塞
            countdownFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    latch.countDown();
                }
            });

            // 使用 Swing Timer 安全地驱动倒计时
            AtomicInteger count = new AtomicInteger(countdownSeconds);
            countdownLabel.setText(String.valueOf(count.get()));
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    count.decrementAndGet();
                    if (count.get() > 0) {
                        countdownLabel.setText(String.valueOf(count.get()));
                    } else {
                        // 倒计时结束，停止定时器并关闭窗口
                        ((Timer) e.getSource()).stop();
                        countdownFrame.dispose();
                        latch.countDown();
                    }
                }
            });
            timer.start();
        });
        return latch;
    }


    @Tool(
            tags = {
                    AiTags.SENSIBLE_VALUE
            },
            description = "capture user screen picture"
    )
    public TmpFileTools.FileAttachMessage capture_screen() throws Exception {
        if (!OsUtil.isWindows()) {
            throw new IllegalStateException("current OS is not windows, cannot capture screen!");
        }
        if (tmpFileTools == null) {
            throw new IllegalStateException("current application not enable tmp file");
        }

        System.setProperty("java.awt.headless", "false");

        boolean ok = DialogBoxes.confirm("即将进行屏幕抓取，请准备好要提供的屏幕显示，确认后将在倒计时结束后进行截图");
        if (!ok) {
            throw new IllegalStateException("user reject capture screen!");
        }

        CountDownLatch latch = startCountdown(5);
        latch.await();

        Thread.sleep(1000);

        // 1. 获取屏幕的默认尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);

        // 2. 创建 Robot 实例
        Robot robot = new Robot();

        // 3. 抓取屏幕图像
        BufferedImage image = robot.createScreenCapture(screenRectangle);

        File tempFile = FileUtil.getTempFile(".jpg");
        try {
            ImageIO.write(image, "jpg", tempFile);

            String virtualFileName = "screen-" + TIME_FORMATTER.format(LocalDateTime.now()) + ".jpg";
            TmpFileTools.UploadTmpFileMetadata metadata = tmpFileTools.saveFile(new FileInputStream(tempFile), virtualFileName);

            Map<String, Object> map = metadata.toMap();
            StringBuilder builder = new StringBuilder();
            builder.append("screen picture has upload, will send with after user message.\n");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }

            TmpFileTools.FileAttachMessage ret = new TmpFileTools.FileAttachMessage();
            ret.setContent(builder.toString());
            ret.setFile(metadata);
            return ret;
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}
