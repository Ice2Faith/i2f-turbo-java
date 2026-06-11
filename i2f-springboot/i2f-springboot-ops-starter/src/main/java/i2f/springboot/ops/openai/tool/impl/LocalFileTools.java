package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.io.stream.StreamUtil;
import i2f.match.impl.AntMatcher;
import i2f.match.regex.RegexUtil;
import i2f.match.regex.data.RegexMatchItem;
import i2f.springboot.ops.util.HumanUtil;
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
@Tools
public class LocalFileTools {
    public static final DateTimeFormatter BACKUP_TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private static final AntMatcher matcher = new AntMatcher("/");

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
        List<Map<String, Object>> ret = new ArrayList<>();

        File rootFile = getRootFile();

        File startFile = getFile(startPath);

        search_files_next(ret, startFile, pattern, maxDeep, rootFile);
        return ret;
    }

    @Tool(
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "Reads text file content by line range [startLine, endLine). " +
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
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int lineNumber = 1;
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (lineNumber >= endLine) {
                    break;
                }
                if (lineNumber >= startLine) {
                    builder.append(lineNumber).append(" |").append(line).append("\n");
                }
                lineNumber++;
            }
            Map<String, Object> ret = new HashMap<>();
            ret.put("realizeStartLine", Math.min(startLine, lineNumber));
            ret.put("realizeEndLine", Math.min(endLine, lineNumber));
            ret.put("textContent", builder.toString());
            ret.put("hasMoreLine", (reader.readLine() != null));
            return ret;
        }
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
        String content = StreamUtil.readString(saveFile);
        List<RegexMatchItem> list = RegexUtil.regexFinds(content, pattern);

        List<Map<String, Object>> ret = new ArrayList<>();
        for (RegexMatchItem item : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("matchedText", item.getMatchStr());
            map.put("startOffset", item.getIdxStart());
            map.put("endOffset", item.getIdxEnd());
            map.put("startLineNumber", findLineNumberByOffset(content, item.getIdxStart()));
            map.put("endLineNumber", findLineNumberByOffset(content, item.getIdxEnd()));
            ret.add(map);
        }
        return ret;
    }

    @Tool(
            tags = {
                    AiTags.WRITABLE_VALUE
            },
            description = "replace file lines in range [startLine,endLine) with new content"
    )
    public Map<String, Object> replace_file_lines(@ToolParam(value = "filePath", description = "file path, for example / or /user")
                                                  String filePath,
                                                  @ToolParam(value = "startLine", description = "start line number, value in [1,...], for example 1 or 100")
                                                  int startLine,
                                                  @ToolParam(value = "endLine", description = "end line number, value in [1,...], for example 1 or 100")
                                                  int endLine,
                                                  @ToolParam(value = "content", description = "new content to replace, cloud be multi-line string")
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

        String[] contentLines = content.split("\n");

        // 如果 startLine 大于文件总行数，则在末尾追加
        if (realizeStartLine > totalLines) {
            // 将 content 按行分割后追加到文件末尾
            for (String contentLine : contentLines) {
                lines.add(contentLine);
            }
        } else {
            // 删除 [realizeStartLine, realizeEndLine) 范围内的行
            int deleteCount = realizeEndLine - realizeStartLine;
            for (int i = 0; i < deleteCount; i++) {
                if (realizeStartLine - 1 < lines.size()) {
                    lines.remove(realizeStartLine - 1);
                }
            }

            // 在 realizeStartLine - 1 位置插入新内容
            int insertIndex = realizeStartLine - 1;
            for (int i = contentLines.length - 1; i >= 0; i--) {
                lines.add(insertIndex, contentLines[i]);
            }
        }

        // 写回文件
        String writeText = String.join("\n", lines);
        StreamUtil.writeString(writeText, file);

        // 返回结果
        Map<String, Object> ret = new HashMap<>();
        ret.put("realizeStartLine", realizeStartLine);
        ret.put("realizeEndLine", realizeEndLine);
        ret.put("replacedLineCount", Math.max(0, realizeEndLine - realizeStartLine));
        ret.put("newLineCount", contentLines.length);
        ret.put("totalLinesAfter", lines.size());
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
            File bakFile = new File(dir, name + ".bak" + BACKUP_TIMESTAMP_FORMATTER.format(LocalDateTime.now()) + suffix);
            try (FileInputStream fis = new FileInputStream(saveFile);
                 FileOutputStream fos = new FileOutputStream(bakFile)) {
                StreamUtil.streamCopy(fis, fos, true);
            }
        }
    }

    public int findLineNumberByOffset(String content, int offset) {
        if (content == null) {
            return -1;
        }
        int line = 0;
        int len = content.length();
        for (int i = 0; i < len; i++) {
            char ch = content.charAt(i);
            if (ch == '\n') {
                line++;
            }
            if (i == offset) {
                return line + 1;
            }
        }
        return -1;
    }

    public File getRootFile() {
        File rootFile = new File(this.rootPath);
        rootFile = normalizeFile(rootFile);
        return rootFile;
    }

    public File normalizeFile(File file) {
        file = new File(file.getAbsolutePath());
        return file;
    }

    public File getFile(String startPath) {
        File rootFile = getRootFile();
        return getSubFile(startPath, rootFile);
    }

    public File getSubFile(String startPath, File rootFile) {
        rootFile = normalizeFile(rootFile);

        String absRootPath = rootFile.getAbsolutePath();
        absRootPath = absRootPath.replace("\\", "/");
        if (!absRootPath.endsWith("/")) {
            absRootPath = absRootPath + "/";
        }

        File startFile = rootFile;
        if (startPath != null && !startPath.isEmpty()) {
            startFile = new File(rootFile, startPath);
            startFile = normalizeFile(startFile);
        }
        String absStartPath = startFile.getAbsolutePath();
        absStartPath = absStartPath.replace("\\", "/");
        if (!absStartPath.endsWith("/")) {
            absStartPath = absStartPath + "/";
        }

        if (!absStartPath.endsWith(absRootPath)
                && !absStartPath.startsWith(absRootPath)) {
            throw new IllegalArgumentException("path not allow access! path=" + startPath);
        }

        return startFile;
    }

    public void search_files_next(List<Map<String, Object>> ret, File startFile, String pattern, int maxDeep, File rootFile) {
        if (maxDeep == 0) {
            return;
        }
        if (startFile == null) {
            return;
        }
        if (!startFile.exists()) {
            return;
        }
        if (startFile.isFile()) {
            acceptFile(startFile, ret, pattern, rootFile);
        } else if (startFile.isDirectory()) {
            try {
                File[] files = startFile.listFiles();
                if (files != null) {
                    for (File file : files) {
                        search_files_next(ret, file, pattern, maxDeep - 1, rootFile);
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    public String getSubPath(File file) {
        return getSubPath(file, getRootFile());
    }

    public String getSubPath(File file, File rootFile) {
        file = normalizeFile(file);
        rootFile = normalizeFile(rootFile);

        String absRootPath = rootFile.getAbsolutePath();
        absRootPath = absRootPath.replace("\\", "/");
        if (!absRootPath.endsWith("/")) {
            absRootPath = absRootPath + "/";
        }

        String absolutePath = file.getAbsolutePath();
        absolutePath = absolutePath.replace("\\", "/");
        if (!absolutePath.startsWith(absRootPath)) {
            return null;
        }
        absolutePath = absolutePath.substring(absRootPath.length());
        if (!absolutePath.startsWith("/")) {
            absolutePath = "/" + absolutePath;
        }
        return absolutePath;
    }

    public void acceptFile(File startFile, List<Map<String, Object>> ret, String pattern, File rootFile) {
        if (startFile == null) {
            return;
        }
        File file = new File(startFile.getAbsolutePath());
        String absolutePath = getSubPath(file, rootFile);
        if (pattern == null || pattern.isEmpty()) {
            Map<String, Object> item = new HashMap<>();
            item.put("file", absolutePath);
            item.put("byteSize", file.length());
            item.put("humanSize", HumanUtil.humanFileSize(file.length()));
            ret.add(item);
            return;
        }

        if (matcher.matches(absolutePath, pattern)) {
            Map<String, Object> item = new HashMap<>();
            item.put("file", absolutePath);
            item.put("byteSize", file.length());
            item.put("humanSize", HumanUtil.humanFileSize(file.length()));
            ret.add(item);
        }
    }
}
