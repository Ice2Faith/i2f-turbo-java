package i2f.tools.tools;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.Tools;
import i2f.form.dialog.DialogBoxes;
import i2f.io.file.FileUtil;
import i2f.springboot.ops.openai.tool.impl.TmpFileTools;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

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
    @Autowired(required = false)
    protected TmpFileTools tmpFileTools;

    @Tool(
            tags = {
                    AiTags.SENSIBLE_VALUE
            },
            description = "capture user screen picture"
    )
    public TmpFileTools.FileAttachMessage capture_screen() throws Exception {
        System.setProperty("java.awt.headless", "false");

        boolean ok = DialogBoxes.confirm("即将进行屏幕抓取，请准备好要提供的屏幕显示，确认后将立即进行截图");
        if (!ok) {
            throw new IllegalStateException("user reject capture screen!");
        }

        Thread.sleep(1000);

        // 1. 获取屏幕的默认尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);

        // 2. 创建 Robot 实例
        Robot robot = new Robot();

        // 3. 抓取屏幕图像
        BufferedImage image = robot.createScreenCapture(screenRectangle);

        File tempFile = FileUtil.getTempFile(".jpg");
        ImageIO.write(image, "jpg", tempFile);

        TmpFileTools.UploadTmpFileMetadata metadata = tmpFileTools.saveFile(new FileInputStream(tempFile), tempFile.getName());

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
    }
}
