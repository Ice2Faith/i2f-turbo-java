package i2f.form.dialog.impl.media;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.*;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * @author Ice2Faith
 * @date 2025/5/9 17:49
 */
public class MediaDialogs {
    public static void previewMedia(URL url) {
        previewMedia(url, null, null);
    }

    public static void previewMedia(URL url, String title) {
        previewMedia(url, title, null);
    }

    public static void previewMedia(URL url, String title, Color bgColor) {
        URI uri = null;
        try {
            uri = url.toURI();
        } catch (Exception e) {

        }
        Media media = new Media(uri.toString());
        previewMedia(media, title == null ? url.toString() : title, bgColor);
    }

    public static void previewMedia(URI uri) {
        previewMedia(uri, null, null);
    }

    public static void previewMedia(URI uri, String title) {
        previewMedia(uri, title, null);
    }

    public static void previewMedia(URI uri, String title, Color bgColor) {
        Media media = new Media(uri.toString());
        previewMedia(media, title == null ? uri.toString() : title, bgColor);
    }

    public static void previewMedia(File file) {
        previewMedia(file, null, null);
    }

    public static void previewMedia(File file, String title) {
        previewMedia(file, title, null);
    }

    public static void previewMedia(File file, String title, Color bgColor) {
        Media media = new Media(file.toURI().toString());
        previewMedia(media, title == null ? file.getName() : title, bgColor);
    }

    public static void previewMedia(Media media) {
        previewMedia(media, null, null);
    }

    public static void previewMedia(Media media, String title) {
        previewMedia(media, title, null);
    }

    public static void previewMedia(Media media, String title, Color bgColor) {
        if (title == null || title.isEmpty()) {
            title = "预览媒体";
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
        Platform.runLater(() -> {

            // 创建JavaFX内容
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, 1080, 720);
            root.setPrefSize(1080, 720);
            // 设置背景颜色
            root.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.rgb(imgBgColor.getRed(), imgBgColor.getGreen(), imgBgColor.getBlue()), null, null)));


            // 加载视频文件
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.setPreserveRatio(true);
            mediaView.fitWidthProperty().bind(scene.widthProperty());
            mediaView.fitHeightProperty().bind(scene.heightProperty().subtract(48));
            root.setCenter(mediaView);
            mediaPlayer.play();

            // 创建进度条（滑块）
            Slider progressSlider = new Slider();
            progressSlider.setMin(0);
            progressSlider.setMax(100);
            // 处理媒体就绪事件（关键改动）
            mediaPlayer.setOnReady(() -> {
                // 设置进度条最大值
                progressSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());

                // 获取屏幕尺寸
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

                // 获取视频的原始尺寸
                int originalWidth = media.getWidth();
                int originalHeight = media.getHeight();
                int width = (int) Math.min(originalWidth, screenSize.getWidth() - 10); // 留出边框空间
                int height = (int) Math.min(originalHeight, screenSize.getHeight() - 96);
                boolean hasVideo = false;
                for (Track track : media.getTracks()) {
                    if (track instanceof VideoTrack) {
                        hasVideo = true;
                        break;
                    }
                }
                if (!hasVideo) {
                    width = 480;
                    height = 96;
                }
                Dimension windowSize = new Dimension(width, height);
                dialog.setSize(windowSize);

                dialog.setLocation((int) (screenSize.getWidth() - width) / 2, (int) (screenSize.getHeight() - height) / 2);

                // 使窗口大小可调整，并让视频自适应

                fxPanel.setPreferredSize(new Dimension(originalWidth, originalHeight));

            });

            progressSlider.setOnMouseClicked((event -> {
                mediaPlayer.seek(Duration.seconds(progressSlider.getValue()));
            }));

            mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                if (!progressSlider.isPressed()) {
                    progressSlider.setValue(newTime.toSeconds());
                }
            });


            // 使用 StackPane 作为容器限制高度
            StackPane sliderContainer = new StackPane(progressSlider);
            sliderContainer.setMaxHeight(48);
            sliderContainer.setMinHeight(48);
            sliderContainer.setPrefHeight(48);

            // 将 Slider 容器添加到底部
            root.setBottom(sliderContainer);

            // 设置场景到JFXPanel
            fxPanel.setScene(scene);

            // 添加JFXPanel到对话框
            dialog.add(fxPanel, BorderLayout.CENTER);

            // 添加窗口监听器以处理窗口关闭事件
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                    dialog.dispose();
                }
            });

            // 确保对话框可以获取焦点，以能够获取键盘事件
            dialog.setFocusable(true);
            dialog.requestFocusInWindow();

            dialog.addKeyListener(new KeyAdapter() {

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        MediaPlayer.Status status = mediaPlayer.getStatus();
                        if (status == MediaPlayer.Status.PLAYING) {
                            mediaPlayer.pause();
                        } else if (status == MediaPlayer.Status.READY
                                || status == MediaPlayer.Status.PAUSED) {
                            mediaPlayer.play();
                        } else if (status == MediaPlayer.Status.STOPPED) {
                            mediaPlayer.seek(Duration.ZERO);
                            mediaPlayer.play();
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        double duration = progressSlider.getValue();
                        double rate = duration / progressSlider.getMax();
                        double delta = (0.05) * progressSlider.getMax();
                        if (delta < 5) {
                            delta = 5;
                        }
                        duration = Math.max(0, duration - delta);
                        mediaPlayer.seek(Duration.seconds(duration));
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        double duration = progressSlider.getValue();
                        double rate = duration / progressSlider.getMax();
                        double delta = (0.05) * progressSlider.getMax();
                        if (delta < 5) {
                            delta = 5;
                        }
                        duration = Math.min(progressSlider.getMax(), duration + delta);
                        mediaPlayer.seek(Duration.seconds(duration));
                    } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        mediaPlayer.stop();
                        mediaPlayer.dispose();
                        dialog.dispose();
                    }
                }
            });

        });

        // 显示对话框
        dialog.setVisible(true);

        dialog.dispose();
    }

}
