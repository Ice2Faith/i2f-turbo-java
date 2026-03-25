package i2f.ai.std.rag.impl;

import i2f.ai.std.rag.RagEmbedding;
import i2f.ai.std.rag.RagEmbeddingStore;
import i2f.ai.std.rag.RagVector;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2026/3/25 14:12
 * @desc
 */
@Data
@NoArgsConstructor
public class InMemoryRagEmbeddingStore implements RagEmbeddingStore {
    protected final Map<String, RagEmbedding> map = new ConcurrentHashMap<>();

    @Override
    public String store(RagEmbedding embedding) {
        String id = embedding.getId();
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString().replace("-", "");
        }
        embedding.setId(id);
        embedding.setScore(0);
        map.put(embedding.getId(), embedding);
        return embedding.getId();
    }

    @Override
    public void remove(String id) {
        map.remove(id);
    }

    @Override
    public List<RagEmbedding> similar(RagVector vector, int topN) {
        List<RagEmbedding> candidate = new ArrayList<>();
        for (Map.Entry<String, RagEmbedding> entry : map.entrySet()) {
            RagEmbedding item = entry.getValue();
            RagVector v2 = item.getVector();
            double score = vector.cosineSimilar(v2);
            RagEmbedding vo = item.copy();
            vo.setScore(score);
            candidate.add(vo);
        }

        candidate.sort(RagEmbedding::compareByScoreDesc);
        List<RagEmbedding> ret = new ArrayList<>();
        for (int i = 0; i < topN; i++) {
            ret.add(candidate.get(i));
        }
        return ret;
    }


}
