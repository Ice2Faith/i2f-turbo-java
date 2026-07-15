package i2f.springboot.ops.openai.tool.impl;

import i2f.io.stream.StreamUtil;
import i2f.match.impl.AntMatcher;
import i2f.match.regex.RegexUtil;
import i2f.match.regex.data.RegexMatchItem;
import i2f.springboot.ops.util.HumanUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/7/15 15:36
 * @desc
 */
public class FileToolUtils {
    private static final AntMatcher matcher = new AntMatcher("/");

    public static List<Map<String, Object>> searchFiles(File startFile, String pattern, int maxDeep, File rootFile) {
        List<Map<String, Object>> ret = new ArrayList<>();
        search_files_next(ret, startFile, pattern, maxDeep, rootFile);
        return ret;
    }

    public static Map<String, Object> getFileStat(File file, File rootFile) {
        Map<String, Object> ret = new HashMap<>();
        boolean exists = file.exists();
        ret.put("path", getSubPath(file, rootFile));
        ret.put("exists", exists);
        if (exists) {
            ret.put("lengthInBytes", file.length());
            ret.put("lengthInHuman", HumanUtil.humanFileSize(file.length()));
            if (file.isFile()) {
                ret.put("type", "file");
            }
            if (file.isDirectory()) {
                ret.put("type", "dir");
            }
            ret.put("canExecute", file.canExecute());
            ret.put("canRead", file.canRead());
            ret.put("canWrite", file.canWrite());
        }
        return ret;
    }

    public static Map<String, Object> getFileTotalLines(File file, File rootFile) throws IOException {
        Map<String, Object> ret = new HashMap<>();
        boolean exists = file.exists();
        ret.put("path", getSubPath(file, rootFile));
        ret.put("exists", exists);
        ret.put("totalLines", -1);
        if (exists) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                int count = 0;
                String line = null;
                while ((line = reader.readLine()) != null) {
                    count++;
                }
                ret.put("totalLines", count);
            }
        }
        return ret;
    }

    public static Map<String, Object> readTextFileRange(File file, int startLine, int endLine) throws IOException {
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

    public static List<Map<String, Object>> regexSearchTextFile(File file, String pattern) throws IOException {
        String content = StreamUtil.readString(file);
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


    public static void search_files_next(List<Map<String, Object>> ret, File startFile, String pattern, int maxDeep, File rootFile) {
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


    public static void acceptFile(File startFile, List<Map<String, Object>> ret, String pattern, File rootFile) {
        if (startFile == null) {
            return;
        }
        File file = new File(startFile.getAbsolutePath());
        String absolutePath = FileToolUtils.getSubPath(file, rootFile);
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

    public static File normalizeFile(File file) {
        file = new File(file.getAbsolutePath());
        return file;
    }

    public static String getSubPath(File file, File rootFile) {
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

    public static File getSubFile(String startPath, File rootFile) {
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

    public static int findLineNumberByOffset(String content, int offset) {
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
}
