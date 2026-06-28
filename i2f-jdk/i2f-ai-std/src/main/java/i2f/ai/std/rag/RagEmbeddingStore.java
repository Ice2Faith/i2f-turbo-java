package i2f.ai.std.rag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/25 12:01
 * @desc
 */
public interface RagEmbeddingStore {
    String store(RagEmbedding embedding);

    default List<RagEmbedding> storeAll(Collection<RagEmbedding> list) {
        List<RagEmbedding> ret = new ArrayList<>();
        for (RagEmbedding item : list) {
            String id = store(item);
            item.setId(id);
            ret.add(item);
        }
        return ret;
    }

    void remove(String id);

    default void removeAll(Collection<String> ids) {
        for (String id : ids) {
            remove(id);
        }
    }

    List<RagEmbedding> similar(RagVector vector, int topN);

    default List<RagEmbedding> similar(RagEmbedding embedding, int topN) {
        return similar(embedding.getVector(), topN);
    }
}
