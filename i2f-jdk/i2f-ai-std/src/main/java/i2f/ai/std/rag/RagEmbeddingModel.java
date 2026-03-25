package i2f.ai.std.rag;

import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2026/3/25 11:59
 * @desc
 */
public interface RagEmbeddingModel {
    RagVector embedAsVector(String content);

    default RagEmbedding embed(String content){
        return embed(null,content);
    }

    default RagEmbedding embed(String id, String content) {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString().replace("-", "");
        }
        RagEmbedding ret = new RagEmbedding();
        ret.setContent(content);
        ret.setVector(embedAsVector(content));
        ret.setId(id);
        ret.setScore(0);
        return ret;
    }
}
