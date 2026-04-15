package i2f.ai.std.rag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2026/3/25 11:59
 * @desc
 */
public interface RagEmbeddingModel {
    RagVector embedAsVector(String content);

    default List<RagVector> embedAllAsVector(Collection<String> content) {
        List<RagVector> ret = new ArrayList<>();
        for (String item : content) {
            RagVector vec = embedAsVector(item);
            ret.add(vec);
        }
        return ret;
    }

    default RagEmbedding embed(String content) {
        return embed(null, content);
    }

    default RagEmbedding embed(String id, String content) {
        if (id == null || id.isEmpty()) {
            id = this.nextId();
        }
        RagEmbedding ret = new RagEmbedding();
        ret.setContent(content);
        ret.setVector(embedAsVector(content));
        ret.setId(id);
        ret.setScore(0);
        return ret;
    }

    default String nextId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    default List<RagEmbedding> embedAll(Supplier<String> idGenerator, Collection<String> content) {
        List<RagEmbedding> ret = new ArrayList<>();
        if (idGenerator == null) {
            idGenerator = this::nextId;
        }
        List<RagVector> vectors = embedAllAsVector(content);
        int i = 0;
        for (String item : content) {
            RagEmbedding embed = new RagEmbedding();
            embed.setContent(item);
            embed.setVector(vectors.get(i));
            embed.setId(idGenerator.get());
            embed.setScore(0);
            ret.add(embed);
            i++;
        }
        return ret;
    }
}
