package i2f.ai.std.rag;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/25 12:01
 * @desc
 */
public interface RagEmbeddingStore {
    String store(RagEmbedding embedding);

    void remove(String id);

    List<RagEmbedding> similar(RagVector vector, int topN);

    default List<RagEmbedding> similar(RagEmbedding embedding, int topN) {
        return similar(embedding.getVector(), topN);
    }
}
