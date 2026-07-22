package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.io.file.FileUtil;
import i2f.io.stream.StreamUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/6/2 11:34
 * @desc
 */
@ConditionalOnExpression("${ai.tools.file.enable:true}")
@Data
@NoArgsConstructor
@Component
@Tools(tags = {
        AiTags.FILE_VALUE
})
public class LocalFileTools {
    public static final DateTimeFormatter BACKUP_TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    @Value("${ai.tools.file.root-path:./ai-root}")
    protected String rootPath = "./ai-root";

    @Value("${ai.tools.file.backup-file:true}")
    protected boolean backupFile = true;

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "search files by ant pattern"
    )
    public List<Map<String, Object>> search_files(@ToolParam(value = "startPath", description = "start search path, cloud be null means from root, for example / or /user")
                                                  String startPath,
                                                  @ToolParam(value = "pattern", description = "match pattern, ant match style, for example /**/*.java or /**/*user*")
                                                  String pattern,
                                                  @ToolParam(value = "maxDeep", description = "max search deep, -1 means unlimited, for example 3 or 10")
                                                  int maxDeep) {

        File rootFile = getRootFile();
        File startFile = getFile(startPath);

        return FileToolUtils.searchFiles(startFile, pattern, maxDeep, rootFile);
    }

    @Tool(
            tags = {
                    AiTags.WRITABLE_VALUE
            },
            description = "make directory"
    )
    public boolean mkdirs(@ToolParam(value = "path", description = "create path, support multiply path(s), for example /user or /user/a/b ")
                          String path) {
        File file = getFile(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return true;
    }

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "get file statistics, include exists,length,type(dir/file)"
    )
    public Map<String, Object> get_file_stat(@ToolParam(value = "path", description = "the path, for example /user or /user/a.txt ")
                                             String path) {
        File file = getFile(path);
        return FileToolUtils.getFileStat(file, getRootFile());
    }

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "get text file total lines"
    )
    public Map<String, Object> get_file_total_lines(@ToolParam(value = "path", description = "the path, for example /user or /user/a.txt ")
                                                    String path) throws Exception {
        File file = getFile(path);
        return FileToolUtils.getFileTotalLines(file, getRootFile());
    }

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "Reads text file content by line range [startLine, endLine). Note: Inclusive startLine, exclusive endLine." +
                    "Return field `textContent` format is `<line_number> | <text_content>` of every line." +
                    "CRITICAL: The `<line_number> |` prefix is for visual positioning only. " +
                    "When extracting text for editing, you MUST strictly preserve ALL original whitespace and indentation immediately following the `|` separator."
    )
    public Map<String, Object> read_text_file_range(@ToolParam(value = "filePath", description = "file path, for example / or /user")
                                                    String filePath,
                                                    @ToolParam(value = "startLine", description = "start line number, value in [1,...], for example 1 or 100")
                                                    int startLine,
                                                    @ToolParam(value = "endLine", description = "end line number, value in [1,...], for example 1 or 100")
                                                    int endLine
    ) throws IOException {
        File file = getFile(filePath);
        return FileToolUtils.readTextFileRange(file, startLine, endLine);
    }


    @Tool(
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "search keyword by regex in file, response line range in [1,...]."
    )
    public List<Map<String, Object>> regex_search_text_file(
            @ToolParam(value = "filePath", description = "file path, for example /test.txt or /user/User.java")
            String filePath,
            @ToolParam(value = "pattern", description = "the regex pattern, java standard.")
            String pattern) throws Exception {
        File saveFile = getFile(filePath);
        return FileToolUtils.regexSearchTextFile(saveFile, pattern);
    }

    @Tool(
            tags = {
                    AiTags.WRITABLE_VALUE
            },
            description = "replace file lines in range [startLine,endLine) with new content. Note: Inclusive startLine, exclusive endLine."
    )
    public Map<String, Object> replace_file_lines(@ToolParam(value = "filePath", description = "file path, for example / or /user")
                                                  String filePath,
                                                  @ToolParam(value = "startLine", description = "start line number, value in [1,...], for example 1 or 100")
                                                  int startLine,
                                                  @ToolParam(value = "endLine", description = "end line number, value in [1,...], for example 1 or 100")
                                                  int endLine,
                                                  @ToolParam(value = "content", description = "new content to replace, cloud be multi-line string, cloud be null means delete this line")
                                                  String content) throws IOException {
        File file = getFile(filePath);
        // 如果开启了备份，那就一定要先备份
        if (backupFile) {
            backupFile(file);
        }

        // 读取所有行
        List<String> lines = new ArrayList<>();
        String text = StreamUtil.readString(file);
        lines.addAll(Arrays.asList(text.split("\n")));

        int totalLines = lines.size();
        int realizeStartLine = Math.max(1, startLine);
        int realizeEndLine = Math.min(endLine, totalLines + 1);

        List<String> prefixLines = new ArrayList<>();
        List<String> suffixLines = new ArrayList<>();

        for (int i = 1; i < totalLines + 1; i++) {
            if (i >= realizeStartLine && i < realizeEndLine) {
                continue;
            }
            if (i < realizeStartLine) {
                prefixLines.add(lines.get(i - 1));
            } else if (i >= realizeEndLine) {
                suffixLines.add(lines.get(i - 1));
            }
        }

        int newLineCount = 0;
        List<String> saveLines = new ArrayList<>();
        saveLines.addAll(prefixLines);
        if (content != null) {
            String[] contentLines = content.split("\n");
            newLineCount = contentLines.length;
            saveLines.addAll(Arrays.asList(contentLines));
        }
        saveLines.addAll(suffixLines);

        String writeText = String.join("\n", saveLines);
        StreamUtil.writeString(writeText, file);

        // 返回结果
        Map<String, Object> ret = new HashMap<>();
        ret.put("realizeStartLine", realizeStartLine);
        ret.put("realizeEndLine", realizeEndLine);
        ret.put("replacedLineCount", Math.max(0, realizeEndLine - realizeStartLine));
        ret.put("newLineCount", newLineCount);
        ret.put("totalLinesAfter", saveLines.size());
        return ret;
    }

    @Tool(
            tags = {
                    AiTags.WRITABLE_VALUE
            },
            description = "write text content to file, if the path not exists will auto created."
    )
    public boolean write_text_file(
            @ToolParam(value = "filePath", description = "file path, for example /test.txt or /user/User.java")
            String filePath,
            @ToolParam(value = "content", description = "the text content")
            String content,
            @ToolParam(value = "append", description = "append mode if is true, cloud be null means cover or create new")
            Boolean append
    ) throws Exception {
        File saveFile = getFile(filePath);
        // 如果开启了备份，那就一定要先备份
        if (backupFile) {
            backupFile(saveFile);
        }
        File dir = saveFile.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        try (FileWriter writer = new FileWriter(saveFile, append != null && append)) {
            writer.write(content);
            return true;
        }
    }

    @Tool(
            tags = {
                    AiTags.WRITABLE_VALUE
            },
            description = "replace keyword by regex in file, use String.replaceAll(...) implements, response line range in [1,...]."
    )
    public boolean regex_replace_text_file(
            @ToolParam(value = "filePath", description = "file path, for example /test.txt or /user/User.java")
            String filePath,
            @ToolParam(value = "pattern", description = "the regex pattern, java standard.")
            String pattern,
            @ToolParam(value = "replacement", description = "the replace text, java standard.")
            String replacement) throws Exception {
        File saveFile = getFile(filePath);
        if (backupFile) {
            backupFile(saveFile);
        }


        String content = StreamUtil.readString(saveFile);
        content = content.replaceAll(pattern, replacement);

        StreamUtil.writeString(content, saveFile);

        return true;
    }

    @Tool(
            tags = {
                    AiTags.WRITABLE_VALUE
            },
            description = "delete file or directory"
    )
    public boolean delete_file(@ToolParam(value = "path", description = "delete path, for example /user or /user/a/b ")
                               String path) throws IOException {
        File file = getFile(path);
        FileUtil.moveToTrash(file);
        return true;
    }

    public void backupFile(File saveFile) throws IOException {
        File dir = saveFile.getParentFile();
        if (saveFile.exists()) {
            String name = saveFile.getName();
            int idx = name.lastIndexOf(".");
            String suffix = "";
            if (idx >= 0) {
                suffix = name.substring(idx);
                name = name.substring(0, idx);
            }
            dir = new File(dir, ".backup");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File bakFile = new File(dir, name + ".bak" + BACKUP_TIMESTAMP_FORMATTER.format(LocalDateTime.now()) + suffix);
            try (FileInputStream fis = new FileInputStream(saveFile);
                 FileOutputStream fos = new FileOutputStream(bakFile)) {
                StreamUtil.streamCopy(fis, fos, true);
            }
        }
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
