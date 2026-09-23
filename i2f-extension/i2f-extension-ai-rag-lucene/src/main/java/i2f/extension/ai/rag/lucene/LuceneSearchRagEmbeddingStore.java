package i2f.extension.ai.rag.lucene;

import i2f.ai.std.rag.RagEmbedding;
import i2f.ai.std.rag.RagEmbeddingStore;
import i2f.ai.std.rag.RagVector;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import i2f.std.consts.StdConst;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/9/23 9:20
 * @desc 基于 lucene 的 rag 存储
 * 注意：lucene实现是没有向量概念的，向量是通过固定字符串Unicode转换得到的，没有实际的向量含义
 * 因此，固定搭配对应的 EmbeddingModel 才能保证正常工作
 * 效果：因为是传统搜索引擎搜索，因此语义搜索并不是那么好
 */
@Data
@NoArgsConstructor
public class LuceneSearchRagEmbeddingStore implements RagEmbeddingStore {
    public static final String DEFAULT_DATA_PATH = StdConst.RUNTIME_PERSIST_DIR + "/lucene_data/rags";
    public static final DateTimeFormatter CREATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    protected IJsonSerializer jsonSerializer = new Json2Serializer();
    protected String dataPath = DEFAULT_DATA_PATH;

    @Override
    public String store(RagEmbedding embedding) {
        try (Directory directory = getDirectory();
             IndexWriter writer = getIndexWriter(directory);) {

            return upsertEmbedding(writer, embedding);
        } catch (Exception e) {
            throw new IllegalStateException("lucene upsert document error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RagEmbedding> storeAll(Collection<RagEmbedding> list) {
        try (Directory directory = getDirectory();
             IndexWriter writer = getIndexWriter(directory);) {

            List<RagEmbedding> ret = new ArrayList<>();
            for (RagEmbedding embedding : list) {
                if (embedding == null) {
                    continue;
                }
                upsertEmbedding(writer, embedding);
                ret.add(embedding);
            }
            return ret;
        } catch (Exception e) {
            throw new IllegalStateException("lucene upsert document error: " + e.getMessage(), e);
        }
    }

    @Override
    public void remove(String id) {
        try (Directory directory = getDirectory();
             IndexWriter writer = getIndexWriter(directory);) {

            Term uniqueKey = new Term("id", id);
            writer.deleteDocuments(uniqueKey);
        } catch (Exception e) {
            throw new IllegalStateException("lucene delete document error: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeAll(Collection<String> ids) {
        try (Directory directory = getDirectory();
             IndexWriter writer = getIndexWriter(directory);) {

            for (String id : ids) {
                if (id == null) {
                    continue;
                }
                Term uniqueKey = new Term("id", id);
                writer.deleteDocuments(uniqueKey);
            }
        } catch (Exception e) {
            throw new IllegalStateException("lucene delete document error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RagEmbedding> similar(RagVector vector, int topN) {
        String content = LuceneRagEmbeddingUtil.vector2string(vector.getArray());
        return searchEmbedding(content, topN);
    }

    @Override
    public List<RagEmbedding> similar(RagEmbedding embedding, int topN) {
        String content = embedding.getContent();
        return searchEmbedding(content, topN);
    }

    public List<RagEmbedding> searchEmbedding(String content, int topN) {
        try (Directory directory = getDirectory();
             DirectoryReader reader = DirectoryReader.open(directory);) {

            IndexSearcher searcher = new IndexSearcher(reader);

            // 搜索端建议使用智能分词，减少噪音
            Analyzer searchAnalyzer = new IKAnalyzer(true);

            // 单字段检索
            // QueryParser parser = new QueryParser("content", searchAnalyzer);

            // 关键改动：用 MultiFieldQueryParser 替代 QueryParser
            String[] fields = {"content", "meta_data"};
            MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, searchAnalyzer);
            // 可选：设置默认操作符为 AND（默认是 OR）
            // parser.setDefaultOperator(MultiFieldQueryParser.Operator.AND);


            Query query = parser.parse(content);

            TopDocs topDocs = searcher.search(query, topN);
            ScoreDoc[] hits = topDocs.scoreDocs;

            List<RagEmbedding> ret = new ArrayList<>();
            for (ScoreDoc hit : hits) {
                Document doc = searcher.doc(hit.doc);

                RagEmbedding embedding = new RagEmbedding();
                embedding.setId(doc.get("id"));
                embedding.setContent(doc.get("content"));
                String vector = doc.get("vector");
                if (vector == null || vector.isEmpty() || "null".equals(vector)) {
                    // lucene 没有有效向量，直接返回固定编码无意义向量
                    embedding.setVector(RagVector.fromArray(LuceneRagEmbeddingUtil.string2vector(embedding.getContent())));
                } else {
                    // lucene 如果有有效向量，则取出设置
                    try {
                        List list = (List) jsonSerializer.deserialize(vector);
                        List<Double> vec = new ArrayList<>();
                        for (Object obj : list) {
                            if (obj instanceof Number) {
                                Number number = (Number) obj;
                                vec.add(number.doubleValue());
                            } else {
                                vec.add(new BigDecimal(String.valueOf(obj)).doubleValue());
                            }
                        }
                        embedding.setVector(RagVector.fromList(vec));
                    } catch (Exception e) {
                        // ignore
                        // 失败则使用默认向量占位
                        embedding.setVector(RagVector.fromArray(LuceneRagEmbeddingUtil.string2vector(embedding.getContent())));
                    }
                }
                embedding.setMetadata(new HashMap<>());
                try {
                    embedding.setMetadata(jsonSerializer.deserializeAsMap(doc.get("meta_data")));
                } catch (Exception e) {
                    // ignore
                }
                embedding.setScore(hit.score);
                ret.add(embedding);
            }
            return ret;
        } catch (Exception e) {
            throw new IllegalStateException("lucene search error: " + e.getMessage(), e);
        }
    }

    public String upsertEmbedding(IndexWriter writer, RagEmbedding embedding) throws IOException {
        String id = embedding.getId();
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString().replace("-", "");
        }
        embedding.setId(id);

        Document doc = new Document();
        doc.add(new StringField("id", embedding.getId(), Field.Store.YES));
        doc.add(new TextField("content", embedding.getContent(), Field.Store.YES));
        if (embedding.getVector() == null
                || embedding.getVector().getArray() == null
                || embedding.getVector().getArray().length == 0) {
            // lucene 没有向量有效值，不存储，占位
            doc.add(new TextField("vector", "null", Field.Store.YES));
        } else {
            // 如果真有有效的向量，那就存储
            doc.add(new TextField("vector", jsonSerializer.serialize(embedding.getVector().getArray()), Field.Store.YES));
        }
        doc.add(new TextField("meta_data", jsonSerializer.serialize(embedding.getMetadata()), Field.Store.YES));
        doc.add(new TextField("create_time", CREATE_FORMATTER.format(LocalDateTime.now()), Field.Store.YES));

        // upsert
        Term uniqueKey = new Term("id", id);
        writer.updateDocument(uniqueKey, doc);

        return embedding.getId();
    }

    public Directory getDirectory() throws IOException {
        Directory directory = FSDirectory.open(Paths.get(dataPath));
        return directory;
    }

    public IndexWriter getIndexWriter(Directory directory) throws IOException {
        // 索引端建议使用细粒度分词，提高召回率
        Analyzer indexAnalyzer = new IKAnalyzer(false);


        IndexWriterConfig config = new IndexWriterConfig(indexAnalyzer);
        if (Files.exists(Paths.get(dataPath))
                && Files.list(Paths.get(dataPath)).findFirst().isPresent()) {
            config.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
        } else {
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        }

        // 配置合并策略
        TieredMergePolicy mergePolicy = new TieredMergePolicy();
        mergePolicy.setSegmentsPerTier(8);          // 每层最多8个段
        mergePolicy.setMaxMergeAtOnce(8);           // 单次最多合并8个段
        mergePolicy.setMaxMergedSegmentMB(2048);    // 合并后最大2GB
        mergePolicy.setFloorSegmentMB(4.0);         // 小段阈值4MB
        mergePolicy.setDeletesPctAllowed(20.0);     // 删除比例超过15%优先合并
        config.setMergePolicy(mergePolicy);

        // 增大内存缓冲区，减少flush频率，从源头减少小段产生
        config.setRAMBufferSizeMB(64);  // 默认16MB，增大到64MB

        // 使用并发合并调度器，后台异步合并，不阻塞写入
        config.setMergeScheduler(new ConcurrentMergeScheduler());

        IndexWriter writer = new IndexWriter(directory, config);

        return writer;
    }


}
