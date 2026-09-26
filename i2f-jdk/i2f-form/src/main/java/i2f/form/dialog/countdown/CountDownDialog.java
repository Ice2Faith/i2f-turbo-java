package i2f.form.dialog.countdown;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ice2Faith
 * @date 2026/8/29 14:04
 * @desc
 */
public class CountDownDialog {
    /**
     * 显示一个倒计时窗口，提醒用户倒计时即将结束，应该尽快完成操作
     * 注意：窗口是异步的，如果要实现同步，请使用返回值进行同步等待
     *
     * @param countdownSeconds 显示的秒数
     * @return 如果需要等待倒计时窗口关闭，则需要使用 latch.await() 等待窗口关闭
     */
    public static CountDownLatch countDownTimer(int countdownSeconds) {
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
}
