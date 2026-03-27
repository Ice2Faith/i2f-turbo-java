package i2f.ai.std.rag;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
}
