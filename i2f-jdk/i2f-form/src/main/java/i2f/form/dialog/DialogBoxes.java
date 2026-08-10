package i2f.form.dialog;

/**
 * @author Ice2Faith
 * @date 2025/4/30 10:11
 */

import i2f.jvm.JvmUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DialogBoxes {
    static {
        if (JvmUtil.isDebug()) {
            enableHeadless(false);
        }
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

    public static void enableHeadless(boolean enable){
        if(enable){
            System.setProperty("java.awt.headless", "true");
        }else{
            System.setProperty("java.awt.headless", "false");
        }
    }

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
        dialog(title, tips, defaultValue, ActionButton.OK);
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
        ActionResult result = dialog(title, tips, defaultValue);
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
        ActionResult result = dialog(title, tips, defaultValue);
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
    public static ActionResult dialog(String title, String tips, String defaultValue, ActionButton... buttons) {
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

}
