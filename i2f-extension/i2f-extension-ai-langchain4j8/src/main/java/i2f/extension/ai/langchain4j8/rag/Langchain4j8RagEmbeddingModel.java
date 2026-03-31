package i2f.extension.ai.langchain4j8.rag;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import i2f.ai.std.rag.RagEmbeddingModel;
import i2f.ai.std.rag.RagVector;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/25 14:47
 * @desc
 */
@Data
@NoArgsConstructor
public class Langchain4j8RagEmbeddingModel implements RagEmbeddingModel {
    protected EmbeddingModel model;

    public Langchain4j8RagEmbeddingModel(EmbeddingModel model) {
        this.model = model;
    }

    @Override
    public RagVector embedAsVector(String content) {
        Response<Embedding> embed = model.embed(content);
        Embedding embedding = embed.content();
        float[] arr = embedding.vector();
        return RagVector.fromFloatArray(arr);
    }

    @Override
    public List<RagVector> embedAllAsVector(Collection<String> content) {
        List<TextSegment> req = new ArrayList<>();
        for (String item : content) {
            req.add(TextSegment.from(item));
        }
        Response<List<Embedding>> resp = model.embedAll(req);
        List<Embedding> list = resp.content();
        List<RagVector> ret = new ArrayList<>();
        for (Embedding embedding : list) {
            ret.add(RagVector.fromFloatArray(embedding.vector()));
        }
        return ret;
    }
}
