package i2f.form.dialog.test;

import i2f.form.dialog.PreviewDialogs;

import java.io.File;

/**
 * @author Ice2Faith
 * @date 2025/5/10 13:33
 */
public class TestPreviewDialog {

    // 示例用法
    public static void main(String[] args) throws Throwable {

        PreviewDialogs.preview(new File("./test.png"));

        PreviewDialogs.preview(new File("./test.gif"));

        PreviewDialogs.preview(new File("./test.mp4"));

        PreviewDialogs.preview(new File("./test.mp3"));

        PreviewDialogs.preview("https://www.baidu.com/");
    }
}
