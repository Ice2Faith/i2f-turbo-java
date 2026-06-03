package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.match.impl.AntMatcher;
import i2f.springboot.ops.util.HumanUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private AntMatcher matcher = new AntMatcher("/");

    @Value("${ai.tools.file.root-path:./ai-root}")
    protected String rootPath = "./ai-root";

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
            description = "read text file content by line range [startLine,endLine)"
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
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int lineNumber = 1;
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (lineNumber >= endLine) {
                break;
            }
            if (lineNumber >= startLine) {
                builder.append(line).append("\n");
            }
            lineNumber++;
        }
        Map<String, Object> ret = new HashMap<>();
        ret.put("realizeStartLine", Math.min(startLine, lineNumber));
        ret.put("realizeEndLine", Math.min(endLine, lineNumber));
        ret.put("textContent", builder.toString());
        ret.put("hasMoreLine", (line != null));
        return ret;
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
