package i2f.ai.std.rag.impl;

import i2f.ai.std.rag.RagFileReader;
import i2f.io.stream.StreamUtil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/7/15 17:20
 * @desc
 */
public class TextFileRagFileReader implements RagFileReader {
    public static final TextFileRagFileReader INSTANCE = new TextFileRagFileReader();
    public static final String[] TEXT_FILE_SUFFIXES = {
            ".txt", ".md",
            ".xml", ".json", ".html",
            ".java", ".py", ".groovy",
            ".js", ".css", ".vue", ".ts",
            ".sql", ".sh", ".bash", ".cmd",
            ".properties", ".yaml", ".yml"
    };
    public static final Set<String> TEXT_FILE_SUFFIXES_SET = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(TEXT_FILE_SUFFIXES)));

    public static boolean isTextFile(File file) {
        String name = file.getName();
        String suffix = "";
        int idx = name.lastIndexOf(".");
        if (idx >= 0) {
            suffix = name.substring(idx).toLowerCase();
        }
        if (TEXT_FILE_SUFFIXES_SET.contains(suffix)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean support(File file) {
        return isTextFile(file);
    }

    @Override
    public String read(File file) throws IOException {
        return StreamUtil.readString(file);
    }
}
