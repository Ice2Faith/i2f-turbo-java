package i2f.tools.tools;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.os.OsUtil;
import i2f.springboot.ops.openai.tool.impl.LocalFileTools;
import i2f.springboot.ops.openai.tool.impl.TmpFileTools;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2026/8/25 16:41
 * @desc
 */
@ConditionalOnExpression("${ai.tools.pandoc.enable:false}")
@Data
@NoArgsConstructor
@Component
@Tools(tags = {
        AiTags.FILE_VALUE
})
public class PandocTools {

    @Autowired(required = false)
    protected TmpFileTools tmpFileTools;

    @Autowired(required = false)
    protected LocalFileTools localFileTools;


    public static enum FileSourceType {
        local_file,
        upload_file,
        text
    }

    @Tool(
            tags = {
                    AiTags.WRITABLE_VALUE
            },
            description = "convert(save) markdown to docx, use pandoc command impl, support multiply source type"
    )
    public TmpFileTools.FileAttachMessage pandoc_markdown_to_docx(
            @ToolParam(value = "type", description = "the content type, value in [\"local_file\", \"upload_file\", \"text\"]")
            FileSourceType type,
            @ToolParam(value = "content", description = "the content, real type according to type argument to decide.\n" +
                    " type=\"local_file\", this is local file path, such \"/docs/sample.md\". \n" +
                    " type=\"upload_file\", this is upload file url, such  \"upload://xxx/sample.md\". \n" +
                    " type=\"text\", this is full text content. \n"
            )
            String content
    ) throws Exception {
        if (tmpFileTools == null) {
            throw new IllegalStateException("current application not enable tmp file");
        }
        File sourceFile = null;
        File tmpFile = null;
        try {
            if (FileSourceType.local_file == type) {
                if (localFileTools == null) {
                    throw new IllegalStateException("current application not enable local file");
                }
                sourceFile = localFileTools.getFile(content);
            } else if (FileSourceType.upload_file == type) {
                sourceFile = tmpFileTools.getFileByUrl(content);
            } else if (FileSourceType.text == type) {
                TmpFileTools.UploadTmpFileMetadata metadata = tmpFileTools.saveFile(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), "tmp.md");
                String fileUrl = metadata.getFileUrl();
                sourceFile = tmpFileTools.getFileByUrl(fileUrl);
            } else {
                throw new IllegalArgumentException("missing or un-correct `type` argument, only support value in [\"local_file\", \"upload_file\", \"text\"]");
            }

            sourceFile = new File(sourceFile.getAbsolutePath());
            File workdir = sourceFile.getParentFile();
            tmpFile = new File(workdir, "tmp_" + (UUID.randomUUID().toString()) + ".docx");

            // pandoc -s --toc -M toc-title="" -t docx input.md -o output.docx

            String output = OsUtil.execCmd(true, 60, new String[]{
                    "pandoc",
                    "-s",
                    "--toc",
                    "-M",
                    "toc-title=\"\"",
                    "-t",
                    "docx",
                    sourceFile.getName(),
                    "-o",
                    tmpFile.getName()
            }, null, workdir, null);
            if (!tmpFile.exists()) {
                System.out.println(output);
                throw new IllegalArgumentException("convert failure!");
            }

            String name = sourceFile.getName();
            int idx = name.lastIndexOf(".");
            if (idx >= 0) {
                name = name.substring(0, idx);
            }
            TmpFileTools.UploadTmpFileMetadata metadata = tmpFileTools.saveFile(new FileInputStream(tmpFile), name + ".docx");
            tmpFile.delete();

            Map<String, Object> map = metadata.toMap();
            StringBuilder builder = new StringBuilder();
            builder.append("convert result file has upload, will send with after user message.\n");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }

            TmpFileTools.FileAttachMessage ret = new TmpFileTools.FileAttachMessage();
            ret.setFile(metadata);
            ret.setContent(builder.toString());

            return ret;
        } finally {
            if (tmpFile != null && tmpFile.exists()) {
                tmpFile.delete();
            }
        }
    }
}
