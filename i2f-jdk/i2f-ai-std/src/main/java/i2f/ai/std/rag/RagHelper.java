package i2f.ai.std.rag;

import i2f.ai.std.rag.data.RagLoadDocumentsOptions;
import i2f.ai.std.rag.impl.SimpleRecursiveRagTextSplitter;
import i2f.ai.std.rag.impl.TextFileRagFileReader;
import i2f.io.stream.StreamUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/31 9:59
 * @desc
 */
public class RagHelper {
    public static final String DEFAULT_RAG_DIR = "./rags";

    public static void loadDocuments(RagWorker worker,
                                     RagTextSplitter splitter) throws IOException {
        loadDocuments(new File(DEFAULT_RAG_DIR), worker, new RagLoadDocumentsOptions().toMutator()
                .set(p -> p::setSplitter, splitter)
                .done());
    }


    public static void loadDocuments(File path,
                                     RagWorker worker,
                                     RagLoadDocumentsOptions options) throws IOException {
        if (path == null) {
            return;
        }
        if (!path.exists()) {
            return;
        }
        if (options == null) {
            options = new RagLoadDocumentsOptions().toMutator()
                    .set(u -> u::setSplitter, new SimpleRecursiveRagTextSplitter())
                    .set(u -> u::setFileFilter, TextFileRagFileReader::isTextFile)
                    .set(u -> u::setStoreBatchSize, -1)
                    .done();
        }
        options.toMutator()
                .fieldIfAbsent(u -> u::getSplitter, SimpleRecursiveRagTextSplitter::new)
                .fieldIfAbsentV(u -> u::getFileFilter, TextFileRagFileReader::isTextFile)
                .done();
        if (path.isFile()) {
            boolean supportProcess = false;
            boolean useCustomReader = false;
            if (options.getFileFilter() == null || options.getFileFilter().test(path)) {
                supportProcess = true;
            }
            if (options.getFileReader() != null && options.getFileReader().support(path)) {
                useCustomReader = true;
                supportProcess = true;
            }
            if (!supportProcess) {
                return;
            }
            String text = null;
            if (useCustomReader) {
                text = options.getFileReader().read(path);
            } else {
                text = StreamUtil.readString(path);
            }
            if (text == null) {
                return;
            }
            if (text.trim().isEmpty()) {
                return;
            }
            List<String> list = options.getSplitter().split(text);
            if (options.getStoreBatchSize() > 0) {
                List<String> once = new ArrayList<>();
                int count = 0;
                for (String str : list) {
                    once.add(str);
                    count++;
                    if (count == options.getStoreBatchSize()) {
                        List<RagEmbedding> embeddings = worker.storeAll(once);
                        if (options.getListener() != null) {
                            for (RagEmbedding item : embeddings) {
                                options.getListener().accept(item);
                            }
                        }
                        once.clear();
                        count = 0;
                    }
                }
                if (count > 0) {
                    List<RagEmbedding> embeddings = worker.storeAll(once);
                    if (options.getListener() != null) {
                        for (RagEmbedding item : embeddings) {
                            options.getListener().accept(item);
                        }
                    }
                    once.clear();
                    count = 0;
                }
            } else {
                List<RagEmbedding> embeddings = worker.storeAll(list);
                if (options.getListener() != null) {
                    for (RagEmbedding item : embeddings) {
                        options.getListener().accept(item);
                    }
                }
            }


        }
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    loadDocuments(file, worker, options);
                }
            }
        }
    }
}
