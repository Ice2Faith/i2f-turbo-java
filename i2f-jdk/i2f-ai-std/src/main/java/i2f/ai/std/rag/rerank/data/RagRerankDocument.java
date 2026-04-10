package i2f.ai.std.rag.rerank.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Ice2Faith
 * @date 2026/4/9 8:49
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class RagRerankDocument {
    protected String text;
    protected double score;
    protected int index;
}
