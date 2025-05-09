package i2f.form.dialog.impl.web;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

/**
 * @author Ice2Faith
 * @date 2025/5/9 17:50
 */
public class WebDialogs {

    public static void previewWeb(URL url, String title, Color bgColor) {
        previewWeb(url == null ? null : url.toString(), title == null ? (url == null ? null : url.toString()) : title, bgColor);
    }

    public static void previewWeb(String url, String title, Color bgColor) {
        if (url == null) {
            url = "https://www.baidu.com/";
        }
        if (title == null || title.isEmpty()) {
            title = "预览网页";
        }
        if (bgColor == null) {
            bgColor = Color.WHITE;
        }
        Color imgBgColor = bgColor;

        // 创建一个模态对话框
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setSize(1080, 720);
        dialog.setResizable(true);
        dialog.setLocationRelativeTo(null); // 居中显示
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // 检查JavaFX是否可用
        // 初始化JavaFX环境
        JFXPanel fxPanel = new JFXPanel();
        String loadUrl = url;
        Platform.runLater(() -> {

            // 创建JavaFX内容
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, 1080, 720);
            root.setPrefSize(1080, 720);
            // 设置背景颜色
            root.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.rgb(imgBgColor.getRed(), imgBgColor.getGreen(), imgBgColor.getBlue()), null, null)));

            // 创建WebView和WebEngine
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.load(loadUrl);

            root.setCenter(webView);

            // 设置场景到JFXPanel
            fxPanel.setScene(scene);

            // 添加JFXPanel到对话框
            dialog.add(fxPanel, BorderLayout.CENTER);

            // 添加窗口监听器以处理窗口关闭事件
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {

                    dialog.dispose();
                }
            });

            // 确保对话框可以获取焦点，以能够获取键盘事件
            dialog.setFocusable(true);
            dialog.requestFocusInWindow();

            dialog.addKeyListener(new KeyAdapter() {

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_F5) {
                        webEngine.reload();
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        webEngine.getHistory().go(-1);
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        webEngine.getHistory().go(1);
                    }
                }
            });

        });

        // 显示对话框
        dialog.setVisible(true);

        dialog.dispose();
    }
}
