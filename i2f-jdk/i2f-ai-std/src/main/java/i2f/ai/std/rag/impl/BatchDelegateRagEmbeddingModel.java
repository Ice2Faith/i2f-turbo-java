package i2f.ai.std.rag.impl;

import i2f.ai.std.rag.RagEmbedding;
import i2f.ai.std.rag.RagEmbeddingModel;
import i2f.ai.std.rag.RagVector;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2026/7/2 14:38
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class BatchDelegateRagEmbeddingModel implements RagEmbeddingModel {
    protected RagEmbeddingModel target;
    protected int batchSize = 10;

    public BatchDelegateRagEmbeddingModel(RagEmbeddingModel target, int batchSize) {
        this.target = target;
        this.batchSize = batchSize;
    }

    @Override
    public RagVector embedAsVector(String content) {
        return target.embedAsVector(content);
    }

    @Override
    public List<RagVector> embedAllAsVector(Collection<String> content) {
        List<RagVector> ret = new ArrayList<>();
        List<String> once = new ArrayList<>();
        int count = 0;
        for (String item : content) {
            once.add(item);
            count++;
            if (count == batchSize) {
                List<RagVector> list = target.embedAllAsVector(once);
                ret.addAll(list);
                count = 0;
                once.clear();
            }
        }
        if (count > 0) {
            List<RagVector> list = target.embedAllAsVector(once);
            ret.addAll(list);
            count = 0;
            once.clear();
        }
        return ret;
    }

    @Override
    public RagEmbedding embed(String content) {
        return target.embed(content);
    }

    @Override
    public RagEmbedding embed(String id, String content) {
        return target.embed(id, content);
    }

    @Override
    public String nextId() {
        return target.nextId();
    }

    @Override
    public List<RagEmbedding> embedAll(Collection<String> content) {
        List<RagEmbedding> ret = new ArrayList<>();
        List<String> once = new ArrayList<>();
        int count = 0;
        for (String item : content) {
            once.add(item);
            count++;
            if (count == batchSize) {
                List<RagEmbedding> list = target.embedAll(once);
                ret.addAll(list);
                count = 0;
                once.clear();
            }
        }
        if (count > 0) {
            List<RagEmbedding> list = target.embedAll(once);
            ret.addAll(list);
            count = 0;
            once.clear();
        }
        return ret;
    }

    @Override
    public List<RagEmbedding> embedAll(Supplier<String> idGenerator, Collection<String> content) {
        List<RagEmbedding> ret = new ArrayList<>();
        List<String> once = new ArrayList<>();
        int count = 0;
        for (String item : content) {
            once.add(item);
            count++;
            if (count == batchSize) {
                List<RagEmbedding> list = target.embedAll(idGenerator, once);
                ret.addAll(list);
                count = 0;
                once.clear();
            }
        }
        if (count > 0) {
            List<RagEmbedding> list = target.embedAll(idGenerator, once);
            ret.addAll(list);
            count = 0;
            once.clear();
        }
        return ret;
    }
}
