package i2f.extension.embedding.core;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/9/30 15:59
 * @desc copy from langchain4j
 */
@Data
@NoArgsConstructor
public class EmbeddingAndTokenCount {
    protected float[] embedding;
    protected int tokenCount;

    public EmbeddingAndTokenCount(float[] embedding, int tokenCount) {
        this.embedding = embedding;
        this.tokenCount = tokenCount;
    }
}
