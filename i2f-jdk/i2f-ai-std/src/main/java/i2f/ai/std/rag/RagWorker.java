package i2f.ai.std.rag;

import i2f.ai.std.rag.impl.SimpleRecursiveRagTextSplitter;
import i2f.ai.std.rag.rerank.RagRerankModel;
import i2f.ai.std.rag.rerank.data.RagRerankDocument;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/3/25 14:41
 * @desc
 */
@Data
@NoArgsConstructor
public class RagWorker {
    protected RagEmbeddingModel model;
    protected RagEmbeddingStore store;
    protected RagRerankModel rerankModel;

    public RagWorker(RagEmbeddingModel model, RagEmbeddingStore store) {
        this.model = model;
        this.store = store;
    }

    public RagWorker model(RagEmbeddingModel model) {
        this.model = model;
        return this;
    }

    public RagWorker store(RagEmbeddingStore store) {
        this.store = store;
        return this;
    }

    public RagEmbedding store(String content) {
        RagEmbedding embedding = model.embed(null, content);
        String sid = store.store(embedding);
        embedding.setId(sid);
        return embedding;
    }

    public RagEmbedding store(String id, String content) {
        RagEmbedding embedding = model.embed(id, content);
        String sid = store.store(embedding);
        embedding.setId(sid);
        return embedding;
    }

    public List<RagEmbedding> similar(String content, int topN) {
        RagEmbedding embedding = model.embed(content);
        return store.similar(embedding, topN);
    }

    public List<RagRerankDocument> rerank(String question,List<RagEmbedding> embeddings,int topN){
        return rerankModel.rerank(question,embeddings.stream()
                .map(RagEmbedding::getContent)
                .collect(Collectors.toList()),
                topN);
    }

    public void loadDefaultDocuments() throws IOException {
        RagHelper.loadDocuments(this, new SimpleRecursiveRagTextSplitter());
    }

    public void loadDefaultDocuments(RagTextSplitter splitter) throws IOException {
        RagHelper.loadDocuments(this, splitter);
    }
}
