package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.rag.RagFileReader;
import i2f.ai.std.rag.impl.*;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.io.stream.StreamUtil;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/7/15 15:51
 * @desc
 */
@ConditionalOnExpression("${ai.tools.tmp-file.enable:true}")
@Data
@NoArgsConstructor
@Component
@Tools
public class TmpFileTools {
    private static final Set<String> exposeTools;

    static {
        Set<String> names = new HashSet<>();
        Method[] methods = TmpFileTools.class.getDeclaredMethods();
        for (Method method : methods) {
            Tool ann = method.getAnnotation(Tool.class);
            if (ann == null) {
                continue;
            }
            String name = method.getName();
            String value = ann.value();
            if (value != null && !value.isEmpty()) {
                name = value;
            }
            names.add(name);
        }
        exposeTools = Collections.unmodifiableSet(names);
    }

    public static Set<String> toolNames() {
        return new HashSet<>(exposeTools);
    }

    @Value("${ai.tools.tmp-file.root-path:./tmp-files}")
    protected String rootPath = "./tmp-files";

    public static final DateTimeFormatter DIR_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter CREATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String PROTOCOL = "upload";

    protected IJsonSerializer jsonSerializer = new Json2Serializer();
    protected RagFileReader fileReader = new ListableRagFileReader(
            MarkitdownCmdRagFileReader.INSTANCE,
            EasyOcrCmdRagFileReader.INSTANCE,
            PandocCmdRagFileReader.INSTANCE
    );

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "get file statistics using upload file url, include exists,length,type(dir/file)." +
                    "Non-text files (documents, images, media, etc.) will be parsed as text for the response."
    )
    public Map<String, Object> get_file_stat_using_url(@ToolParam(value = "fileUrl", description = "the fileUrl, for example 'upload://xxxxx/data.py' ")
                                                       String fileUrl) throws IOException {
        File file = getFileByUrl(fileUrl);
        file = parseAsTextFile(file);
        return FileToolUtils.getFileStat(file, getRootFile());
    }

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "get file total lines using upload file url. "
    )
    public Map<String, Object> get_file_total_lines_using_url(@ToolParam(value = "fileUrl", description = "the fileUrl, for example 'upload://xxxxx/data.py' ")
                                                              String fileUrl) throws Exception {
        File file = getFileByUrl(fileUrl);
        file = parseAsTextFile(file);
        return FileToolUtils.getFileTotalLines(file, getRootFile());
    }

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "Reads file content by line range [startLine, endLine) using upload file url.  " +
                    "Non-text files (documents, images, media, etc.) will be parsed as text for the response." +
                    "Return field `textContent` format is `<line_number> | <text_content>` of every line." +
                    "CRITICAL: The `<line_number> |` prefix is for visual positioning only. " +
                    "When extracting text for editing, you MUST strictly preserve ALL original whitespace and indentation immediately following the `|` separator."
    )
    public Map<String, Object> read_file_range_using_url(@ToolParam(value = "fileUrl", description = "the fileUrl, for example 'upload://xxxxx/data.py' ")
                                                         String fileUrl,
                                                         @ToolParam(value = "startLine", description = "start line number, value in [1,...], for example 1 or 100")
                                                         int startLine,
                                                         @ToolParam(value = "endLine", description = "end line number, value in [1,...], for example 1 or 100")
                                                         int endLine
    ) throws IOException {
        File file = getFileByUrl(fileUrl);
        file = parseAsTextFile(file);
        return FileToolUtils.readTextFileRange(file, startLine, endLine);
    }


    @Tool(
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "search keyword by regex in file, response line range in [1,...] using upload file url. " +
                    "Non-text files (documents, images, media, etc.) will be parsed as text for the response."
    )
    public List<Map<String, Object>> regex_search_file_using_url(
            @ToolParam(value = "fileUrl", description = "the fileUrl, for example 'upload://xxxxx/data.py' ")
            String fileUrl,
            @ToolParam(value = "pattern", description = "the regex pattern, java standard.")
            String pattern) throws Exception {
        File file = getFileByUrl(fileUrl);
        file = parseAsTextFile(file);
        return FileToolUtils.regexSearchTextFile(file, pattern);
    }

    public Map<String, Object> saveFile(InputStream is, String fileName) throws IOException {
        String datePath = DIR_FORMATTER.format(LocalDateTime.now());
        File dateDir = getFile(datePath);

        String uid = UUID.randomUUID().toString().replace("-", "");
        File uidDir = new File(dateDir, uid);
        uidDir.mkdirs();

        fileName = fileName.replace("\\", "/");
        int idx = fileName.lastIndexOf("/");
        if (idx >= 0) {
            fileName = fileName.substring(idx + 1);
        }
        String fullName = fileName;
        String nameOnly = fullName;
        String suffix = "";
        idx = fileName.lastIndexOf(".");
        if (idx >= 0) {
            nameOnly = fileName.substring(0, idx);
            suffix = fileName.substring(idx);
        }
        File dataFile = new File(uidDir, "data" + suffix);
        File metaFile = new File(uidDir, "metadata.json");

        StreamUtil.writeBytes(is, dataFile);

        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("fileUrl", PROTOCOL + "://" + dateDir.getName() + "/" + uidDir.getName() + "/" + dataFile.getName());
        metadata.put("fileName", fullName);
        metadata.put("createTime", CREATE_FORMATTER.format(LocalDateTime.now()));

        String json = jsonSerializer.serialize(metadata);
        StreamUtil.writeString(json, metaFile);

        return metadata;
    }

    public File parseAsTextFile(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("file not exists.");
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("file is not readable.");
        }
        if (TextFileRagFileReader.INSTANCE.support(file)) {
            return file;
        }
        if (fileReader == null) {
            throw new IllegalArgumentException("un-support parse as text content file type.");
        }
        boolean support = fileReader.support(file);
        if (!support) {
            throw new IllegalArgumentException("un-support parse as text content file type.");
        }
        file = FileToolUtils.normalizeFile(file);
        File ret = new File(file.getParentFile(), file.getName() + ".parsed");
        if (ret.exists()) {
            return ret;
        }
        String content = fileReader.read(file);
        StreamUtil.writeString(content, ret);
        return ret;
    }

    public File getFileByUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new IllegalArgumentException("fileUrl required.");
        }
        int idx = fileUrl.indexOf("://");
        if (idx < 0) {
            throw new IllegalArgumentException("fileUrl must be like [protocol]://{...}");
        }
        String protocol = fileUrl.substring(0, idx);
        if (PROTOCOL.equals(protocol)) {
            String realPath = fileUrl.substring(idx + 3);
            return getFile(realPath);
        }
        throw new IllegalArgumentException("un-support protocol: " + protocol + "://");
    }


    public File getRootFile() {
        File rootFile = new File(this.rootPath);
        rootFile = FileToolUtils.normalizeFile(rootFile);
        return rootFile;
    }

    public File getFile(String startPath) {
        File rootFile = getRootFile();
        return FileToolUtils.getSubFile(startPath, rootFile);
    }

    public String getSubPath(File file) {
        return FileToolUtils.getSubPath(file, getRootFile());
    }
}
