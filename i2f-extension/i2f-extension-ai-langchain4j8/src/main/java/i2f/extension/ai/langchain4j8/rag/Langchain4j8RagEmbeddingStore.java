package i2f.extension.ai.langchain4j8.rag;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import i2f.ai.std.rag.RagEmbedding;
import i2f.ai.std.rag.RagEmbeddingStore;
import i2f.ai.std.rag.RagVector;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/25 14:54
 * @desc
 */
@Data
@NoArgsConstructor
public class Langchain4j8RagEmbeddingStore implements RagEmbeddingStore {
    protected EmbeddingStore<TextSegment> store;
    public Langchain4j8RagEmbeddingStore(EmbeddingStore<TextSegment> store){
        this.store=store;
    }
    @Override
    public String store(RagEmbedding embedding) {
        Embedding vo=new Embedding(embedding.getVector().toFlatArray());
        String id = store.add(vo, new TextSegment(embedding.getContent(), new Metadata(embedding.getMetadata())));
        embedding.setId(id);
        return embedding.getId();
    }

    @Override
    public void remove(String id) {
        store.remove(id);
    }

    @Override
    public List<RagEmbedding> similar(RagVector vector, int topN) {
        List<RagEmbedding> ret=new ArrayList<>();
        EmbeddingSearchResult<TextSegment> result = store.search(EmbeddingSearchRequest.builder()
                .queryEmbedding(new Embedding(vector.toFlatArray()))
                .maxResults(topN)
                .build());
        List<EmbeddingMatch<TextSegment>> matches = result.matches();
        for (EmbeddingMatch<TextSegment> match : matches) {
            String id = match.embeddingId();
            Embedding embedding = match.embedding();
            TextSegment segment = match.embedded();
            RagEmbedding vo=new RagEmbedding();
            vo.setId(id);
            vo.setVector(RagVector.fromFloatArray(embedding.vector()));
            vo.setContent(segment.text());
            vo.setScore(match.score());
            vo.setMetadata(segment.metadata().toMap());
            ret.add(vo);
        }
        return ret;
    }
}
