package i2f.ai.std.rag;

import i2f.io.stream.StreamUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2026/3/31 9:59
 * @desc
 */
public class RagHelper {
    public static final String[] TEXT_FILE_SUFFIXES = {
            ".txt", ".md",
            ".xml", ".json", ".html",
            ".java", ".py", ".groovy",
            ".js", ".css", ".vue", ".ts",
            ".sql", ".sh", ".bash", ".cmd",
            ".properties", ".yaml", ".yml"
    };
    public static final Set<String> TEXT_FILE_SUFFIXES_SET = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(TEXT_FILE_SUFFIXES)));
    public static final String DEFAULT_RAG_DIR = "./rags";

    public static void loadDocuments(RagWorker worker,
                                     RagTextSplitter splitter) throws IOException {
        loadDocuments(new File(DEFAULT_RAG_DIR), worker, splitter);
    }

    public static void loadDocuments(File path,
                                     RagWorker worker,
                                     RagTextSplitter splitter) throws IOException {
        loadDocuments(path, worker, splitter, (file) -> {
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
        }, null);
    }

    public static void loadDocuments(File path,
                                     RagWorker worker,
                                     RagTextSplitter splitter,
                                     Predicate<File> textFileFilter) throws IOException {
        loadDocuments(path, worker, splitter, textFileFilter, null);
    }

    public static void loadDocuments(File path,
                                     RagWorker worker,
                                     RagTextSplitter splitter,
                                     Predicate<File> textFileFilter,
                                     Consumer<RagEmbedding> listener) throws IOException {
        if (path == null) {
            return;
        }
        if (!path.exists()) {
            return;
        }
        if (path.isFile()) {
            if (textFileFilter.test(path)) {
                String text = StreamUtil.readString(path);
                List<String> list = splitter.split(text);
                for (String item : list) {
                    RagEmbedding embedding = worker.store(item);
                    if (listener != null) {
                        listener.accept(embedding);
                    }
                }
            }
        }
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    loadDocuments(file, worker, splitter, textFileFilter, listener);
                }
            }
        }
    }
}
