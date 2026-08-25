package i2f.tools.tools;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.io.stream.StreamUtil;
import i2f.os.OsUtil;
import i2f.resources.ResourceUtil;
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
import java.io.InputStream;
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
            description = "Convert markdown content to a docx document. \n" +
                    "Supports input methods: direct text (type=\"text\"), local file path (type=\"local_file\"), and upload file link (type=\"upload_file\"). \n" +
                    "The most commonly used is type=\"text\" — just pass the markdown content directly, no need to save a file first.\n" +
                    "Note: must setting `type` argument."
    )
    public TmpFileTools.FileAttachMessage pandoc_markdown_to_docx(
            @ToolParam(value = "type", description = "the content type, not null, value in [\"local_file\", \"upload_file\", \"text\"]")
            FileSourceType type,
            @ToolParam(value = "content", description = "the content, real type according to `type` argument to decide.\n" +
                    " `type`=\"local_file\", this is local file path, such \"/docs/sample.md\". \n" +
                    " `type`=\"upload_file\", this is upload file url, such  \"upload://xxx/sample.md\". \n" +
                    " `type`=\"text\", this is full markdown text content, such \"# title\". \n"
            )
            String content
    ) throws Exception {
        if (tmpFileTools == null) {
            throw new IllegalStateException("current application not enable tmp file");
        }
        File sourceFile = null;
        File tmpFile = null;
        File referenceFile=null;
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
            referenceFile=new File(workdir,"ref_" + (UUID.randomUUID().toString().replace("-","")) + ".docx");
            tmpFile = new File(workdir, "tmp_" + (UUID.randomUUID().toString().replace("-","")) + ".docx");

            InputStream is = ResourceUtil.getClasspathResourceAsStream("assets/pandoc/custom-reference.docx");
            StreamUtil.writeBytes(is,referenceFile);


            // pandoc -s --toc -M toc-title="" -t docx input.md -o output.docx --reference-doc=custom-reference.docx

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
                    tmpFile.getName(),
                    "--reference-doc="+referenceFile.getName()
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
            referenceFile.delete();

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
            if(referenceFile!=null && referenceFile.exists()){
                referenceFile.delete();
            }
            if (tmpFile != null && tmpFile.exists()) {
                tmpFile.delete();
            }
        }
    }
}
