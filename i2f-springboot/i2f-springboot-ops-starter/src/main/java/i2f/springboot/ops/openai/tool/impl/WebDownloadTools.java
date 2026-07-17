package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.io.stream.StreamUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2026/6/22 16:55
 * @desc
 */
@ConditionalOnExpression("${ai.tools.web-download.enable:true}")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Tools(tags = {
        AiTags.WEB_VALUE,
        AiTags.DOWNLOAD_VALUE
})
public class WebDownloadTools {

    @Autowired(required = false)
    private LocalFileTools localFileTools;

    @Tool(
            tags = {
                    AiTags.WRITABLE_VALUE,
                    AiTags.HUMAN_VALUE
            }, description = "download a url content to file"
    )
    public String download_url_to_file(
            @ToolParam(value = "url", description = "the url, for example 'https://sample.com/download/a.png'")
            String url,
            @ToolParam(value = "savePath", description = "the save path, cloud be null means rootPath, for example 'user' or '/home'")
            String savePath
    ) throws Exception {
        if (savePath == null) {
            savePath = "";
        }
        URL href = new URL(url);
        String path = href.getPath();
        int idx = path.lastIndexOf("/");
        if (idx >= 0) {
            path = path.substring(idx + 1);
        }
        String[] arr = "~!@#$%^&*[];'/{}:\"<>?".split("");
        for (String str : arr) {
            path = path.replace(str, "");
        }

        String suffix = "";
        String name = path;
        String uid = UUID.randomUUID().toString().replace("-", "");
        idx = name.lastIndexOf(".");
        if (idx >= 0) {
            name = name.substring(0, idx);
            suffix = name.substring(idx);
        }

        if (localFileTools == null) {
            throw new IllegalStateException("missing local-file secure control.");
        }
        File saveFile = localFileTools.getFile(savePath + "/" + name + "-" + uid + suffix);

        try (InputStream is = href.openStream();
             FileOutputStream fos = new FileOutputStream(saveFile)) {
            StreamUtil.streamCopy(is, fos, true);
        }
        String ret = localFileTools.getSubPath(saveFile);
        return "file save to: " + ret;
    }
}
