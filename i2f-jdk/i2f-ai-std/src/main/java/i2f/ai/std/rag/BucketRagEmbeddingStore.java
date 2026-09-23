package i2f.ai.std.rag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/25 12:01
 * @desc
 */
public interface BucketRagEmbeddingStore {
    String store(RagEmbedding embedding, String bucket);

    default List<RagEmbedding> storeAll(Collection<RagEmbedding> list, String bucket) {
        List<RagEmbedding> ret = new ArrayList<>();
        for (RagEmbedding item : list) {
            String id = store(item, bucket);
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

    List<RagEmbedding> similar(RagVector vector, int topN, Collection<String> buckets);

    default List<RagEmbedding> similar(RagEmbedding embedding, int topN, Collection<String> buckets) {
        return similar(embedding.getVector(), topN, buckets);
    }
}
